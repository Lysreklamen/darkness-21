package darkness.generator.scripts.uka17;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.BulbRGB;
import darkness.generator.api.ScriptBase;

import java.util.Arrays;

public class BaseScript extends ScriptBase {
	protected BulbGroup A;
	protected BulbGroup B;
	protected BulbGroup C;
	protected BulbGroup D;
	protected BulbGroup E;
	protected BulbGroup F;
	protected BulbGroup G;
	protected BulbGroup H;
	protected BulbGroup I;
	protected BulbGroup[] letters;
	protected BulbRGB[] allBulbs;

	@Override
	public void run() {
		A = group(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
		B = group(15, 16, 17, 18, 19, 20, 21);
		C = group(25, 26);
		D = group(30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42);
		E = group(45, 46, 47, 48, 49, 50, 51, 52);
		F = group(55, 56);
		G = group(60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72);
		H = group(75, 76, 77, 78, 79, 80, 81, 82, 83);
		I = group(85, 86, 87, 88, 89, 90, 91, 92, 93);
		letters = new BulbGroup[]{A, B, C, D, E, F, G, H, I};
		allBulbs = Arrays.stream(letters).flatMap(letter -> letter.getAllBulbs().stream()).toArray(BulbRGB[]::new);
	}
}
