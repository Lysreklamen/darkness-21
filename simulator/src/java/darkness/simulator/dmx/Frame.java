package darkness.simulator.dmx;

import darkness.simulator.ParseException;

public class Frame {
    public static final int SIZE = 512;

    private final int[] channelValues = new int[SIZE];

    public int getChannelValue(int channel) {
        return channelValues[channel - 1];
    }

    public void setChannelValue(int channel, int value) throws ParseException {
        if (value < 0 || value > 255) {
            throw new IllegalArgumentException(String.format("Value for channel %d has value %d (expected 0-255)", channel, value));
        }
        channelValues[channel - 1] = value;
    }
}
