package darkness.generator.scripts.uka15;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.ScriptBase;

public class BaseScriptUka15 extends ScriptBase {
	// Obfuscated letters, to prevent the UKA name from being directly readable by someone who looks at the source code over someone's shoulder.
	// Feel free to change to the actual letters if there is consensus that it is safe to do so (or to change to another obfuscation scheme).
	protected BulbGroup A;
	protected BulbGroup B;
	protected BulbGroup C;
	protected BulbGroup[] letters;

	@Override
	public void run() {
		A = group(0, 1, 2, 3, 4, 5, 6, 7, 8);
		B = group(10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23);
		C = group(25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41);
		letters = new BulbGroup[]{A, B, C};
	}
}
