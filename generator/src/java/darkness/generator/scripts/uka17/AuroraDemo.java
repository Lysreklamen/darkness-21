package darkness.generator.scripts.uka17;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.BulbRGB;

import darkness.generator.api.effects.Aurora;

import java.awt.*;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.Random;


public class AuroraDemo extends BaseScript {
    @Override
    public void run() {
        super.run();
	
    Color c = new Color(57, 255, 20); // neon green

    effect(new Aurora(mergedAllBulbs, c, 10, 4, 30, 0.0f));

    }
}
