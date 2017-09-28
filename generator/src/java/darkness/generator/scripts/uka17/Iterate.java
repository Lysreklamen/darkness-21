package darkness.generator.scripts.uka17;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.BulbRGB;

public class Iterate extends BaseScript {
	@Override
	public void run() {
		super.run();
		BulbRGB previousBulb = null;
		for (BulbGroup letter : letters) {
			for (BulbRGB bulb : letter) {
				set(bulb, 255, 255, 255);
				if (previousBulb != null) {
					set(previousBulb, 0, 0, 0);
				}
				previousBulb = bulb;
				skip(5);
			}
		}
	}
}
