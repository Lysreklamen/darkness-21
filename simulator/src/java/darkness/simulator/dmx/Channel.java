package darkness.simulator.dmx;

/**
 * A class that represent a single grayscale DMX channel.
 * Multiple channels can be grouped together to bulbs, giving for example an RGB bulb
 */
public class Channel {
    private final int universe;
    private final int channel;

    private int value;

    private ChannelOwner owner;

    /**
     * Constructs a channel object with universe and channel number
     * @param universe The number of the DMX universe this channel is connected to
     * @param channel The DMX channel number between 1 and 512 (inclusive)
     */
    Channel(int universe, int channel) {
        this.universe = universe;

        if(channel < 1 || channel > 512) {
            throw new IllegalArgumentException("The channel must be between 1 and 512 (inclusive)");
        }

        this.channel = channel;

        value = 0;
    }

    /**
     * A constructor that only gives the channel number in the DMX universe.
     * The universe is implicitly set to 0.
     * @param channel The DMX channel number between 1 and 512 (inclusive)
     */
    Channel(int channel) {
        this(0, channel);
    }

    public int getUniverse() {
        return universe;
    }

    public int getChannel() {
        return channel;
    }

    public int getValue() {
        return value;
    }

    /**
     * Sets a value to the channel
     * @param value a value between 0 and 255 inclusive.
     */
    public void setValue(int value) {
        if(value < 0 || value > 255) {
            throw new IllegalArgumentException("The channels value must be between 0 and 255 (inclusive)");
        }
        this.value = value;

        if(owner != null) {
            owner.onChannelUpdated(value);
        }
    }

    public ChannelOwner getOwner() {
        return owner;
    }

    public void setOwner(ChannelOwner owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "{U:"+universe+",C:"+channel+"}";
    }
}
