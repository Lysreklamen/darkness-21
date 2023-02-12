package darkness.libsvg;

import java.awt.Dimension
import java.io.File
import java.io.FileOutputStream
import javax.swing.JFrame
import javax.swing.WindowConstants

/**
 * Entry point of the standalone build of libsvg
 *
 * Takes the arguments
 * --flatten <float> : the minimum distance a flattened curve can diverge from the real smooth curve
 * --splitlines <float> : the maximum distance between points in the outline of the letters
 * -o <filename> : the filename to write a pattern file to
 * -v : Display a UI of the parsed SVG
 * <filename.svg> : the SVG file to parse
 */
class Entry {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            var fileName: String? = null
            var outputFileName: String? = null
            var flatness = 10.0f
            var maxLineLength = 10.0f
            var visualize = false
            var scale = 1.0f

            var index = 0
            while (index < args.size) {
                val arg = args[index]
                if (arg == "--flatten" && index + 1 < args.size) {
                    flatness = args[++index].toFloat()
                } else if (arg == "--splitlines" && index + 1 < args.size) {
                    maxLineLength = args[++index].toFloat()
                } else if (arg == "-o" && index + 1 < args.size) {
                    outputFileName = args[++index]
                } else if (arg == "-v") {
                    visualize = true
                } else if ((arg == "--scale" || arg == "-s") && index + 1 < args.size) {
                    scale = args[++index].toFloat()
                } else {
                    if (fileName != null) {
                        error("Filename can not be provided twice")
                    }
                    fileName = arg
                }
                index++
            }

            if (fileName == null) {
                error("Filename for the target SVG must be provided")
            }

            // Load the SVG file into Batik
            val svgFile = File(fileName)
            val parser = SVGParser(svgFile, flatness, maxLineLength, scale)
            parser.parse()

            val gen = PatternGenerator(parser)
            println(gen.pattern)
            if (outputFileName != null) {
                val of = File(outputFileName)
                FileOutputStream(of).use {
                    it.write(gen.pattern.toByteArray())
                }
            }

            if (visualize) {
                val ui = JFrame()
                ui.title = "SVG Visualizer"
                ui.size = Dimension(640, 480)
                ui.contentPane.add(Visualizer(parser))
                ui.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
                ui.isVisible = true;
            }
        }
    }
}
