package darkness.libsvg

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter.Indenter
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

/**
 * This class, along with [PatternGroup] and [PatternAlu], match the structure of the JSON file
 * format that is expected by websim for the pattern file.
 */
private data class Pattern(
    val formatVersion: String,
    val groups: List<PatternGroup>,
)

private data class PatternGroup(
    val pos: List<Float>,
    val alu: PatternAlu,
    val bulbs: List<List<Any>>,
)

private data class PatternAlu(
    val outline: List<List<Float>>,
    val holes: List<List<List<Float>>>,
)

private val prettyPrinter = DefaultPrettyPrinter()
    .withArrayIndenter(
        object : Indenter {
            override fun writeIndentation(g: JsonGenerator?, level: Int) {
                val list = g?.currentValue as? List<*>
                // Write points (two floats) and bulbs (six elements, first is bulb id) on a single line,
                // but write all other arrays with each element on its own line and indented
                if (list?.size == 2 && list.all { it is Float } || list?.size == 6 && list[0] is Int) {
                    g.writeRaw(' ')
                } else {
                    g?.writeRaw("\n" + "  ".repeat(level))
                }
            }
            override fun isInline() = false
        },
    )

private val jsonMapper = jacksonObjectMapper().writer(prettyPrinter)

/**
 * This class accepts an [SVGParser] that has already parsed an SVG file. Calling [generate] will
 * return a JSON file for the pattern, in the format that is expected by websim.
 */
class PatternGenerator(private val parser: SVGParser) {
    fun generate(): String {
        val pattern = Pattern(
            formatVersion = "v0.1.0",
            groups = parser.letters.map { letter ->
                PatternGroup(
                    pos = listOf(0.0f, 0.0f),
                    alu = PatternAlu(
                        outline = letter.outline.map { point -> listOf(point.x, -point.y) },
                        holes = letter.holes.map { hole ->
                            hole.map { point -> listOf(point.x, -point.y) }
                        },
                    ),
                    bulbs = letter.bulbs.map { (bulbId, bulb) ->
                        val (redChannel, greenChannel, blueChannel) = if (bulbId >= 100) {
                            Triple(bulbId - 100, 50 + bulbId - 100, 100 + bulbId - 100)
                        } else {
                            Triple(200 + bulbId, 300 + bulbId, 400 + bulbId)
                        }
                        listOf(bulbId, bulb.x, -bulb.y, redChannel, greenChannel, blueChannel)
                    }
                )
            }
        )

        return jsonMapper.writeValueAsString(pattern)
    }
}
