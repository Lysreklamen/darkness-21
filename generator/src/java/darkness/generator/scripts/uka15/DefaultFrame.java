package darkness.generator.scripts.uka15;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.BulbRGB;

public class DefaultFrame extends BaseScriptUka15 {
	@Override
	public void run() {
		super.run();
		int[] values = {127, 255};
		int i = 0;
		for (BulbGroup letter : letters) {
			for (BulbRGB bulb : letter) {
				int r = values[i % values.length];
				int g = values[(i / values.length) % values.length];
				int b = values[(i / (values.length * values.length)) % values.length];
				set(bulb, r, g, b);
				i++;
			}
		}
	}
}
