package darkness.generator.scripts.uka15;

import darkness.generator.api.BulbRGB;

public class Sinus extends BaseScript {
	@Override
	public void run() {
		super.run();
		for (float t = 0; t < 3.14 * 40; t += 0.5) {
			for (BulbRGB bulb : allBulbs) {
				float x = bulb.getPosition()[0];
				float y = bulb.getPosition()[1];
				if ((Math.sin(x + t) + 1) / 2 * 1.5 > y) {
					set(bulb, 255, 0, 0);
				} else {
					set(bulb, 0, 0, 0);
				}
			}
			next();
		}
	}
}
