package darkness.generator.api

import com.zoominfo.util.yieldreturn.Generator
import darkness.generator.api.effects.EffectBase
import darkness.generator.api.effects.Hold
import darkness.generator.api.effects.RGBFade
import darkness.generator.api.effects.HSBFade

import java.awt.*

/**
 * A [ScriptBase] can be anything that controls channel values, either over time or as a one-shot action.
 * Subclasses of [EffectBase] will typically be one single effect (which may last across several frames),
 * while direct subclasses of [ScriptBase] will typically be scripts that are a collection of effects.
 * Subclasses should implement [.run] as a sequence of calls to the methods in this class.
 * Each method's effect will start (and possibly end) in the current frame, and [.next] and [.skip]
 * will advance to the next frame.
 */
abstract class ScriptBase : Generator<Void>() {
    var priority: Int = 0
    var isCancelled: Boolean = false
        private set

    /**
     * Convenience method for accessing the bulb with id `id`.
     */
    protected fun bulb(id: Int): BulbRGB {
        return BulbManager.getBulb(id)
    }

    /**
     * Convenience method for creating a [BulbGroup] for accessing all the bulbs with the ids given by `ids`.
     */
    protected fun group(vararg ids: Int): BulbGroup {
        val bulbs = ids.map(::bulb)
        return BulbGroup(bulbs)
    }

    /**
     * The run function of the script is responsible for generating the sequence.
     */
    public abstract override fun run()

    open fun cancel() {
        isCancelled = true
    }

    /*************************************************
     * Sequence functions below
     */

    /**
     * Declares the current frame in this script or effect to be finished, and starts a new frame.
     */
    protected operator fun next() {
        yield(null)
    }

    /**
     * Skips a given number of frames before returning.
     * This is equivalent to calling [next][.next] a given number of times.
     * @param frames The number of frames to wait.
     */
    protected fun skip(frames: Int) {
        var i = 0
        while (i < frames && !isCancelled) {
            next()
            i++
        }
    }

    /**
     * Runs an effect in parallel to the current script.
     */
    protected fun effect(effect: EffectBase) {
        ScriptManager.registerEffect(effect)
    }

    /**
     * Runs another script in parallel to the current script.
     */
    protected fun merge(script: ScriptBase) {
        ScriptManager.registerScript(script)
    }

    /**
     * Sets a color on a bulb.
     * @param bulbSet The bulb or bulb group to set the color on
     * @param red Value between 0..255
     * @param green Value between 0..255
     * @param blue Value between 0..255
     */
    protected operator fun set(bulbSet: BulbSet, red: Int, green: Int, blue: Int) {
        bulbSet.set(red, green, blue, this)
    }

    /**
     * Sets a color on a bulb.
     */
    protected operator fun set(bulbSet: BulbSet, color: Color) {
        bulbSet.set(color, this)
    }

    /**
     * Sets a HSB color on a bulb.
     * @param bulbSet The bulb or bulb group to set the color on
     * @param hue The floor of this number is subtracted from it to create a fraction between 0 and 1. This fractional number is then multiplied by 360 to produce the hue angle in the HSB color model.
     * @param saturation In the range 0.0..1.0
     * @param brightness In the range 0.0..1.0
     */
    protected fun setHSB(bulbSet: BulbSet, hue: Float, saturation: Float, brightness: Float) {
        bulbSet.setHSB(hue, saturation, brightness, this)
    }

    /**
     * Set the bulb(s) to the given RGB color. Out-of-range values will be coerced to [0-255].
     */
    protected fun setCoerced(bulbSet: BulbSet, red: Double, green: Double, blue: Double) {
        bulbSet.setCoerced(red, green, blue, this)
    }

    /**
     * Can be used from the main script to override the color of a bulb or bulb group which would otherwise be set by a subscript or effect.
     * As opposed to [.set], its effect can last for many frames.
     */
    protected fun hold(bulbSet: BulbSet, color: Color, frames: Int) {
        effect(Hold(bulbSet, color, frames))
    }

    /**
     * Can be used from the main script to override the color of a bulb or bulb group which would otherwise be set by a subscript or effect.
     * As opposed to [.set], its effect can last for many frames.
     */
    protected fun hold(bulbSet: BulbSet, red: Int, green: Int, blue: Int, frames: Int) {
        hold(bulbSet, Color(red, green, blue), frames)
    }

    /**
     * Can be used from the main script to override the color of a bulb or bulb group which would otherwise be set by a subscript or effect.
     * As opposed to [.setHSB], its effect can last for many frames.
     */
    protected fun holdHSB(bulbSet: BulbSet, hue: Float, saturation: Float, brightness: Float, frames: Int) {
        hold(bulbSet, Color.getHSBColor(hue, saturation, brightness), frames)
    }

    /**
     * Relinquish control over the bulb(s). If no other script(s) in the same frame set the bulb(s),
     * it/they will turn black; otherwise, the other script(s) will win.
     */
    protected fun relinquish(bulbSet: BulbSet) {
        bulbSet.relinquish(this)
    }

    /**
     * Starts a new [RGBFade] effect on the given bulb(s).
     */
    protected fun rgbFade(bulbSet: BulbSet, color: Color, duration: Int) {
        effect(RGBFade(bulbSet, color, duration))
    }

    /**
     * Starts a new [HSBFade] effect on the given bulb(s).
     */
    protected fun hsbFade(bulbSet: BulbSet, color: FloatArray, duration: Int) {
        effect(HSBFade(bulbSet, color, duration))
    }

    /**
     * Starts a new [HSBFade] effect on the given bulb(s).
     */
    protected fun hsbFade(bulbSet: BulbSet, hue: Float, saturation: Float, brightness: Float, duration: Int) {
        hsbFade(bulbSet, floatArrayOf(hue, saturation, brightness), duration)
    }

    /**
     * Starts a new [RGBFade] effect on the given bulb(s).
     */
    protected fun rgbFade(bulbSet: BulbSet, red: Int, green: Int, blue: Int, duration: Int) {
        rgbFade(bulbSet, Color(red, green, blue), duration)
    }
}
