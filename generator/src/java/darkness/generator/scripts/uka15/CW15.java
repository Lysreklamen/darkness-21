package darkness.generator.scripts.uka15;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.effects.CW;

import java.awt.*;

/**
 * Created by knutaldrin on 30.09.2015.
 */
public class CW15 extends BaseScript {

    @Override
    public void run() {
        super.run();

        BulbGroup cCentre = group(37, 38, 39, 40);
        Color warmWhite = new Color(255, 214, 170);

        CW cw = new CW(cCentre, "lurifax", warmWhite, 3);
        effect(cw);

        skip(cw.getTotalFrames());

        skip(10);
    }
}
