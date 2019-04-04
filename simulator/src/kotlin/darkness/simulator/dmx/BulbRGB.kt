package darkness.simulator.dmx

import com.jme3.scene.Node
import darkness.simulator.graphics.Bulb
import darkness.simulator.graphics.Point

import java.awt.Color

class BulbRGB(
    val channelRed: Channel,
    val channelGreen: Channel,
    val channelBlue: Channel,
    position: Point,
    parentNode: Node
) {
    private val bulb: Bulb
    private val controller = ChannelController()

    val color: Color
        get() = Color(red, green, blue)

    val red: Int
        get() = channelRed.value

    val green: Int
        get() = channelGreen.value

    val blue: Int
        get() = channelBlue.value

    init {
        this.channelRed.owner = controller
        this.channelGreen.owner = controller
        this.channelBlue.owner = controller
        this.bulb = Bulb(position, parentNode, toString())
    }

    operator fun set(red: Int, green: Int, blue: Int) {
        channelRed.value = red
        channelGreen.value = green
        channelBlue.value = blue
    }

    fun update() {
        bulb.update(red, green, blue)
    }

    fun set(color: Color) {
        set(color.red, color.green, color.blue)
    }

    fun set(hexColor: String) {
        set(Color.decode(hexColor))
    }

    /**
     * Set a HSB color to this bulb
     * @param hue The floor of this number is subtracted from it to create a fraction between 0 and 1. This fractional number is then multiplied by 360 to produce the hue angle in the HSB color model.
     * @param saturation In the range 0.0..1.0
     * @param brightness In the range 0.0..1.0
     */
    fun setHSB(hue: Float, saturation: Float, brightness: Float) {
        set(Color.getHSBColor(hue, saturation, brightness))
    }

    override fun toString(): String {
        return "Bulb{R:" + channelRed + ",G:" + channelGreen + "B:" + channelBlue + "}"
    }

    // TODO wrong naming...
    private inner class ChannelController : ChannelOwner {

        override fun onChannelUpdated(newValue: Int) {
            update()
        }
    }
}
