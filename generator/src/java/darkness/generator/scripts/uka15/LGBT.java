package darkness.generator.scripts.uka15;

import darkness.generator.api.BulbRGB;

import java.awt.*;

public class LGBT extends BaseScript {
	@Override
	public void run() {
		super.run();
		final int fallFrames = 30;
		for (int i = 0; i < fallFrames; i++) {
			for (BulbRGB bulb : allBulbs) {
				final float y = bulb.getPosition()[1];
				if (y >= LETTER_HEIGHT * i / fallFrames && y < LETTER_HEIGHT * (i + 1) / fallFrames) {
					rgbFade(bulb, Color.getHSBColor(getHue(bulb), 1, 1), 30);
				}
			}
			next();
		}
		skip(40);

		for (int i = 0; i < 2; i++) {
			for (BulbRGB bulb : allBulbs) {
				float[] hsb = new float[3];
				Color.RGBtoHSB(bulb.getRed(), bulb.getGreen(), bulb.getBlue(), hsb);
				hsbFade(bulb, hsb[0] + 1, 1, 1, 40);
			}
			skip(40);
		}

		for (int i = 0; i < 4; i++) {
			for (BulbRGB bulb : allBulbs) {
				rgbFade(bulb, Color.getHSBColor(getHue(bulb), 1, 0.5f), 10);
			}
			skip(10);
			for (BulbRGB bulb : allBulbs) {
				rgbFade(bulb, Color.getHSBColor(getHue(bulb), 1, 1), 10);
			}
			skip(10);
		}
		skip(40);
		for (BulbRGB bulb : allBulbs) {
			rgbFade(bulb, 0, 0, 0, 20);
		}
		skip(20);
	}

	private float getHue(BulbRGB bulb) {
		final float y = bulb.getPosition()[1];
		return (float) ((1 - y / LETTER_HEIGHT) * 0.8);
	}
}
