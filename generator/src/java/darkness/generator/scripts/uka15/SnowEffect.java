package darkness.generator.scripts.uka15;

import darkness.generator.api.BulbRGB;
import darkness.generator.api.effects.EffectBase;

public class SnowEffect extends EffectBase {
	private final BulbRGB[] allBulbs;
	private final double xTarget;
	protected final double LETTER_HEIGHT = 1.5;
	protected final double LEFT = 1.73;
	protected final double RIGHT = 8.62;
	protected final double WIDTH = RIGHT - LEFT;

	public SnowEffect(BulbRGB[] allBulbs, double xTarget) {
		this.allBulbs = allBulbs;
		this.xTarget = xTarget;
	}

	@Override
	public void run() {
		double radius = LETTER_HEIGHT / 5;
		double extraDepth = radius * 1.5;
		int duration = 40;
		for (int i = 0; i < duration; i++) {
			double yTarget = (1 - Math.pow(1 - (duration - (double) i) / duration * 0.75, 2)) * (LETTER_HEIGHT + extraDepth) - extraDepth;
			for (BulbRGB bulb : allBulbs) {
				double x = bulb.getPosition()[0];
				double y = bulb.getPosition()[1];
				double c = 255 * (1 - Math.sqrt(Math.pow(xTarget - x, 2) + Math.pow(yTarget - y, 2)) / radius);
				if (c <= 10) {
					relinquish(bulb);
				} else {
					setCoerced(bulb, c, c, c);
				}
			}
			next();
		}
		skip(40);
	}

	@Override public String toString() {
		return "SnowEffect";
	}
}
