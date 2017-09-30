package darkness.generator.scripts.uka17;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.BulbRGB;

public class Jubileum extends BaseScript {
    @Override
    public void run() {
        super.run();
        for (BulbGroup digit : digits) {
	    for (BulbRGB bulb : digit) {
		set(bulb, 255, 255, 255);
                skip(2);
	    }
	}
	// hardcoded fill last element
	set(bulb(95), 255, 255, 255);
	skip(2);
	set(bulb(85), 255, 255, 255);
	set(bulb(90), 255, 255, 255);
	skip(2);
	set(bulb(86), 255, 255, 255);
	set(bulb(91), 255, 255, 255);
	skip(2);
	set(bulb(87), 255, 255, 255);
	set(bulb(92), 255, 255, 255);
	skip(2);
	set(bulb(88), 255, 255, 255);
	set(bulb(93), 255, 255, 255);
	skip(2);
	set(bulb(89), 255, 255, 255);
	set(bulb(94), 255, 255, 255);
	skip(2);

	// flash last \0/
	skip(3);
	int repeats = 5;
	while (repeats > 0) {
	    set(I, 0, 0, 0);
	    skip(5);
	    set(I, 255, 255, 255);
	    skip(10);
	    repeats -= 1;
	}
	skip(10);
    }
}
