package darkness.generator.api

import java.awt.Color

/**
 * Either an individual bulb ([BulbRGB]), or a set of bulbs ([BulbGroup]).
 */
interface BulbSet {
    /**
     * If this is a single bulb, return the red component of that bulb.
     * Otherwise, return the red component of the first bulb in the group.
     */
    val red: Int

    /**
     * If this is a single bulb, return the green component of that bulb.
     * Otherwise, return the green component of the first bulb in the group.
     */
    val green: Int

    /**
     * If this is a single bulb, return the blue component of that bulb.
     * Otherwise, return the blue component of the first bulb in the group.
     */
    val blue: Int

    /**
     * If this is a single bulb, return the RGB color of that bulb.
     * Otherwise, return the RGB color of the first bulb in the group.
     */
    val color: Color

    /**
     * Gets the position (or average of positions of multiple bulbs).
     * @return a 3 element array with x, y and z position. (Should be meters from the bottom left corner of the sign). z is normally set to 0.0f
     */
    val position: FloatArray

    /**
     * Set the bulb(s) to the given RGB color.
     */
    operator fun set(red: Int, green: Int, blue: Int, setter: ScriptBase)

    /**
     * Set the bulb(s) to the given RGB color.
     */
    operator fun set(color: Color, setter: ScriptBase)

    /**
     * Set the bulb(s) to the given HSB color.
     */
    fun setHSB(hue: Float, saturation: Float, brightness: Float, setter: ScriptBase)

    /**
     * Set the bulb(s) to the given RGB color. Out-of-range values will be coerced to [0-255].
     */
    fun setCoerced(red: Double, green: Double, blue: Double, setter: ScriptBase) {
        set(
                Math.max(0, Math.min(255, Math.round(red))).toInt(),
                Math.max(0, Math.min(255, Math.round(green))).toInt(),
                Math.max(0, Math.min(255, Math.round(blue))).toInt(),
                setter)
    }

    /**
     * Relinquish control over the bulb(s). If no other script(s) in the same frame set the bulb(s),
     * it/they will turn black; otherwise, the other script(s) will win.
     */
    fun relinquish(setter: ScriptBase)
}
