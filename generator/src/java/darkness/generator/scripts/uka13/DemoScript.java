package darkness.generator.scripts.uka13;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.BulbRGB;

import java.awt.Color;

public class DemoScript extends DemoBaseScript {
    @Override
    public void run() {
        super.run(); // Must call super if extending a custom base script
        Color[] rainbowColors = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, new Color(0, 255, 127), Color.BLUE, new Color(127, 0, 255), new Color(255, 0, 255)};

        // Make each letter fade in, with a little delay between the start of each letter and a separate color for each letter
        for (int i = 0; i < letters.length; i++) {
            rgbFade(letters[i], rainbowColors[i], 48);
            skip(12);
        }
        skip(36);

        // Turn each bulb white, one per frame
        for (BulbGroup letter : letters) {
            for (BulbRGB bulb : letter) {
                set(bulb, Color.WHITE);
                next();
            }
        }

        // Start the same sub-script three times. Note that with the current implementation, a bulb that is turned off
        // by one script cannot in the same frame be turned on by another; hence the skip.
        for (int i = 0; i < 3; i++) {
            merge(new DemoSubScript());
            skip(2);
        }

        // Enjoy the results for four seconds
        skip(96);
    }
}
