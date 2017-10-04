package darkness.generator.scripts.uka17;

import darkness.generator.api.BulbGroup;

public class HorizontalLight extends BaseScript {

	@Override
	public void run() {
		super.run();

		whiteover();
		whiteover();
		whiteover();
	}

	private void whiteover()
	{
		for (BulbGroup column : columns) {
			set(column, 255, 255, 255);
			skip(1);
		}
		for (BulbGroup column : columns) {
			set(column, 0, 0, 0);
			skip(2);
		}
	}
}
