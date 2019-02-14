package darkness.simulator.dmx

import darkness.simulator.ParseException

class Frame(private val supportsTransparency: Boolean) {
    private val channelValues = IntArray(size)

    fun getChannelValue(channel: Int): Int {
        return channelValues[channel - 1]
    }

    @Throws(ParseException::class)
    fun setChannelValue(channel: Int, value: Int) {
        if (value < 0 || !(value <= 255 || value == transparent)) {
            throw IllegalArgumentException(String.format("Value for channel %d has value %d (expected 0-255%s)",
                    channel, value, if (supportsTransparency) " or $transparent" else ""))
        }
        channelValues[channel - 1] = value
    }

    companion object {
        const val size = 512
        const val transparent = 256
    }
}
