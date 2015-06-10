package darkness.generator;

import darkness.generator.api.BulbManager;
import darkness.generator.api.ScriptManager;
import darkness.generator.scripts.uka13.DemoScript;

public class Main {
    public static void main(String[] args) {
        System.out.println("Darkness sequence generator started");

        BulbManager bulbManager = BulbManager.getInstance();
        for(int i = 0; i < 100; i++) {
            bulbManager.registerBulb(i, 200 + i, 300 + i, 400 + i);
        }

        ScriptManager scriptManager = ScriptManager.getInstance();
        scriptManager.Start(new DemoScript());
    }
}
