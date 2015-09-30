package darkness.generator.scripts.uka15;

import darkness.generator.api.BulbRGB;
import darkness.generator.api.effects.EffectBase;
import darkness.generator.api.effects.Hold;
import darkness.generator.api.effects.RGBFade;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Snow extends BaseScript {
	@Override
	public void run() {
		super.run();
		Random random = new Random(42);
		double[] xes = Arrays.stream(columns)
				.mapToDouble(c -> c.getBulb(0).getPosition()[0])
				.filter(x -> x > 1.3)
				.toArray();
		double snowLevel = -0.5;
		double snowHeight = LETTER_HEIGHT - snowLevel;
		int iterations = 100;
		int sleep = 3;
		List<EffectBase> effects = new ArrayList<>();
		Set<Integer> fixed = new HashSet<>();
		for (int i = 0; i < iterations; i++) {
			double x = xes[random.nextInt(xes.length)];
			effect(new SnowEffect(allBulbs, x));
			snowLevel += snowHeight / iterations;
			for (BulbRGB bulb : allBulbs) {
				if (bulb.getPosition()[1] < snowLevel && !fixed.contains(bulb.getId())) {
					Hold hold = new Hold(bulb, Color.WHITE, sleep * (iterations - i + 60));
					hold.setPriority(1);
					effect(hold);
					effects.add(hold);
					fixed.add(bulb.getId());
				}
			}
			skip(sleep);
		}
		skip(60);
		for (BulbRGB bulb : allBulbs) {
			RGBFade fade = new RGBFade(bulb, Color.BLACK, 40);
			fade.setPriority(2);
			effect(fade);
			effects.add(fade);
		}
		skip(40);
		for (EffectBase effect : effects) {
			effect.cancel();
		}
	}
}
