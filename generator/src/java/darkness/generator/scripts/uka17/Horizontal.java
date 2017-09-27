package darkness.generator.scripts.uka17;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.BulbRGB;

public class Horizontal extends BaseScript {
	private int[] rainbow48 = {255, 255, 255, 255, 255, 255, 255, 255, 255, 223, 191, 159, 128, 96, 64, 32, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 32, 64, 96, 128, 159, 191, 223, 255, 255, 255, 255, 255, 255, 255, 255};

	@Override
	public void run() {
		super.run();

		whiteover();
		whiteover();
		scrollrainbow(3);
	}

	private void scrollrainbow(int loops)
	{
		for (int k=0; k<loops; k++)
			for (int i=0; i<rainbow48.length; i++) {
				for (int j=0; j<columns.length; j++) {
					set(columns[j], rainbow48[(i+j)%rainbow48.length], rainbow48[(i+j+16)%rainbow48.length], rainbow48[(i+j+32)%rainbow48.length]);
				}

				skip(1);
			}
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
