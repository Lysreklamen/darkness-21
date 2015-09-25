package darkness.generator.scripts.uka15;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.BulbManager;
import darkness.generator.api.ScriptBase;

public class BaseScript extends ScriptBase {
	// Obfuscated letters, to prevent the UKA name from being directly readable by someone who looks at the source code over someone's shoulder.
	// Feel free to change to the actual letters if there is consensus that it is safe to do so (or to change to another obfuscation scheme).
	protected BulbGroup A;
	protected BulbGroup B;
	protected BulbGroup C;
	protected BulbGroup D;
	protected BulbGroup E;
	protected BulbGroup F;
	protected BulbGroup G;
	protected BulbGroup[] letters;
	protected BulbGroup[] columns;

	@Override
	public void run() {
		A = group(0, 1, 2, 3, 4, 5, 6, 7, 8);
		B = group(10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23);
		C = group(25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41);
		D = group(45, 46, 47, 48, 49);
		E = group(50, 51, 52, 53, 54, 55, 56, 57, 58, 59);
		F = group(65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76);
		G = group(80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95);
		letters = new BulbGroup[]{A, B, C, D, E, F, G};

		// Contains each bulb column in sign
		columns = new BulbGroup[]{
				// A
				group(0, 1, 2, 3, 4, 5, 6),
				group(7),
				group(8),
				//B
				group(10, 11, 12, 13, 14),
				group(15),
				group(16),
				group(17),
				group(18),
				group(19, 20, 21, 22, 23),
				//C
				group(25, 26, 27, 28, 29),
				group(30, 36, 37, 38),
				group(31, 35, 39, 40, 41),
				group(32, 33, 34),
				//D
				group(45, 46, 47, 48, 49),
				//E
				group(50, 51, 52, 53, 54),
				group(55, 58),
				group(56, 59),
				group(57),
				//F
				group(65),
				group(66),
				group(67),
				group(68, 74),
				group(69, 75),
				group(70, 76),
				group(71),
				group(72),
				group(73),
				//G
				group(80, 88),
				group(81, 89, 90),
				group(82, 91),
				group(83),
				group(84, 92),
				group(85, 93),
				group(86, 94),
				group(87, 95)
		};



	}
}
