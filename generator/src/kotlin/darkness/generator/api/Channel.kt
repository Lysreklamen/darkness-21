package darkness.generator.api

/**
 * A class that represents a single grayscale DMX channel.
 * Multiple channels can be grouped together to bulbs, giving for example an RGB bulb.
 *
 * Constructor creates a channel object with universe and channel number.
 * @param universe The number of the DMX universe this channel is connected to
 * @param channel The DMX channel number between 1 and 512 (inclusive)
 */
class Channel(val universe: Int, val channel: Int) {
    var value: Int = 0
        private set
    private var lastSetterInCurrentFrame: ScriptBase? = null

    init {
        if (channel < 1 || channel > 512) {
            throw IllegalArgumentException("The channel must be between 1 and 512 (inclusive)")
        }
    }

    /**
     * A constructor that only gives the channel number in the DMX universe.
     * The universe is implicitly set to 0.
     * @param channel The DMX channel number between 1 and 512 (inclusive)
     */
    internal constructor(channel: Int) : this(0, channel) {}

    /**
     * Sets a value to the channel
     * @param value A value between 0 and 255 inclusive (to indicate a DMX channel value),
     * or 256 (to indicate transparency; only useful in overlays),
     * or 257 (to indicate relinquishment of the channel, which will set the channel to 0
     * unless it has already been actively set to something else in the same frame).
     * If the channel has been set in the same frame, the setter's priorities are used to determine a winner;
     * with equal priorities, the original value wins.
     */
    fun setValue(value: Int, setter: ScriptBase?) {
        if (value < 0 || value > relinquish) {
            throw IllegalArgumentException("The channels value must be between 0 and 255 (inclusive), or 256 for transparency, or 257 for relinquishing")
        }
        if (setter == null) {
            throw IllegalArgumentException("setter must be specified")
        }
        if (value == relinquish) {
            if (lastSetterInCurrentFrame == null) {
                this.value = 0
            }
        } else {
            if (lastSetterInCurrentFrame == null || lastSetterInCurrentFrame!!.priority <= setter.priority) {
                this.value = value
                lastSetterInCurrentFrame = setter
            }
        }
    }

    fun nextFrame() {
        lastSetterInCurrentFrame = null
    }

    override fun toString() = "{U:$universe,C:$channel}"

    companion object {
        val relinquish = 257
    }
}
