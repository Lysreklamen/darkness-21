package darkness.generator.scripts.uka13;

import darkness.generator.api.BulbRGB;

import java.awt.*;

public class DemoSubScript extends DemoBaseScript {
	@Override
	public void run() {
		super.run();
		BulbRGB previous = null;
		for (BulbRGB bulb : O) {
			set(bulb, Color.RED);
			if (previous != null) {
				set(previous, Color.BLACK);
			}
			previous = bulb;
			next();
		}
		set(previous, Color.BLACK);
	}
}
