package darkness.generator.scripts.uka13;

import darkness.generator.api.ScriptBase;
import darkness.generator.api.ScriptManager;

/**
 * Created by janosa on 2/20/15.
 */
public class DemoScript extends ScriptBase {
    @Override
    public void run() {
        next();
        for(int i = 0; i < 30; i++) {
            rgbFade(bulb(i), 255, 0, 0, 30);
            skip(5);
        }
        next();
    }
}
