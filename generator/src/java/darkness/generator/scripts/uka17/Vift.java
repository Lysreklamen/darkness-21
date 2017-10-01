package darkness.generator.scripts.uka17;

import darkness.generator.api.effects.FanScroll;

import java.awt.*;

public class Vift extends BaseScript {
    @Override
    public void run() {
        super.run();

        Color c = new Color(218,165,32);
        int t = 3;
        int fade_ext = 7;

	FanScroll fan_full1 = new FanScroll(allBulbsRGB, 50, c, true, false, 1.0);
	effect(fan_full1);
	skip(100);
	fan_full1.cancel();

	for (int i = 0; i < 3; i++) {
        	FanScroll fan_l2r = new FanScroll(allBulbsRGB, 50, c, false, false, 1.2);
        	FanScroll fan_r2l = new FanScroll(allBulbsRGB, 50, c, false, true, 1.2);

        	effect(fan_l2r);
		skip(10);
        	effect(fan_r2l);

		skip(40);
		fan_l2r.cancel();
		skip(10);
		fan_r2l.cancel();
	}

	FanScroll fan_full2 = new FanScroll(allBulbsRGB, 50, c, true, true, 1.0);
	effect(fan_full2);
	skip(100);
	fan_full2.cancel();

	skip(10);
    }
}
