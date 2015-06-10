package darkness.generator.api;

import java.awt.Color;

public interface BulbSet {
	void set(int red, int green, int blue);
	void set(Color color);
	void setHSB(float hue, float saturation, float brightness);
	int getRed();
	int getGreen();
	int getBlue();
	Color getColor();
}
