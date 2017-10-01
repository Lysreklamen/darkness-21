package darkness.generator.scripts.uka17;

import darkness.generator.api.BulbRGB;
import darkness.generator.api.effects.FanScroll;

import java.awt.Color;


public class FanScrollDemo extends BaseScript {

    @Override
    public void run() {
        super.run();

        for (BulbRGB bulb: allBulbsRGB)
            set(bulb, Color.RED);


        FanScroll fan = new FanScroll(allBulbsRGB, 30, Color.GREEN);
        effect(fan);
        // Keep blinking for a couple of seconds

        for (BulbRGB bulb: allBulbsRGB)
            rgbFade(bulb, Color.BLUE, 90);

        skip(90);
        fan.cancel();

        for (BulbRGB bulb: allBulbsRGB)
            set(bulb, Color.RED);

        FanScroll fan2 = new FanScroll(allBulbsRGB, 30, Color.GREEN, true, true, 2.0);
        effect(fan2);
        // Keep blinking for a couple of seconds

        for (BulbRGB bulb: allBulbsRGB)
            rgbFade(bulb, Color.BLUE, 90);

        skip(90);
        fan2.cancel();
    }
}
