package darkness.generator.api

import java.awt.*

/**
 * An RGB bulb, whose color is defined by three channels.
 * Constructor creates a new bulb that is controlled by the given channels.
 */
class BulbRGB(
    /** The bulb id. */
    val id: Int,
    /** The channel that controls the red component of this bulb. */
    val channelRed: Channel,
    /** The channel that controls the green component of this bulb. */
    val channelGreen: Channel,
    /** The channel that controls the blue component of this bulb. */
    val channelBlue: Channel,
    /** The x position. */
    posX: Float,
    /** The y position. */
    posY: Float
) : BulbSet {
    override val position = floatArrayOf(-1.0f, -1.0f, 0.0f)

    /** The RGB color that is indicated by the current values of this bulb's channels. */
    override val color: Color
        get() = Color(red, green, blue)

    /** The red component that is indicated by the current values of this bulb's red channel. */
    override val red: Int
        get() = channelRed.value

    /** The green component that is indicated by the current values of this bulb's green channel. */
    override val green: Int
        get() = channelGreen.value

    /** The blue component that is indicated by the current values of this bulb's blue channel. */
    override val blue: Int
        get() = channelBlue.value

    init {
        this.position[0] = posX
        this.position[1] = posY
    }

    /**
     * Set the RGB color of this bulb, by setting the individual channels to the given values.
     */
    override fun set(red: Int, green: Int, blue: Int, setter: ScriptBase) {
        channelRed.setValue(red, setter)
        channelGreen.setValue(green, setter)
        channelBlue.setValue(blue, setter)
    }

    /**
     * Set the RGB color of this bulb, by setting the individual channels to the components of the given [Color].
     */
    override fun set(color: Color, setter: ScriptBase) {
        set(color.red, color.green, color.blue, setter)
    }

    /**
     * Set the RGB color of this bulb, by setting the individual channels to the components of the given hexadecimal RGB string.
     * `hexColor` must start with "0x" or "#".
     */
    operator fun set(hexColor: String, setter: ScriptBase) {
        set(Color.decode(hexColor), setter)
    }

    /**
     * Set a HSB color of this bulb, by setting the individual channels to the components of the RGB color that corresponds to the given HSB color.
     * @param hue The floor of this number is subtracted from it to create a fraction between 0 and 1. This fractional number is then multiplied by 360 to produce the hue angle in the HSB color model.
     * @param saturation In the range 0.0..1.0
     * @param brightness In the range 0.0..1.0
     */
    override fun setHSB(hue: Float, saturation: Float, brightness: Float, setter: ScriptBase) {
        set(Color.getHSBColor(hue, saturation, brightness), setter)
    }

    override fun relinquish(setter: ScriptBase) {
        set(Channel.relinquish, Channel.relinquish, Channel.relinquish, setter)
    }

    /**
     * @return A string representation of this bulb, indicating its current channel values.
     */
    override fun toString(): String {
        return "Bulb{R:" + channelRed + ",G:" + channelGreen + "B:" + channelBlue + "}"
    }
}
