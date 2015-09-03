package darkness.generator.scripts.uka15;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.ScriptBase;

public class BaseScriptUka15 extends ScriptBase {
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
	}
}
