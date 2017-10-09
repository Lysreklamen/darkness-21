package darkness.generator.scripts.uka17;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.BulbRGB;
import darkness.generator.api.effects.Aurora;
import darkness.generator.api.effects.RGBFade;

import java.awt.*;

public class Fire extends BaseScript {

    @Override
    public void run() {
        super.run();

        BulbGroup red = group(
                0, 7,
                15, 16,

                37, 38, 39,
                45, 50,

                67, 68, 69,
                78,
                95
        );

        BulbGroup orange = group(
                1,
                17, 18, 19,

                33, 36,
                51, 49,

                63, 66,
                77, 79, 82,
                85, 86, 90
        );

        BulbGroup yellow = group(
                2, 3, 8, 10,
                20, 21,
                25,
                32, 35, 40, 42,
                46, 52, 53,
                55,
                62, 65, 70, 60,
                76, 83, 80,
                87, 91, 93
        );

        int time = 20;
        int fade = 4;

        effect(new Aurora(red, Color.RED, time, fade, red.numBulbs, 0.3f));
        effect(new Aurora(orange, Color.ORANGE, time, fade, orange.numBulbs, 0.3f));
        effect(new Aurora(yellow, Color.YELLOW, time, fade, yellow.numBulbs, 0.3f));
        skip(time * 20);
        fadeOut(20);
    }

    private void fadeOut(int frames) {
        for (BulbRGB bulb: allBulbsRGB) {
            effect(new RGBFade(bulb, Color.BLACK, frames));
        }
    }
}
