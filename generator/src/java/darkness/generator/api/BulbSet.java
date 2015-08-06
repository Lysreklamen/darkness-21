package darkness.generator.api;

import java.awt.Color;

/**
 * Either an individual bulb ({@link BulbRGB}), or a set of bulbs ({@link BulbGroup}).
 */
public interface BulbSet {
	/**
	 * Set the bulb(s) to the given RGB color.
	 */
	void set(int red, int green, int blue);

	/**
	 * Set the bulb(s) to the given RGB color.
	 */
	void set(Color color);

	/**
	 * Set the bulb(s) to the given HSB color.
	 */
	void setHSB(float hue, float saturation, float brightness);

	/**
	 * If this is a single bulb, return the red component of that bulb.
	 * Otherwise, return the red component of the first bulb in the group.
	 */
	int getRed();

	/**
	 * If this is a single bulb, return the green component of that bulb.
	 * Otherwise, return the green component of the first bulb in the group.
	 */
	int getGreen();

	/**
	 * If this is a single bulb, return the blue component of that bulb.
	 * Otherwise, return the blue component of the first bulb in the group.
	 */
	int getBlue();

	/**
	 * If this is a single bulb, return the RGB color of that bulb.
	 * Otherwise, return the RGB color of the first bulb in the group.
	 */
	Color getColor();
}
