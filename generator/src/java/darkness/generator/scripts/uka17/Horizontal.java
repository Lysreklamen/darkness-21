package darkness.generator.scripts.uka17;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.BulbRGB;

public class Horizontal extends BaseScript {

	private BulbGroup[] line = new BulbGroup[29];
	private int[] rainbow48 = {255, 255, 255, 255, 255, 255, 255, 255, 255, 223, 191, 159, 128, 96, 64, 32, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 32, 64, 96, 128, 159, 191, 223, 255, 255, 255, 255, 255, 255, 255, 255};

	@Override
	public void run() {
		super.run();

		line[0] = group(6);
		line[1] = group(0, 2, 4);
		line[2] = group(1, 3, 5);
		line[3] = group(7, 8, 9, 10);
		line[4] = group(11, 15);
		line[5] = group(17, 18);
		line[6] = group(16, 21);
		line[7] = group(20);
		line[8] = group(19);
		line[9] = group(25, 26);
		line[10] = group(30, 31, 32, 33);
		line[11] = group(34, 35, 36, 37, 38, 43);
		line[12] = group(39, 42);
		line[13] = group(40, 41);
		line[14] = group(45, 46, 47, 48);
		line[15] = group(50, 51, 52, 53);
		line[16] = group(49, 54);
		line[17] = group(55, 56);
		line[18] = group(60, 61, 62, 63);
		line[19] = group(64, 65, 66, 67, 68, 73);
		line[20] = group(69, 72);
		line[21] = group(70, 71);
		line[22] = group(75, 76, 77);
		line[23] = group(82, 83, 84);
		line[24] = group(78);
		line[25] = group(79, 80, 81);
		line[26] = group(85, 86, 87, 88, 89);
		line[27] = group(95);
		line[28] = group(90, 91, 92, 93, 94);

		whiteover();
		whiteover();
		scrollrainbow(3);
	}

	private void scrollrainbow(int loops)
	{
		for (int k=0; k<loops; k++)
			for (int i=0; i<rainbow48.length; i++) {
				for (int j=0; j<line.length; j++) {
					set(line[j], rainbow48[(i+j)%rainbow48.length], rainbow48[(i+j+16)%rainbow48.length], rainbow48[(i+j+32)%rainbow48.length]);
				}
				skip(1);
			}
	}

	private void whiteover()
	{
		for (BulbGroup letter : line) {
			set(letter, 255, 255, 255);
			skip(1);
		}
		for (BulbGroup letter : line) {
			set(letter, 0, 0, 0);
			skip(2);
		}
	}
}
