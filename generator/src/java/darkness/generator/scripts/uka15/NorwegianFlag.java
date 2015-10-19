package darkness.generator.scripts.uka15;

import darkness.generator.api.BulbRGB;

import java.awt.Color;

public class NorwegianFlag extends BaseScript {
	@Override
	public void run() {
		super.run();
		for (double y = LETTER_HEIGHT; y >= -0.05; y -= 0.05) {
			for (int i = 0; i < 9; i++) {
				for (BulbRGB bulb : columns[i]) {
					if (bulb.getPosition()[1] >= y) {
						set(bulb, 0xED, 0x29, 0x39);
					}
				}
			}
			for (BulbRGB bulb : columns[8]) {
				if (bulb.getPosition()[1] >= y) {
					set(bulb, Color.WHITE);
				}
			}
			for (int i = 9; i < 11; i++) {
				for (BulbRGB bulb : columns[i]) {
					if (bulb.getPosition()[1] >= y) {
						set(bulb, 0x00, 0x26 + 0x15, 0x64 + 0x40); // Needs to be lighter than the actual color (#002664) because the blue light is harder to see
					}
				}
			}
			for (BulbRGB bulb : columns[11]) {
				if (bulb.getPosition()[1] >= y) {
					set(bulb, Color.WHITE);
				}
			}
			for (int i = 12; i < columns.length; i++) {
				for (BulbRGB bulb : columns[i]) {
					if (bulb.getPosition()[1] >= y) {
						set(bulb, 0xED, 0x29, 0x39);
					}
				}
			}
			next();
		}
		skip(80);
		for (BulbRGB bulb : allBulbs) {
			rgbFade(bulb, Color.BLACK, 40);
		}
		skip(20);
	}
}
