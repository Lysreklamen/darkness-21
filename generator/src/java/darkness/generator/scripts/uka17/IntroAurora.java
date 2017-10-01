package darkness.generator.scripts.uka17;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.BulbRGB;

import darkness.generator.api.effects.Aurora;

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

	set(C, Color.BLACK);
	set(F, Color.BLACK);
	set(A, green);
	set(B, green);
	set(D, green);
	set(E, green);
	set(G, green);
	set(H, green);
	set(I, green);
	skip(5*20);



    }
}
