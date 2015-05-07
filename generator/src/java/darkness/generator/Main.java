package darkness.generator;

import com.zoominfo.util.yieldreturn.Generator;
import darkness.generator.api.BulbManager;
import darkness.generator.api.ScriptManager;
import darkness.generator.scripts.uka13.DemoScript;

import java.util.Iterator;

public class Main {

    public static void main(String[] args) {
        System.out.println("Darkness sequence generator started");

        BulbManager bulbManager = BulbManager.getInstance();
        for(int i = 0; i < 100; i++) {
            bulbManager.registerBulb(i, i*3+1, i*3+2, i*3+3);
        }

        ScriptManager scriptManager = ScriptManager.getInstance();
        scriptManager.Start(new DemoScript());


    }


}
