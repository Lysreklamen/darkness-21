package darkness.generator.api.effects;

import darkness.generator.api.BulbSet;
import darkness.generator.api.BulbGroup;
import darkness.generator.api.BulbRGB;

import java.awt.Color;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.Random;

public class Aurora extends EffectBase {
    private final BulbGroup bulbGroup;
    private final Color color;
    private final int time; // in seconds
    private final int fade; // in frames
    private final int nChangeBulbs; // number of bulbs that possibly change between blocks
    private final float minBrightness; 
    private final Random rnd;

    public Aurora(BulbGroup bulbGroup, Color color, int time, int fade, int nChangeBulbs, float minBrightness) {
        this.bulbGroup = bulbGroup;
        this.color = color;
	this.time = time;
	this.fade = fade;
	this.nChangeBulbs = nChangeBulbs;
	this.minBrightness = minBrightness;
	this.rnd = new Random(1337);
    }

    @Override
    public void run() {
	    float[] hsb_vals = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);

	    int nRepeats = time * 20 / fade;

	    for (int j = 0; j < nRepeats; j++) {
		    for (int i = 0; i < nChangeBulbs; i++) {
			    int nextBulbIdx = rnd.nextInt(bulbGroup.numBulbs);
			    BulbRGB nextBulb = bulbGroup.getBulb(nextBulbIdx);
			    float nextBrightness = rnd.nextFloat();
			    while (nextBrightness <= minBrightness) {
				    nextBrightness = rnd.nextFloat();
			    }
			    Color c = Color.getHSBColor(hsb_vals[0], hsb_vals[1], nextBrightness);
			    rgbFade(nextBulb, c, fade);
		    }
		    skip(fade);
	    }
    }

    @Override
    public String toString() {
        return "Effect Aurora";
    }
}
