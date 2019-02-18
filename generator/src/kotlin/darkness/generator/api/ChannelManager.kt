package darkness.generator.api

import darkness.generator.output.BaseOutput

import java.io.BufferedWriter

object ChannelManager {
    // Key = Universe * 1000 + channel
    private val channelMap = mutableMapOf<Int, Channel>()

    /**
     * Gets or creates a channel with the given universe and channel number
     * @param universe The number of the DMX universe this channel is connected to
     * @param channel The DMX channel number between 1 and 512 (inclusive)
     * @return The Channel object related to the given universe and channel number
     */
    fun getChannel(universe: Int, channel: Int): Channel {
        if (channel < 1 || channel > 512) {
            throw IllegalArgumentException("The channel must be between 1 and 512 (inclusive)")
        }

        return channelMap.computeIfAbsent(universe * 1000 + channel) { Channel(universe, channel) }
    }

    /**
     * Gets or creates a channel with channel number (the universe is implicitly set to 0)
     * @param channel The DMX channel number between 1 and 512 (inclusive)
     * @return The Channel object related to the given universe and channel number
     */
    fun getChannel(channel: Int): Channel {
        return getChannel(0, channel)
    }

    fun nextFrame() {
        for (channel in channelMap.values) {
            channel.nextFrame()
        }
    }

    fun dumpChannels(output: BaseOutput) {
        for (channel in channelMap.values) {
            output.writeChannelValue(channel)
        }
    }

    fun dumpChannels(writer: BufferedWriter) {
        // Only dump universe 0 for now
        // TODO implement a format that supports more universes
        val channelValues = MutableList(512) { 0 }
        var highestChannelNumberFound = 0
        for (channel in channelMap.values) {
            if (channel.channel > highestChannelNumberFound) {
                highestChannelNumberFound = channel.channel
            }

            channelValues[channel.channel - 1] = channel.value
        }

        for (i in 0 until highestChannelNumberFound) {
            writer.write(Integer.toString(channelValues[i]))
            if (i != highestChannelNumberFound - 1) {
                writer.write(" ")
            }
        }
    }
}
