package darkness.generator.scripts.uka17;

import darkness.generator.api.BulbRGB;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomFade extends BaseScript {

    private Random random = new Random(1337);

    @Override
    public void run() {
        super.run();

        int loops = 10;
        while (loops > 0) {
            fade(10, getRandomColor());
            loops--;
        }
        fadeOut(10);
    }

    private Color getRandomColor() {
        return new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    private List<BulbRGB> getShuffledBulbs() {
        List<BulbRGB> shuffled = Arrays.asList(allBulbsRGB);
        Collections.shuffle(shuffled);
        return shuffled;
    }

    private void fade(int duration, Color color) {
        for (BulbRGB bulb: getShuffledBulbs()) {
            rgbFade(bulb, color, duration);
            skip(1);
        }
    }

    private void fadeOut(int duration) {
        fade(duration, Color.BLACK);
    }
}
