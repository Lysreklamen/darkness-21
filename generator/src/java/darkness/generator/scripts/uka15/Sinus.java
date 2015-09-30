package darkness.generator.scripts.uka15;

import darkness.generator.api.BulbRGB;

public class Sinus extends BaseScript {
	@Override
	public void run() {
		super.run();
		final double middleBottom = 0.5;
		final double blurRadius = 0.2f;
		final double frequencyMultiplier = 1.01;
		final int iterations = 600;
		double frequency = 0.1;
		double t = 0;
		for (int i = 0; i < iterations; t += 0.03, i++, frequency *= frequencyMultiplier) {
			for (BulbRGB bulb : allBulbs) {
				final float x = bulb.getPosition()[0];
				final float y = bulb.getPosition()[1];
				final double bottom = (1 - Math.abs(((LEFT + RIGHT) / 2 - x) / (WIDTH / 2))) * middleBottom;
				final double sin = bottom + (Math.sin((x - LEFT + t) * frequency) + 1) / 2 * (LETTER_HEIGHT - bottom);
				setCoerced(bulb, (sin - (y - blurRadius)) / (blurRadius * 2) * 255, 0, 0);
			}
			next();
		}
	}
}
