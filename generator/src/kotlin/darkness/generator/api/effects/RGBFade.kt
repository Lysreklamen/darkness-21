package darkness.generator.api.effects

import darkness.generator.api.BulbSet

import java.awt.Color

class RGBFade(
    private val bulbSet: BulbSet,
    private val endColor: Color,
    private val frames: Int
) : EffectBase() {
    private val startColor = bulbSet.color
    private val delta_r: Float
    private val delta_g: Float
    private val delta_b: Float

    init {
        this.delta_r = (endColor.red - startColor.red).toFloat() / frames.toFloat()
        this.delta_g = (endColor.green - startColor.green).toFloat() / frames.toFloat()
        this.delta_b = (endColor.blue - startColor.blue).toFloat() / frames.toFloat()
    }

    override fun run() {
        var frame = 1
        while (frame <= frames && !isCancelled) {
            val red = startColor.red + (delta_r * frame).toInt()
            val green = startColor.green + (delta_g * frame).toInt()
            val blue = startColor.blue + (delta_b * frame).toInt()

            set(bulbSet, red, green, blue)
            next()
            frame++
        }
    }

    override fun toString() = "Effect RGBFade on $bulbSet from color: $startColor to color: $endColor over $frames frames."
}
