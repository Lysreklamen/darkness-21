package darkness.simulator.dmx;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by janosa on 2/20/15.
 */
public class ChannelManager {
    private static ChannelManager ourInstance = new ChannelManager();

    public static ChannelManager getInstance() {
        return ourInstance;
    }

    // Key = Universe * 1000 + channel
    private HashMap<Integer, Channel> channelMap = new HashMap<Integer, Channel>();

    private ChannelManager() {
    }

    /**
     * Gets or creates a channel with the given universe and channel number
     * @param universe The number of the DMX universe this channel is connected to
     * @param channel The DMX channel number between 1 and 512 (inclusive)
     * @return The Channel object related to the given universe and channel number
     */
    public Channel getChannel(int universe, int channel) {
        if(channel < 1 || channel > 512) {
            throw new IllegalArgumentException("The channel must be between 1 and 512 (inclusive)");
        }

        int id = universe*1000 + channel;
        Channel ret = channelMap.get(id);
        if(ret == null) {
            ret = new Channel(universe, channel);
            channelMap.put(id, ret);
        }

        return ret;
    }

    /**
     * Gets or creates a channel with channel number (the universe is implicitly set to 0)
     * @param channel The DMX channel number between 1 and 512 (inclusive)
     * @return The Channel object related to the given universe and channel number
     */
    public Channel getChannel(int channel) {
        return getChannel(0, channel);
    }
}
