package darkness.simulator.dmx;

import darkness.simulator.ParseException;

public class Frame {
    public static final int SIZE = 512;
	public static final int TRANSPARENT = 256;

    private final int[] channelValues = new int[SIZE];
	private final boolean supportsTransparency;

	public Frame(boolean supportsTransparency) {
		this.supportsTransparency = supportsTransparency;
	}

    public int getChannelValue(int channel) {
        return channelValues[channel - 1];
    }

    public void setChannelValue(int channel, int value) throws ParseException {
        if (value < 0 || !(value <= 255 || value == TRANSPARENT)) {
            throw new IllegalArgumentException(String.format("Value for channel %d has value %d (expected 0-255%s)",
					channel, value, supportsTransparency ? " or " + TRANSPARENT : ""));
        }
        channelValues[channel - 1] = value;
    }
}
