package darkness.generator.scripts.uka17;

import darkness.generator.api.BulbGroup;

public class SimpleNumber extends BaseScript {
    @Override
    public void run() {
        super.run();
        for (BulbGroup digit : digits) {
            set(digit, 255, 255, 255);
        }
    }
}
