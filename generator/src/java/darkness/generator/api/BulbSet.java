package darkness.generator.api;

import java.awt.Color;

/**
 * Either an individual bulb ({@link BulbRGB}), or a set of bulbs ({@link BulbGroup}).
 */
public interface BulbSet {
	/**
	 * Set the bulb(s) to the given RGB color.
	 */
	void set(int red, int green, int blue, ScriptBase setter);

	/**
	 * Set the bulb(s) to the given RGB color.
	 */
	void set(Color color, ScriptBase setter);

	/**
	 * Set the bulb(s) to the given HSB color.
	 */
	void setHSB(float hue, float saturation, float brightness, ScriptBase setter);

	/**
	 * Set the bulb(s) to the given RGB color. Out-of-range values will be coerced to [0-255].
	 */
	default void setCoerced(double red, double green, double blue, ScriptBase setter) {
		set(
				(int) Math.max(0, Math.min(255, Math.round(red))),
				(int) Math.max(0, Math.min(255, Math.round(green))),
				(int) Math.max(0, Math.min(255, Math.round(blue))),
				setter);
	}

	/**
	 * Relinquish control over the bulb(s). If no other script(s) in the same frame set the bulb(s),
	 * it/they will turn black; otherwise, the other script(s) will win.
	 */
	void relinquish(ScriptBase setter);

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

	/**
	 * Gets the position (or average of positions of multiple bulbs).
	 * @return a 3 element array with x, y and z position. (Should be meters from the bottom left corner of the sign). z is normally set to 0.0f
	 */
	float[] getPosition();
}
