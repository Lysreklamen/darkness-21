package darkness.generator.scripts.uka17;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.BulbRGB;
import darkness.generator.api.effects.Aurora;
import darkness.generator.api.effects.RGBFade;

import java.awt.*;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;

public class Genesis extends BaseScript {

    private Random random = new Random(1337);
    private Color goldenrod = new Color(218, 165, 32);

    @Override
    public void run() {
        super.run();

        BulbGroup[] nonDigits = {
                B, C, E, F, H, I,
                group(7, 8, 9, 10, 11),
                group(34, 35, 36, 37),
                group(64, 65, 66, 67)
        };

        glitter(mergedAllBulbs, goldenrod);
        glitter(mergeBulbGroups(digits), goldenrod);
        effect(new RGBFade(mergeBulbGroups(nonDigits), Color.BLACK, 5 * 20));
        rgbFade(mergeBulbGroups(nonDigits), Color.BLACK, 5 * 20);
    }

    private void glitter(BulbGroup bulbGroup, Color baseColor) {
        int time = 10;
        int nChangeBulbs = 30;
        int fade = 4;
        int loops = time * 20 / fade;
        int minBrightness = 1;
        float[] hsbValues = Color.RGBtoHSB(
                baseColor.getRed(),
                baseColor.getGreen(),
                baseColor.getBlue(),
                null
        );

        int index, seed;
        float brightness;
        BulbRGB bulb;
        Color color;

        while (loops > 0) {
            for (int i = 0; i < nChangeBulbs; i++) {
                index = random.nextInt(bulbGroup.numBulbs);
                bulb = bulbGroup.getBulb(index);
                brightness = (float) (100 - random.nextInt(100 - minBrightness)) / 100;
                color = Color.getHSBColor(hsbValues[0], hsbValues[1], brightness);
                rgbFade(bulb, color, fade);
            }
            if (loops % 2 == 1) {
                minBrightness++;
            }
            skip(fade);
            loops--;
        }
    }
}
