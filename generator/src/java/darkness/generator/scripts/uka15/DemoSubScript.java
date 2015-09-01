package darkness.generator.scripts.uka15;

import darkness.generator.api.BulbRGB;

import java.awt.*;

public class DemoSubScript extends DemoBaseScript {
	@Override
	public void run() {
		super.run();
		BulbRGB previous = null;
		for (BulbRGB bulb : O) {
			setRGB(bulb, Color.RED);
			if (previous != null) {
				setRGB(previous, Color.BLACK);
			}
			previous = bulb;
			next();
		}
		setRGB(previous, Color.BLACK);
	}
}
