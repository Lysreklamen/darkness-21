package darkness.generator.api;

/**
 * A class that represents a single grayscale DMX channel.
 * Multiple channels can be grouped together to bulbs, giving for example an RGB bulb.
 */
public class Channel {
    private final int universe;
    private final int channel;

    private int value;
    private ScriptBase lastSetterInCurrentFrame;

    public static final int RELINQUISH = 257;

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
     * @param value A value between 0 and 255 inclusive (to indicate a DMX channel value),
     *              or 256 (to indicate transparency; only useful in overlays),
     *              or 257 (to indicate relinquishment of the channel, which will set the channel to 0
     *              unless it has already been actively set to something else in the same frame).
     *              If the channel has been set in the same frame, the setter's priorities are used to determine a winner;
     *              with equal priorities, the original value wins.
     */
    public void setValue(int value, ScriptBase setter) {
        if (value < 0 || value > RELINQUISH) {
            throw new IllegalArgumentException("The channels value must be between 0 and 255 (inclusive), or 256 for transparency, or 257 for relinquishing");
        }
        if (setter == null) {
            throw new IllegalArgumentException("setter must be specified");
        }
        if (value == RELINQUISH) {
            if (lastSetterInCurrentFrame == null) {
                this.value = 0;
            }
        } else {
            if (lastSetterInCurrentFrame == null || lastSetterInCurrentFrame.getPriority() < setter.getPriority()) {
                this.value = value;
                lastSetterInCurrentFrame = setter;
            }
        }
    }

    public void nextFrame() {
        lastSetterInCurrentFrame = null;
    }

    @Override
    public String toString() {
        return "{U:"+universe+",C:"+channel+"}";
    }
}
