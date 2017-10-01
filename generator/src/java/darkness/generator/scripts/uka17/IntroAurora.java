package darkness.generator.scripts.uka17;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.BulbRGB;

import darkness.generator.api.effects.Aurora;
import darkness.generator.api.effects.WeightedStrobe;

import java.awt.*;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.Random;


public class IntroAurora extends BaseScript {
    @Override
    public void run() {
        super.run();
	
    Color green = new Color(57, 255, 20); // neon green
	Color warmWhite = new Color(218,165,32);

    int longWaitTime = 1; // TODO: increase to number of seconds

    effect(new Aurora(mergedAllBulbs, warmWhite, 1, 4, 30, 0.0f));
    skip(1*20);
    
    effect(new Aurora(mergedAllBulbs, warmWhite, 20, 4, 30, 0.0f));

    skip(5*20);
    effect(new Aurora(A, green, 15, 4, 30, 0.5f));
    effect(new Aurora(B, green, 15, 4, 30, 0.5f));

    skip(5*20);
    effect(new Aurora(D, green, 10, 4, 30, 0.5f));
    effect(new Aurora(E, green, 10, 4, 30, 0.5f));

    skip(5*20);
    effect(new Aurora(G, green, 5, 4, 30, 0.5f));
    effect(new Aurora(H, green, 5, 4, 30, 0.5f));
    effect(new Aurora(I, green, 5, 4, 30, 0.5f));
    skip(5*20+1);

	set(C, 0,0,0);
	set(F, 0,0,0);
	set(A, green);
	set(B, green);
	set(D, green);
	set(E, green);
	set(G, green);
	set(H, green);
	set(I, green);
	skip(1*20);

	effect(new WeightedStrobe(A, 5, 2, 3));
	effect(new WeightedStrobe(B, 5, 2, 3));
	effect(new WeightedStrobe(D, 5, 2, 3));
	effect(new WeightedStrobe(E, 5, 2, 3));
	effect(new WeightedStrobe(G, 5, 2, 3));
	effect(new WeightedStrobe(H, 5, 2, 3));
	effect(new WeightedStrobe(I, 5, 2, 3));
	skip(1*20);
	rgbFade(A, Color.BLACK, 10);
	rgbFade(B, Color.BLACK, 10);
	rgbFade(D, Color.BLACK, 10);
	rgbFade(E, Color.BLACK, 10);
	rgbFade(G, Color.BLACK, 10);
	rgbFade(H, Color.BLACK, 10);
	rgbFade(I, Color.BLACK, 10);

        int t = 3;
        int fade_ext = 17;

        for (BulbGroup digit : digits) {
	        for (BulbRGB bulb : digit) {
		        rgbFade(bulb, warmWhite, t + fade_ext);
                skip(t);
	        }
            skip(fade_ext);
	    }
	    // hardcoded fill last element
	    rgbFade(bulb(95), warmWhite, t + fade_ext);
	    skip(t);
        for (int i = 0; i < 5; i++) {
	        rgbFade(bulb(85+i), warmWhite, t + fade_ext);
	        rgbFade(bulb(90+i), warmWhite, t + fade_ext);
	        skip(t);
        }
	    skip(fade_ext);
	    // flash last \0/
        effect(new WeightedStrobe(I, 3*t, t, 3));
	    skip(3*t);
    }
}
