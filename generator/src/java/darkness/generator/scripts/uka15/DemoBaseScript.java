package darkness.generator.scripts.uka15;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.BulbRGB;
import darkness.generator.api.ScriptBase;

// We should make one such base class for UKA'15, so that the individual scripts don't need to define all the letters
public class DemoBaseScript extends ScriptBase {
	protected BulbRGB topOfJ;
	protected BulbGroup J;
	protected BulbGroup U;
	protected BulbGroup B;
	protected BulbGroup A;
	protected BulbGroup L;
	protected BulbGroup O;
	protected BulbGroup N;
	protected BulbGroup G;
	protected BulbGroup[] letters;

	@Override
	public void run() {
		topOfJ = bulb(6);
		J = group(0, 1, 2, 3, 4, 5, 6);
		U = group(10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20);
		B = group(24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39);
		A = group(42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54);
		L = group(58, 59, 60, 61, 62, 63, 64, 65);
		O = group(138, 139, 140, 107, 106, 105, 104, 108, 123, 122, 121, 103, 109,
				124, 133, 132, 120, 102, 110, 125, 134, 137, 131, 119, 101, 111, 126,
				135, 136, 130, 118, 112, 127, 128, 129, 117, 113, 114, 115, 116);
		N = group(70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83);
		G = group(87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97);
		letters = new BulbGroup[]{J, U, B, A, L, O, N, G};
	}
}
