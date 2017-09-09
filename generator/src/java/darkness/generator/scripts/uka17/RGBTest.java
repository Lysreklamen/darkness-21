package darkness.generator.scripts.uka17;

import darkness.generator.api.BulbGroup;

public class RGBTest extends BaseScript {
	@Override
	public void run() {
		super.run();
		for (BulbGroup letter : letters) {
			set(letter, 255, 0, 0);
		}
		skip(20);
		for (BulbGroup letter : letters) {
			set(letter, 0, 255, 0);
		}
		skip(20);
		for (BulbGroup letter : letters) {
			set(letter, 0, 0, 255);
		}
		skip(20);
	}
}
