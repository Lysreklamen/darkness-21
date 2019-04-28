package darkness.libsvg

import java.awt.geom.Rectangle2D

class PatternGenerator(val parser: SVGParser) {

    val pattern: String
        get() {
            val output = StringBuilder()
            val scale = 0.01f

            output.append("OFFSET 0 0\n")
            output.append("SCALE 1 1\n")
            output.append("\n\n")

            if (!parser.backgroundOutline.isEmpty) {
                output.append("BACKGROUND (%.3f, %.3f, %.3f, %.3f)".format(parser.backgroundOutline.x * scale,
                    parser.backgroundOutline.y * scale,
                    parser.backgroundOutline.width * scale,
                    parser.backgroundOutline.height * scale))

                if (parser.backgroundTexture.isNotBlank()) {
                    output.append("; ").append(parser.backgroundTexture)
                }
                output.append("\n\n")
            }

            for (shape in parser.letters) {
                output.append("ALU")
                for (p in shape.outline) {
                    output.append(" (%.3f, %.3f);".format(p.x / 100.0f, p.y / 100.0f))
                }
                for (hole in shape.holes) {
                    output.append(" -;")
                    for (p in hole) {
                        output.append(" (%.3f, %.3f);".format(p.x / 100.0f, p.y / 100.0f))
                    }
                }
                output.append("\n\n")
            }

            output.append("\n\n")

            for ((bulbId, bulb) in parser.bulbs.entries) {
                var rChan = 200 +bulbId
                var gChan = 300 +bulbId
                var bChan = 400 +bulbId
                if (bulbId >= 100) {
                    rChan = bulbId-100
                    gChan = 50+bulbId-100
                    bChan = 100+bulbId-100
                }
                output.append("%d (%.2f, %.2f) R %d %d %d\n".format(bulbId, bulb.x / 100.0f, bulb.y / 100.0f, rChan, gChan, bChan))
            }
            output.append("\n\n")

            return output.toString()
        }
}
