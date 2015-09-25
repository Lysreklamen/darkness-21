package darkness.generator.scripts.uka15;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.BulbRGB;

/**
 * Created by janosa on 25.09.15.
 */
public class RGBTest extends BaseScript {

    @Override
    public void run() {
        super.run();

        for (BulbGroup letter: letters) {
            set(letter, 255, 0, 0);
            skip(40);
            set(letter, 0, 255, 0);
            skip(40);
            set(letter, 0, 0, 255);
            skip(40);
            set(letter, 0, 0, 0);
        }
        for(BulbGroup column: columns) {
            for(BulbRGB bulb: column) {
                set(bulb, 255, 0, 0);
                skip(5);
                set(bulb, 0, 255, 0);
                skip(5);
                set(bulb, 0, 0, 255);
                skip(5);
                set(bulb, 0, 0, 0);

            }
        }
    }
}
