package darkness.generator.scripts.uka17;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.BulbRGB;
import darkness.generator.api.effects.Cycle;
import darkness.generator.api.effects.FanScroll;

import java.awt.Color;
import java.util.LinkedList;


public class Vegas extends BaseScript {

    @Override
    public void run() {
        super.run();

        ////// CAUTION //////
        // Ugly code ahead //
        ////// CAUTION //////

        BulbGroup groupA, groupB;
        LinkedList<BulbRGB> a = new LinkedList<>();
        LinkedList<BulbRGB> b = new LinkedList<>();
        boolean flipflop = true;

        // TODO: Probably a smooth functional way to do this
        // Setup bulb groups for vegas sign digits
        for (BulbGroup digit: digits) {
            for (BulbRGB bulb: digit) {
                if (flipflop)
                    a.add(bulb);
                else
                    b.add(bulb);

                flipflop = !flipflop;
            }
        }

        groupA = new BulbGroup(a.stream().toArray(BulbRGB[]::new));
        groupB = new BulbGroup(b.stream().toArray(BulbRGB[]::new));

        // Feel free to open your eyes now
        // TODO: The colours used are for demonstration purposes, and should probably be changed by someone more visual than me.

        // Initial sign colour
        for (BulbRGB bulb: allBulbsRGB)
            set(bulb, Color.RED);

        // Vegas casino neon sign effect
        Cycle cycle = new Cycle(Color.WHITE, Color.BLACK, 10, groupA, groupB);
        effect(cycle);

        // Scrolling fan, nice background indeed
        FanScroll fan = new FanScroll(allBulbsRGB, 30, Color.GREEN);
        effect(fan);

        // Fade background to another colour while all the other shit is running
        for (BulbRGB bulb: allBulbsRGB)
            rgbFade(bulb, Color.BLUE, 90);

        // Yep yep yep
        skip(90);

        // Cleanup
        fan.cancel();
        cycle.cancel();
    }
}
