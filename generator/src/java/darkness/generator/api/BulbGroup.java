package darkness.generator.api;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

public class BulbGroup implements BulbSet {
	private final List<BulbRGB> bulbs;

	public BulbGroup(BulbRGB... bulbs) {
		if (bulbs == null || bulbs.length == 0) {
			throw new IllegalArgumentException("bulbs must be non-null and contain elements");
		}
		this.bulbs = Arrays.asList(bulbs);
	}

	@Override
	public void set(int red, int green, int blue) {
		for (BulbRGB bulb : bulbs) {
			bulb.set(red, green, blue);
		}
	}

	@Override
	public void set(Color color) {
		for (BulbRGB bulb : bulbs) {
			bulb.set(color);
		}
	}

	@Override
	public void setHSB(float hue, float saturation, float brightness) {
		for (BulbRGB bulb : bulbs) {
			bulb.setHSB(hue, saturation, brightness);
		}
	}

	@Override
	public int getRed() {
		return bulbs.get(0).getRed();
	}

	@Override
	public int getGreen() {
		return bulbs.get(0).getGreen();
	}

	@Override
	public int getBlue() {
		return bulbs.get(0).getBlue();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("BulbSet{");
		for (BulbRGB bulb : bulbs) {
			sb.append(bulb);
			sb.append(',');
		}
		sb.setCharAt(sb.length() - 1, '}'); // Overwrite trailing comma
		return sb.toString();
	}

	@Override
	public Color getColor() {
		return bulbs.get(0).getColor();
	}
}
