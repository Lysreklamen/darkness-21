package darkness.generator.api.effects

import darkness.generator.api.BulbGroup

import java.awt.Color
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

    override suspend fun run() {
        val (hue, saturation, _) = Color.RGBtoHSB(color.red, color.green, color.blue, null)
        val nRepeats = time * 20 / fade
        val previousTargetBrightness = mutableMapOf<Int, Float>()

        for (j in 0 until nRepeats) {
            val bulbIndexesUsedThisRound = mutableSetOf<Int>()
            for (i in 0 until nChangeBulbs) {
                val bulbIndex = rnd.nextInt(bulbGroup.numBulbs)
                val bulb = bulbGroup.getBulb(bulbIndex)
                var nextBrightness = rnd.nextFloat()
                while (nextBrightness <= minBrightness) {
                    nextBrightness = rnd.nextFloat()
                }
                // Don't skip until here, so that the random generator stays in sync with the old version
                if (!bulbIndexesUsedThisRound.add(bulbIndex)) {
                    continue
                }
                val previousColor = Color.getHSBColor(hue, saturation, previousTargetBrightness.getOrDefault(bulbIndex, 0f))
                val nextColor = Color.getHSBColor(hue, saturation, nextBrightness)
                rgbFade(bulb, previousColor, nextColor, fade)
                previousTargetBrightness[bulbIndex] = nextBrightness
            }
            skip(fade)
        }
    }

    override fun toString() = "Effect Aurora"
}
