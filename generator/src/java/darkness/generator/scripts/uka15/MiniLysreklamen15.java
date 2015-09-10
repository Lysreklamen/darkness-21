package darkness.generator.scripts.uka15;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.BulbRGB;

import java.awt.*;

public class MiniLysreklamen15 extends BaseScriptUka15 {
	@Override
	public void run() {
		super.run();

		// Turn each bulb on one by one
		for (BulbGroup letter : letters) {
			for (BulbRGB bulb : letter) {
				setRGB(bulb, Color.WHITE);
				next();
			}
		}

		skip(16);

		// Circus sign
		{
			int i = 0;

			// Fade each bulb to a semi-random colour
			for( int j = 0; j < 32; ++j) {
				for (BulbGroup letter : letters) {
					for (BulbRGB bulb : letter) {
						float col[] = {i * 0.15f % 1.0f, 0.4f, 0.8f};
						//hsbFade(bulb, col, 16);
						setHSB(bulb, col[0], col[1], col[2]);
						i++;
					}
				}
				next();
			}
		}

		// Horizontal rainbow
		int numColors = columns.length;
		Color colors[] = new Color[numColors];

		for (int i = 0; i < columns.length; ++i) {
			colors[i] = Color.getHSBColor((1.0f/numColors)*i, 0.4f, 0.8f);
		}

		for (int i = 0; i < 64; ++i) {
			int j = 0;
			for (BulbGroup col : columns) {
				for (BulbRGB bulb : col) {
					setRGB(bulb, colors[(i + j) % numColors]);
				}
				++j;
			}
			next();
		}

		skip(32);
	}
}
