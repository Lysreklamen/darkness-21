package darkness.generator.scripts.uka17;

import darkness.generator.api.BulbGroup;

import java.awt.*;

public class RGBDebug extends BaseScript {
    @Override
    public void run() {
        super.run();
        Color[] colors = {Color.RED, Color.GREEN, Color.BLUE};
        for (Color color : colors) {
            for (BulbGroup letter : letters) {
                rgbFade(letter, color, 80);
            }
            skip(80);
            for (BulbGroup letter : letters) {
                rgbFade(letter, Color.BLACK, 80);
            }
            skip(80);
        }
    }
}
