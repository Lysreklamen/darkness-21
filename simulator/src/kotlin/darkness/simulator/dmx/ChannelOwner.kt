package darkness.simulator.dmx

interface ChannelOwner {
    fun onChannelUpdated(newValue: Int)
}
