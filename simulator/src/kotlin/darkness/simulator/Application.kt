package darkness.simulator

import com.jme3.app.SimpleApplication
import com.jme3.scene.Node
import com.jme3.system.AppSettings
import darkness.generator.api.ScriptBase
import darkness.generator.api.ScriptManager
import darkness.generator.output.PgmOutput
import darkness.simulator.dmx.BulbManager
import darkness.simulator.dmx.ChannelManager
import darkness.simulator.graphics.Aluminum
import darkness.simulator.graphics.Background
import darkness.simulator.graphics.Point
import darkness.simulator.graphics.Scene
import kotlinx.coroutines.runBlocking
import java.awt.Color
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.lang.Float.parseFloat
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.collections.ArrayList
import darkness.generator.api.BulbManager as GeneratorBulbManager

/**
 * The simulator's main class.
 */
class Application(private val arguments: Arguments) : SimpleApplication() {
    // These constants are rather uninteresting with their current values,
    // but we'll keep them in case we need to shift things around at some point

    private lateinit var player: PgmPlayer

    init {
        isShowSettings = false
        val setting = AppSettings(true)
        setting["Width"] = 1280
        setting["Height"] = 720
        setting["Title"] = "Darkness Simulator"
        setting["VSync"] = true
        setSettings(setting)
        isPauseOnLostFocus = false
    }

    override fun simpleInitApp() {
        val scene = Scene()
        parsePatternFile(arguments.patternFileName, scene.parentNodeForBulbs, scene.signNode)
        val pgmReaders = ArrayList<PgmReader>()
        if (arguments.scriptClassName != null) {
            pgmReaders.add(generatePgmFromScript(arguments.scriptClassName))
        }
        for (pgmFileName in arguments.sequenceFileNames) {
            pgmReaders.add(PgmReader(pgmFileName))
        }
        player = PgmPlayer(pgmReaders)
        player.start()
        CommandSocket(player).start()
    }

    private fun parsePatternFile(fileName: String?, lightNode: Node, signNode: Node) {
        val file: File
        try {
            file = File(fileName)
        } catch (ex: NullPointerException) {
            System.err.println("Pattern file '$fileName' does not exist!")
            throw ex // TODO: Actually handle exception
        }

        val fileReader = FileReader(file)
        val reader = BufferedReader(fileReader)

        var offset = Point(0f, 0f)
        var scale = Point(1f, 1f) // Not really a point, but it consists of one float for x and one for y, so let's reuse the class

        var lineNumber = 0
        var line: String? = reader.readLine()
        while (line != null) {
            lineNumber++
            try {
                if (line.trim { it <= ' ' }.isEmpty()) {
                    line = reader.readLine()
                    continue
                }
                val parts = line.split("[ \\t]+".toRegex()).dropLastWhile { it.isEmpty() }
                val maybeInstruction = parts[0].toUpperCase()
                if (maybeInstruction == "OFFSET") {
                    offset = Point(java.lang.Float.parseFloat(parts[1]), java.lang.Float.parseFloat(parts[2]))
                    line = reader.readLine()
                    continue
                }
                if (maybeInstruction == "SCALE") {
                    scale = Point(java.lang.Float.parseFloat(parts[1]), java.lang.Float.parseFloat(parts[2]))
                    line = reader.readLine()
                    continue
                }
                if (maybeInstruction == "BACKGROUND") {
                    val spec = line.substring(maybeInstruction.length+1)
                    /*
                   The format of the BACKGROUND looks like this
                   (X.xx, Y.yy, W.idth, H.eight); path/to/background.png
                   The background image is optional. Default background is a black square
                    */

                    val elements = spec.split(";")
                    if (elements.size > 2) {
                        System.err.println("Could not parse BACKGROUND on line $lineNumber: $line")
                        line = reader.readLine()
                        continue
                    }
                    val background : String
                    if (elements.size == 2) {
                        background = elements[1].trim()
                    } else {
                        background = ""
                    }

                    val coords = elements[0].split(',')
                    val topLeft = parsePoint(coords[0], coords[1], offset, scale)
                    val size = parsePoint(coords[2], coords[3], offset, scale)

                    signNode.attachChild(Background(topLeft.x, topLeft.y, size.x, size.y, background))
                }
                if (maybeInstruction == "ALU" || maybeInstruction == "ALUOPEN") {
                    val closed = maybeInstruction == "ALU"
                    val spec = line.substring(maybeInstruction.length+1)

                    /*
                    The format of the ALU looks like this
                    (X.xx, Y.yy); (X.xx, Y.yy); (X.xx, Y.yy); (X.xx, Y.yy); -; (X.xx, Y.yy); (X.xx, Y.yy); -; (X.xx, Y.yy); (X.xx, Y.yy);
                    The dash separates polygons within the same letter. This is done to support letters with holes in them
                     */

                    val polygons = ArrayList<ArrayList<Point>>()
                    var activePolygon = ArrayList<Point>()
                    polygons.add(activePolygon)

                    val elements = spec.replace(" ", "").split(';')
                    for (elem in elements) {
                        if (elem.isBlank()) {
                            continue
                        }
                        if (elem == "-") {
                            // New polygon within the same shape
                            activePolygon = ArrayList()
                            polygons.add(activePolygon)
                            continue
                        }
                        val coords = elem.split(',')
                        if (coords.size != 2) {
                            System.err.println("Could not parse ALU profile on line $lineNumber: $line")
                            break
                        }

                        activePolygon.add(parsePoint(coords[0], coords[1], offset, scale))
                    }

                    lightNode.attachChild(Aluminum(polygons[0], polygons.subList(1, polygons.size), closed, "Aluminum:$lineNumber"))
                    line = reader.readLine()
                    continue
                }
                if (parts.size < 7 || parts.size % 2 != 1) {
                    System.err.println("Parse error on line $lineNumber: $line")
                    line = reader.readLine()
                    continue
                }

                val id = Integer.parseInt(parts[0])
                val position = parsePoint(parts[1], parts[2], offset, scale)
                val channelRed = Integer.parseInt(parts[4])
                val channelGreen = Integer.parseInt(parts[5])
                val channelBlue = Integer.parseInt(parts[6])

                val bulb = BulbManager.registerBulb(id,
                        ChannelManager.getChannel(channelRed),
                        ChannelManager.getChannel(channelGreen),
                        ChannelManager.getChannel(channelBlue),
                        position,
                        lightNode)
                if (arguments.scriptClassName != null) {
                    // If we want to use the generator, its bulb manager must also be populated
                    GeneratorBulbManager.registerBulb(id, channelRed, channelGreen, channelBlue, position.x, position.y)
                }

                // Default color in case no sequence or script is supplied
                val hue = (position.x * position.x + position.y * position.y) / (10.0f * 10.0f + 1.0f * 1.0f)
                val color = Color.getHSBColor(hue, 1f, 0.9f)
                bulb.set(color)
            } catch (ex: Exception) {
                // Rethrow the exception but write the line number first
                System.err.println("An exception occurred while parsing line $lineNumber of the pattern file.")
                throw ex
            }

            line = reader.readLine()
        }

        reader.close()
    }

    private fun parsePoint(xStr: String, yStr: String, offset: Point, scale: Point): Point {
        val x = (parseFloat(xStr.replace("[,;()]".toRegex(), "")) - offset.x) * scale.x
        val y = (parseFloat(yStr.replace("[,;()]".toRegex(), "")) - offset.y) * -scale.y
        return Point(x, y)
    }

    override fun simpleUpdate(tpf: Float) {
        player.update()
    }

    private fun generatePgmFromScript(scriptClassName: String): PgmReader {
        val qualifiedScriptClassName = if (scriptClassName.contains(".")) scriptClassName else "darkness.generator.scripts.uka19.$scriptClassName"
        val script = Class.forName(qualifiedScriptClassName).getConstructor().newInstance() as ScriptBase
        val tempFile = File("sequences/uka19/${arguments.scriptClassName}.pgm")
        runBlocking { ScriptManager.start(script, PgmOutput(tempFile.path)) }
        return PgmReader(tempFile.path)
    }

    class Arguments(
        val patternFileName: String,
        val sequenceFileNames: List<String>,
        val scriptClassName: String?
    ) {
        companion object {
            fun parse(args: Array<String>): Application.Arguments {
                var patternFileName: String? = null
                val sequenceFileNames = ArrayList<String>()
                var scriptClassName: String? = null
                var i = 0
                while (i < args.size) {
                    when (args[i++]) {
                        "--pattern" -> patternFileName = args[i++]
                        "--playlist" -> {
                            val playlist = args[i++]
                            sequenceFileNames.addAll(Files.readAllLines(Paths.get(playlist), StandardCharsets.UTF_8))
                        }
                        "--sequence" -> sequenceFileNames.add(args[i++])
                        "--script" -> scriptClassName = args[i++]
                    }
                }
                if (patternFileName == null) {
                    throw java.lang.Exception("--pattern is required")
                }
                return Application.Arguments(patternFileName, sequenceFileNames, scriptClassName)
            }
        }
    }

    companion object {
        @JvmStatic
        lateinit var instance: Application
            private set

        @JvmStatic
        fun main(args: Array<String>) {
            instance = Application(Arguments.parse(args))
            instance.start()
        }
    }
}
