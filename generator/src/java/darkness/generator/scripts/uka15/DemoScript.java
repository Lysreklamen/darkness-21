package darkness.generator.scripts.uka15;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.BulbRGB;

import java.awt.Color;

public class DemoScript extends BaseScript {
	@Override
	public void run() {
		super.run();

		for (BulbGroup letter : letters) {
			for (BulbRGB bulb : letter) {
				set(bulb, Color.GREEN);
				next();
			}
		}

		skip(24);

		for (int i = 0; i < 4; i++) {
			merge(new DemoSubScript());
			next();
		}

		skip(96);

	}
}
