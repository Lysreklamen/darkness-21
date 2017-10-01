package darkness.generator.scripts.uka17;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.BulbRGB;

import darkness.generator.api.effects.WeightedStrobe;

import java.awt.*;

public class HauptsacheStrobo extends BaseScript {
    @Override
    public void run() {
        super.run();

        Color bg = new Color(255, 255, 255);
        int t_base = 3;
	int t_on = 3;
	int t_off = 1;
	int n_flash = 5;
	
	for (BulbRGB bulb : allBulbs) {
		rgbFade(bulb, bg, t_base * (t_on + t_off) * n_flash);
	}
	skip(t_base * (t_on + t_off) * n_flash);

	effect(new WeightedStrobe(A, t_base * t_on, t_base * t_off, n_flash));
	effect(new WeightedStrobe(B, t_base * t_on, t_base * t_off, n_flash));
	rgbFade(A, Color.RED, t_base * (t_on + t_off) * n_flash);
	rgbFade(B, Color.RED, t_base * (t_on + t_off) * n_flash);
	skip(t_base * (t_on + t_off) * n_flash);

	rgbFade(C, Color.ORANGE, t_base * (t_on + t_off) * n_flash);
	skip(t_base * (t_on + t_off));

	effect(new WeightedStrobe(D, t_base * t_on, t_base * t_off, n_flash));
	effect(new WeightedStrobe(E, t_base * t_on, t_base * t_off, n_flash));
	rgbFade(D, Color.YELLOW, t_base * (t_on + t_off) * n_flash);
	rgbFade(E, Color.YELLOW, t_base * (t_on + t_off) * n_flash);
	skip(t_base * (t_on + t_off) * n_flash);
	
	rgbFade(F, Color.GREEN, t_base * (t_on + t_off) * n_flash);
	skip(t_base * (t_on + t_off));

	effect(new WeightedStrobe(G, t_base * t_on, t_base * t_off, n_flash));
	effect(new WeightedStrobe(H, t_base * t_on, t_base * t_off, n_flash));
	effect(new WeightedStrobe(I, t_base * t_on, t_base * t_off, n_flash));
	rgbFade(G, Color.BLUE, t_base * (t_on + t_off) * n_flash);
	rgbFade(H, Color.BLUE, t_base * (t_on + t_off) * n_flash);
	rgbFade(I, Color.MAGENTA, t_base * (t_on + t_off) * n_flash);
	skip(t_base * (t_on + t_off) * n_flash);
	
	for (BulbRGB bulb : allBulbs) {
		rgbFade(bulb, Color.BLACK, t_base * (t_on + t_off) * n_flash);
	}
	skip(t_base * (t_on + t_off) * n_flash);
	}
}
