package darkness.generator.api.effects

import darkness.generator.api.BulbGroup

import java.awt.*
import java.util.Random

class Aurora(
    private val bulbGroup: BulbGroup,
    private val color: Color,
    /** In seconds */
    private val time: Int,
    /** In frames */
    private val fade: Int,
    /** Number of bulbs that possibly change between blocks */
    private val nChangeBulbs: Int,
    private val minBrightness: Float
) : EffectBase() {
    private val rnd: Random = Random(1337)

    override fun run() {
        val hsbValues = Color.RGBtoHSB(color.red, color.green, color.blue, null)

        val nRepeats = time * 20 / fade

        for (j in 0 until nRepeats) {
            for (i in 0 until nChangeBulbs) {
                val nextBulbIdx = rnd.nextInt(bulbGroup.numBulbs)
                val nextBulb = bulbGroup.getBulb(nextBulbIdx)
                var nextBrightness = rnd.nextFloat()
                while (nextBrightness <= minBrightness) {
                    nextBrightness = rnd.nextFloat()
                }
                val c = Color.getHSBColor(hsbValues[0], hsbValues[1], nextBrightness)
                rgbFade(nextBulb, c, fade)
            }
            skip(fade)
        }
    }

    override fun toString() = "Effect Aurora"
}
