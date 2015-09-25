package darkness.generator.scripts.uka15;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.BulbRGB;

import java.util.LinkedList;
import java.util.Random;

public class DefaultFrame extends BaseScript {
	@Override
	public void run() {
		super.run();
		Random random = new Random(1);

		BulbGroup[] fadeInOrder = {C, F, A, D, G, E, B};


		for (int idx = 0; idx < fadeInOrder.length; idx++) {
			BulbGroup letter = fadeInOrder[idx];
			// Get a color for our letter
			float colorAngle = ((float)idx)/((float)fadeInOrder.length);

			setHSB(letter, colorAngle, 1.0f, 0.8f);

		}
		next();
	}
}
