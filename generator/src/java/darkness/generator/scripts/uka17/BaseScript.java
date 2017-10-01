package darkness.generator.scripts.uka17;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.BulbRGB;
import darkness.generator.api.ScriptBase;

import java.util.Arrays;
import java.util.List;

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
    protected BulbGroup digitA;
    protected BulbGroup digitB;
    protected BulbGroup digitC;
    protected BulbGroup[] digits;
    protected BulbGroup[] columns;
    protected BulbGroup[] allBulbs;

    protected BulbRGB[] allBulbsRGB; // ...except for the counter

    protected BulbGroup mergedAllBulbs; // happy with one group containing all bulbs ;)

    protected BulbGroup[] counter;
    protected static final int[][] counterDigits = {
        {0, 1, 2, 3, 4, 5}, // 0
        {1, 2}, // 1
        {0, 1, 6, 4, 3}, // 2
        {0, 1, 6, 2, 3}, // 3
        {5, 6, 1, 2}, // 4
        {0, 5, 6, 2, 3}, // 5
        {0, 5, 4, 3, 2, 6}, // 6
        {0, 1, 2}, // 7
        {0, 1, 2, 3, 4, 5, 6}, // 8
        {6, 5, 0, 1, 2, 3}, // 9
    };

    protected static final double LETTER_HEIGHT = 2.05;
    protected static final double LEFT = 0.14;
    protected static final double RIGHT = 6.33;
    protected static final double WIDTH = RIGHT - LEFT;

    @Override
    public void run() {
        A = group(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
        B = group(15, 16, 17, 18, 19, 20, 21);
        C = group(25, 26);
        D = group(30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43);
        E = group(45, 46, 47, 48, 49, 50, 51, 52, 53, 54);
        F = group(55, 56);
        G = group(60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73);
        H = group(75, 76, 77, 78, 79, 80, 81, 82, 83, 84);
        I = group(85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95);
        letters = new BulbGroup[]{A, B, C, D, E, F, G, H, I};

        digitA = group(0, 1, 2, 3, 4, 5, 6);
        digitB = group(30, 31, 32, 33, 38, 39, 40, 41, 42, 43);
        digitC = group(60, 61, 62, 63, 68, 69, 70, 71, 72, 73);
        digits = new BulbGroup[]{digitA, digitB, digitC};

        allBulbs = new BulbGroup[]{
            group(0,1,2,3,4,5,6,7,8,9,10,11),
            group(15,16,17,18,19,20,21),
            group(25,26),
            group(30,31,32,33,34,35,36,37,38,39,40,41,42,43),
            group(45,46,47,48,49,50,51,52,53,54),
            group(55,56),
            group(60, 61,62,63,64,65,66,67,68,69,70, 71,72,73),
            group(75,76,77,78,79,80,81,82,83,84),
            group(85,86,87,88,89,90,91,92,93,94,95),
        };

        columns = new BulbGroup[]{
            group(6),
            group(0, 2, 4),
            group(1, 3, 5),
            group(7, 8, 9, 10),
            group(11, 15),
            group(17, 18),
            group(16, 21),
            group(20),
            group(19),
            group(25, 26),
            group(30, 31, 32, 33),
            group(34, 35, 36, 37, 38, 43),
            group(39, 42),
            group(40, 41),
            group(45, 46, 47, 48),
            group(50, 51, 52, 53),
            group(49, 54),
            group(55, 56),
            group(60, 61, 62, 63),
            group(64, 65, 66, 67, 68, 73),
            group(69, 72),
            group(70, 71),
            group(75, 76, 77),
            group(82, 83, 84),
            group(78),
            group(79, 80, 81),
            group(85, 86, 87, 88, 89),
            group(95),
            group(90, 91, 92, 93, 94),
        };

        allBulbsRGB = Arrays.stream(letters).flatMap(letter -> letter.getAllBulbs().stream()).toArray(BulbRGB[]::new);
	
	    mergedAllBulbs = new BulbGroup(allBulbsRGB);

        counter = new BulbGroup[]{
            group(101, 102, 103, 104, 105, 106, 107),
            group(108, 109, 110, 111, 112, 113, 114),
        };
    }

    protected BulbGroup mergeBulbGroups(BulbGroup... groups) {
        return new BulbGroup(Arrays.stream(groups).flatMap(g -> g.getAllBulbs().stream()).toArray(BulbRGB[]::new));
    }

    protected void setCounter(int number, boolean leadingZero) {
        setDigit(0, number / 10, leadingZero);
        setDigit(1, number % 10, true);
    }

    private void setDigit(int digitIndex, int digit, boolean showZero) {
        List<BulbRGB> bulbs = counter[digitIndex].getAllBulbs();
        int[] bulbIndices = counterDigits[digit];
        for (BulbRGB bulb : bulbs) {
            set(bulb, 0, 0, 0);
        }
        if (digit != 0 || showZero) {
            for (int i : bulbIndices) {
                set(bulbs.get(i), 255, 0, 0);
            }
        }
    }
}
