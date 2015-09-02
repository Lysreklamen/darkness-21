package darkness.generator.scripts.uka15;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.BulbRGB;

import java.awt.Color;

public class DemoScriptUka15 extends BaseScriptUka15 {
	@Override
	public void run() {
		super.run();

		for (BulbGroup letter : letters) {
			for (BulbRGB bulb : letter) {
				setRGB(bulb, Color.WHITE);
				next();
			}
		}

		skip(96);
	}
}
