package darkness.generator.api.effects;

import darkness.generator.api.BulbSet;

import java.awt.Color;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.Random;

public class Aurora extends EffectBase {
    private final Color color;
    private final int time; // in seconds
    private final int fade; // in frames
    private final int nChangeBulbs; // number of bulbs that possibly change between blocks
    private final float minBrightness; 
    private final int maxBulbIndex;
    private final Set<Integer> notAssignedBulbs;
    private final Random rnd;

    public Aurora(Color color, int time, int fade, int nChangeBulbs, float minBrightness) {
        this.color = color;
	this.time = time;
	this.fade = fade;
	this.nChangeBulbs = nChangeBulbs;
	this.minBrightness = minBrightness;
	this.maxBulbIndex = 95; // XXX: UKA-17
	this.notAssignedBulbs = new HashSet<>(Arrays.asList(12, 13, 14, 22, 23, 24, 27, 28, 29, 44, 57, 58, 59, 74)); // XXX: UKA-17
	this.rnd = new Random(1337);
    }

    @Override
    public void run() {
	    float[] hsb_vals = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);

	    int nRepeats = time * 20 / fade;

	    for (int j = 0; j < nRepeats; j++) {
		    for (int i = 0; i < nChangeBulbs; i++) {
			    int nextBulb = rnd.nextInt(maxBulbIndex + 1);
			    while (notAssignedBulbs.contains(nextBulb)) { // java ass
				    nextBulb = rnd.nextInt(maxBulbIndex + 1);
			    }
			    float nextBrightness = rnd.nextFloat();
			    while (nextBrightness <= minBrightness) {
				    nextBrightness = rnd.nextFloat();
			    }
			    Color c = Color.getHSBColor(hsb_vals[0], hsb_vals[1], nextBrightness);
			    rgbFade(bulb(nextBulb), c, fade);
		    }
		    skip(fade);
	    }
    }

    @Override
    public String toString() {
        return "Effect Aurora";
    }
}
