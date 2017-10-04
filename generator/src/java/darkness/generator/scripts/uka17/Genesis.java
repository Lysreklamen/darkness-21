package darkness.generator.scripts.uka17;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.BulbRGB;
import darkness.generator.api.effects.RGBFade;

import java.awt.*;
import java.util.*;

/**
 * Opening sequence for Lysreklamen UKA 17.
 *
 * Runs a glittering effect on all bulbs for n seconds, before revealing the
 * 100th anniversary message.
 *
 * Uses a slightly modified version of the
 * {@link darkness.generator.api.effects.Aurora} effect.
 */
public class Genesis extends BaseScript {

    private final Random RANDOM = new Random(1337);
    private final Color GOLDENROD = new Color(218, 165, 32);

    @Override
    public void run() {
        super.run();

        BulbGroup everything = mergeBulbGroups(letters);
        BulbGroup onlyDigits = mergeBulbGroups(digits);
        BulbGroup nonDigits = mergeBulbGroups(
                B, C, E, F, H, I,
                group(7, 8, 9, 10, 11),
                group(34, 35, 36, 37),
                group(64, 65, 66, 67)
        );

        final int FRAMES_PER_SECOND = 20;
        int glitterFrames = FRAMES_PER_SECOND * 20; // TODO change to actual frame number
        int fadeFrames = FRAMES_PER_SECOND * 5;

        glitter(everything, GOLDENROD, glitterFrames, 0.1f, 0.8f);

        // Make sure that each bulb fade from its current color
        for (BulbRGB bulb: nonDigits) {
            effect(new RGBFade(bulb, Color.BLACK, fadeFrames));
        }
        glitter(onlyDigits, GOLDENROD, glitterFrames / 2, 0.1f, 0.8f);

        // Make sure that each bulb fade from its current color
        for (BulbRGB bulb: onlyDigits) {
            effect(new RGBFade(bulb, GOLDENROD, fadeFrames / 2));
        }
        skip(fadeFrames);
        glitter(onlyDigits, GOLDENROD, glitterFrames / 2, 0.2f, 1.0f);
    }

    private float randomFloat(float left, float right) {
        return left + RANDOM.nextFloat() * (right - left);
    }

    private void glitter(BulbGroup bulbGroups, Color color, int frames, float min, float max) {
        glitter(bulbGroups, color, frames, 4, bulbGroups.numBulbs / 2, min, max);
    }

    /**
     * Make a group of bulbs glitter.
     *
     * @param bulbGroup Group of bulbs to make glittering.
     * @param color Glittering base color.
     * @param frames Glitter duration in frames.
     * @param fade Fade time in frames.
     * @param glitteringBulbs Number of bulbs that change brightness during glittering.
     * @param min Minimum brightness.
     * @param max Maximum brightness.
     */
    private void glitter(BulbGroup bulbGroup, Color color, int frames, int fade, int glitteringBulbs, float min, float max) {
        int loops = frames / fade;
        float[] hsbValues = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsbValues);

        while (loops > 0) {
            for (int i = 0; i < glitteringBulbs; i++) {
                int index = RANDOM.nextInt(bulbGroup.numBulbs);
                BulbRGB bulb = bulbGroup.getBulb(index);
                float brightness = randomFloat(min, max);
                hsbFade(bulb, hsbValues[0], hsbValues[1], brightness, fade);
            }
            skip(fade);
            loops--;
        }
    }
}
