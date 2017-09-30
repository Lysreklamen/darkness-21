package darkness.generator.scripts.uka17;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.BulbRGB;

import darkness.generator.api.effects.WeightedStrobe;

import java.awt.*;

public class Jubileum extends BaseScript {
    @Override
    public void run() {
        super.run();

        Color c = new Color(255, 255, 255);
        int t = 3;
        int fade_ext = 17;

        for (BulbGroup digit : digits) {
	        for (BulbRGB bulb : digit) {
		        rgbFade(bulb, c, t + fade_ext);
                skip(t);
	        }
            skip(fade_ext);
	    }
	    // hardcoded fill last element
	    rgbFade(bulb(95), c, t + fade_ext);
	    skip(t);
        for (int i = 0; i < 5; i++) {
	        rgbFade(bulb(85+i), c, t + fade_ext);
	        rgbFade(bulb(90+i), c, t + fade_ext);
	        skip(t);
        }
	    skip(fade_ext);
	    // flash last \0/
        effect(new WeightedStrobe(I, 3*t, t, 3));
	    skip(3*t);
    }
}
