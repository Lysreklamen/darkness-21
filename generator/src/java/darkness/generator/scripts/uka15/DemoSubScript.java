package darkness.generator.scripts.uka15;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.BulbRGB;

public class DemoSubScript extends BaseScript {
	@Override
	public void run() {
		super.run();
		BulbRGB previous = null;
		for (BulbGroup letter : letters) {
			for (BulbRGB bulb : letter) {
				if (previous != null) {
					relinquish(previous);
				}
				set(bulb, 255, 0, 0);
				previous = bulb;
				next();
			}
		}
		relinquish(previous);
	}
}
