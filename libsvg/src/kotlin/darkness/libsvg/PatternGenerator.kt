package darkness.libsvg

import java.awt.geom.Rectangle2D

class PatternGenerator(val parser: SVGParser) {

    val pattern: String
        get() {
            val output = StringBuilder()
            val scale = 0.01f

            output.append("""
                {
                    "format_versin": "v0.1.0",
                    "groups": [
                """.trimIndent() + "\n"
            )

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

            for ((s, shape) in parser.letters.withIndex()) {
                output.append("""        {
            "pos": [0.0, 0.0],
            "alu": {
                "outline": [
"""
                )

                printPoints(shape.outline, output, 20)
                output.append("${" ".repeat(16)}],\n")
                output.append("${" ".repeat(16)}\"holes\": [\n")

                for ((i,holePoints) in shape.holes.withIndex()){
                    output.append("${" ".repeat(20)}[\n")
                    printPoints(holePoints, output, 24)
                    output.append("${" ".repeat(20)}]")
                    if( i != shape.holes.size-1){
                        output.append(",")
                    }
                    output.append("\n")
                }
                output.append("${" ".repeat(16)}]\n")
                output.append("${" ".repeat(12)}},\n")
                output.append("${" ".repeat(12)}\"bulbs\": [\n")

                for((index, bulbEntry) in parser.bulbs.entries.withIndex()) {
                    val (bulbId, bulb) = bulbEntry
                    var rChan = 200 +bulbId
                    var gChan = 300 +bulbId
                    var bChan = 400 +bulbId
                    if (bulbId >= 100) {
                        rChan = bulbId-100
                        gChan = 50+bulbId-100
                        bChan = 100+bulbId-100
                    }
                    output.append("${" ".repeat(16)}")
                    output.append("[${bulbId}, ${bulb.x}, ${-bulb.y}, ${rChan}, ${gChan}, ${bChan}]")
                    if(index != parser.bulbs.size - 1){
                        output.append(",")
                    }
                    output.append("\n")
                }
                output.append("${" ".repeat(12)}")
                output.append("]\n")

                output.append("${" ".repeat(8)}}")
                if(s != parser.letters.size -1){
                    output.append(",")
                }
                output.append("\n")

            }
            output.append("]\n}")

            output.append("\n\n")

//            for ((bulbId, bulb) in parser.bulbs.entries) {
//                var rChan = 200 +bulbId
//                var gChan = 300 +bulbId
//                var bChan = 400 +bulbId
//                if (bulbId >= 100) {
//                    rChan = bulbId-100
//                    gChan = 50+bulbId-100
//                    bChan = 100+bulbId-100
//                }
//                output.append("%d (%.2f, %.2f) R %d %d %d\n".format(bulbId, bulb.x / 100.0f, bulb.y / 100.0f, rChan, gChan, bChan))
//            }
//            output.append("\n\n")

            return output.toString()
        }

    private fun printPoints(points: List<SVGParser.Point>, output: StringBuilder, indentation: Int) {
        for ((i, p) in points.withIndex()) {
            output.append("${" ".repeat(indentation)}[${p.x}, ${-p.y}]")
            if (i != points.size - 1) {
                output.append(",")
            }
            output.append("\n")
        }
    }
}
