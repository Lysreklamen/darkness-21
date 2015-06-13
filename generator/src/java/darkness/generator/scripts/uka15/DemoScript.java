package darkness.generator.scripts.uka15;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.ScriptBase;

public class DemoScript extends ScriptBase {
    @Override
    public void run() {
        BulbGroup J = group(0, 1, 2, 3, 4, 5, 6);
        BulbGroup U = group(10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20);
        next();
        rgbFade(J, 255, 255, 0, 30);
        skip(5);
        rgbFade(U, 127, 127, 255, 30);
    }
}
