package darkness.generator.scripts.uka17;

import darkness.generator.api.BulbRGB;
import java.awt.*;

public class Spinner extends BaseScript {

    @Override
    public void run() {
        super.run();

        float[] center = mergedAllBulbs.getPosition();

        double theta = -Math.PI;
        final double omega = .4;
        final double epsilon = .3;
        final float delta = .2f;
        final int frames = 200;

        Color onColor = new Color(198, 61, 15);
        Color offColor = Color.BLACK;
        Color centerColor = new Color(126, 143, 124);

        for (int i = 0; i < frames; ++i) {
            for (BulbRGB bulb : mergedAllBulbs) {
                float x = bulb.getPosition()[0] - center[0];
                float y = bulb.getPosition()[1] - center[1];

                if (   Math.abs(Math.atan2(y, x) - theta) < epsilon
                    || Math.abs(Math.atan2(-y, -x) - theta) < epsilon) {
                    set(bulb, onColor);
                } else {
                    set(bulb, offColor);
                }

                // If darn close to the center, set anyway
                if( Math.abs(x) < delta && Math.abs(y) < delta)
                    set(bulb, centerColor);
            }

            theta += omega;

            if (theta > Math.PI)
                theta = -Math.PI;

            next();
        }

        for (BulbRGB bulb : mergedAllBulbs)
            rgbFade(bulb, Color.BLACK, 10);

        skip(10);
    }
}
