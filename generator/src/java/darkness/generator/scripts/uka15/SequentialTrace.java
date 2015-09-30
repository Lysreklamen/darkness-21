package darkness.generator.scripts.uka15;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.BulbRGB;

import java.awt.*;

/**
 * Created by knutaldrin on 30.09.2015.
 */
public class SequentialTrace extends BaseScript {

    Color warmWhite = new Color(255, 197, 143);


    @Override
    public void run() {
        super.run();

        turnOnSequentially();

    }

    private void turnOnSequentially() {

        // Turn each bulb on one by one
        for (BulbGroup letter : letters) {
            for (BulbRGB bulb : letter) {
                set(bulb, warmWhite);
                next();
            }
        }

        for (BulbGroup letter : letters) {
            for (BulbRGB bulb : letter) {
                set(bulb, Color.black);
                next();
            }
        }
    }
}
