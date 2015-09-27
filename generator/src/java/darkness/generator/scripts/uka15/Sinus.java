package darkness.generator.scripts.uka15;

import darkness.generator.api.BulbRGB;

public class Sinus extends BaseScript {
	@Override
	public void run() {
		super.run();
		final double letterHeight = 1.5;
		final double middleBottom = 0.5;
		final double left = 1.73;
		final double right = 8.62;
		final double width = right - left;
		final double blurRadius = 0.2f;
		final double frequencyMultiplier = 1.01;
		final int iterations = 600;
		double frequency = 0.1;
		double t = 0;
		for (int i = 0; i < iterations; t += 0.03, i++, frequency *= frequencyMultiplier) {
			for (BulbRGB bulb : allBulbs) {
				final float x = bulb.getPosition()[0];
				final float y = bulb.getPosition()[1];
				final double bottom = (1 - Math.abs(((left + right) / 2 - x) / (width / 2))) * middleBottom;
				final double sin = bottom + (Math.sin((x - left + t) * frequency) + 1) / 2 * (letterHeight - bottom);
				setCoerced(bulb, (sin - (y - blurRadius)) / (blurRadius * 2) * 255, 0, 0);
			}
			next();
		}
	}
}
