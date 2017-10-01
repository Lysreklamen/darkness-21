package darkness.generator.scripts.uka17;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.BulbRGB;

import darkness.generator.api.effects.WeightedStrobe;

import java.awt.*;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.Random;


public class Aurora extends BaseScript {
    @Override
    public void run() {
        super.run();
	
	Set<Integer> notAssignedBulbs = new HashSet<>(Arrays.asList(12, 13, 14, 22, 23, 24, 27, 28, 29, 44, 57, 58, 59, 74));

	Random rnd = new Random(1337);
	float[] hsb_vals = Color.RGBtoHSB(57, 255, 20, null); // neon green
	for (int j = 0; j < 1000; j++) {
		for (int i = 0; i < 30; i++) {
			int nextBulb = rnd.nextInt(96);
			while (notAssignedBulbs.contains(nextBulb)) { // java ass
				nextBulb = rnd.nextInt(96);
			}
			Color c = Color.getHSBColor(hsb_vals[0], hsb_vals[1], rnd.nextFloat());
			set(bulb(nextBulb), c);
		}
		skip(2);
	}
	//fadeHSB(bulb10(10), c_hsb[0], c_hsb[1], 0, 100);
    }
}
