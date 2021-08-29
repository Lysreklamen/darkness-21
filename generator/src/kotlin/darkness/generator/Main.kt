package darkness.generator

import darkness.generator.api.BulbManager
import darkness.generator.api.ScriptBase
import darkness.generator.api.ScriptManager
import darkness.generator.output.PgmOutput
import kotlinx.coroutines.runBlocking
import net.sourceforge.argparse4j.ArgumentParsers
import net.sourceforge.argparse4j.inf.ArgumentParserException
import org.json.JSONArray
import org.json.JSONObject
import java.math.BigDecimal
import kotlin.system.exitProcess

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        println("Darkness sequence generator started")
        val parser = ArgumentParsers.newFor("darkness-generator").build()
            .defaultHelp(true)
            .description("A program for generating pattern sequences based on scripts")

        parser.addArgument("scene").help("The scene name to use. Ie 'uka21'")
        parser.addArgument("script").help("The script to run")

        var sceneName = ""
        var scriptName = ""
        try {
            val ns = parser.parseArgs(args)
            sceneName = ns["scene"]
            scriptName = ns["script"]
        } catch (e: ArgumentParserException) {
            parser.handleError(e)
            exitProcess(1)
            return
        }


        val sceneFile = javaClass.classLoader.getResourceAsStream("scenes/${sceneName}/scene.json");
        if (sceneFile == null) {
            println("Scene not found")
            exitProcess(1)
        }

        val sceneJsonBytes = sceneFile.readBytes();
        val scene = JSONObject(sceneJsonBytes.decodeToString())

        registerBulbsFromScene(scene);

        val scriptClass = Main.javaClass.classLoader.loadClass("darkness.generator.scripts.${sceneName}.${scriptName}")
        val script = scriptClass.getConstructor().newInstance() as ScriptBase

        PgmOutput("output/${scriptName}.pgm").use { output ->
            runBlocking {
                ScriptManager.start(script, output)
            }
        }
    }

    private fun registerBulbsFromScene(scene: JSONObject) {
        val groups = scene["groups"] as JSONArray
        for (gi in 0 until groups.length()) {
            val group = groups.getJSONObject(gi);
            val groupPos = group["pos"] as JSONArray
            val groupX = groupPos.getDouble(0)
            val groupY = groupPos.getDouble(1)

            val bulbs = group.getJSONArray("bulbs")

            for (bi in 0 until bulbs.length()) {
                val bulb = bulbs.getJSONArray(bi)
                val id = bulb.getInt(0)
                val x = bulb.getDouble(1)
                val y = bulb.getDouble(2)

                val r = bulb.getInt(3)
                val g = bulb.getInt(4)
                val b = bulb.getInt(5)

                BulbManager.registerBulb(id, r, g, b, (groupX+x).toFloat(), (groupY+y).toFloat())
            }
        }
    }
}
