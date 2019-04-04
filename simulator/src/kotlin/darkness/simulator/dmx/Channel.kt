package darkness.simulator.dmx

/**
 * A class that represent a single grayscale DMX channel with a universe and a channel number.
 * Multiple channels can be grouped together to bulbs, giving for example an RGB bulb.
 *
 * @param universe The number of the DMX universe this channel is connected to
 * @param channel The DMX channel number between 1 and 512 (inclusive)
 */
class Channel(val universe: Int, val channel: Int) {
    var owner: ChannelOwner? = null

    var value: Int = 0
        set(value) {
            if (value < 0 || value > 255) {
                throw IllegalArgumentException("The channels value must be between 0 and 255 (inclusive)")
            }
            field = value
            owner?.onChannelUpdated(value)
        }

    init {
        if (channel < 1 || channel > 512) {
            throw IllegalArgumentException("The channel must be between 1 and 512 (inclusive)")
        }
    }

    override fun toString(): String {
        return "{U:$universe,C:$channel}"
    }
}
