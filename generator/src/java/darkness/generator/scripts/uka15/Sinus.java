package darkness.generator.scripts.uka15;

import darkness.generator.api.BulbRGB;

public class Sinus extends BaseScript {
	@Override
	public void run() {
		super.run();
		final double middleBottom = 0.5;
		final double blurRadius = 0.2f;
		final double frequencyMultiplier = 1.007;
		final int iterations = 400;
		double frequency = 0.2;
		double t = 0;
		for (int i = 0; i < iterations; t += 0.03, i++, frequency *= frequencyMultiplier) {
			for (BulbRGB bulb : allBulbs) {
				final float x = bulb.getPosition()[0];
				final float y = bulb.getPosition()[1];
				final double bottom = (1 - Math.abs(((LEFT + RIGHT) / 2 - x) / (WIDTH / 2))) * middleBottom;
				final double sin = bottom + (Math.sin((x - LEFT + t) * frequency) + 1) / 2 * (LETTER_HEIGHT - bottom);
				final double red = (sin - (y - blurRadius)) / (blurRadius * 2) * 255;
				if (i == 0) {
					rgbFade(bulb, red < 0 ? 0 : (red > 255 ? 255 : (int) Math.round(red)), 0, 0, 20);
				} else {
					setCoerced(bulb, red, 0, 0);
				}
			}
			if (i == 0) {
				skip(20);
			} else {
				next();
			}
		}
		for (BulbRGB bulb : allBulbs) {
			rgbFade(bulb, 0, 0, 0, 20);
		}
	}
}
