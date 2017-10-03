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

        // Number of partitions
        int numGroups = 3;

        LinkedList<BulbRGB>[] arrays = new LinkedList[numGroups];

        for(int i = 0; i < numGroups; ++i)
            arrays[i] = new LinkedList<>();

        BulbGroup[] groups = new BulbGroup[numGroups];

        // Don't pollute
        {
            int count = 0;

            // TODO: Probably a smooth functional way to do this
            // Setup bulb groups for vegas sign digits
            for (BulbGroup digit : digits) {
                for (BulbRGB bulb : digit) {
                    arrays[count].add(bulb);
                    ++count;

                    if (count >= numGroups)
                        count = 0;
                }
            }
        }

        for (int i = 0; i < numGroups; ++i) {
            groups[i] = new BulbGroup(arrays[i].stream().toArray(BulbRGB[]::new));
        }

        // Feel free to open your eyes now

        // I wish java had structs
        final Color COL_startBg = new Color(88, 24, 69);
        final Color COL_endBg = new Color(39, 41, 109);
        final Color COL_fan = new Color(109, 128, 55);
        final Color COL_bulbOn = Color.WHITE;
        final Color COL_bulbOff = null; // Relinquish

        // Initial sign colour
        rgbFade(mergedAllBulbs, COL_startBg, 10);
        skip(10);

        // Vegas casino neon sign effect
        Cycle cycle = new Cycle(COL_bulbOn, COL_bulbOff, 3, groups);
        effect(cycle);

        // Scrolling fan, nice background indeed
        FanScroll fan = new FanScroll(allBulbsRGB, 60, COL_fan, true, false, 2.0);
        effect(fan);

        for(int i = 0; i < 2; ++i) {

            // Fade background to another colour while all the other shit is running
            for (BulbRGB bulb : allBulbsRGB)
                rgbFade(bulb, COL_endBg, 120);

            // Yep yep yep
            skip(120);

            // Fade back
            for (BulbRGB bulb : allBulbsRGB)
                rgbFade(bulb, COL_startBg, 120);

            // Herp derp
            skip(120);
        }

        // Cleanup
        fan.cancel();
        cycle.cancel();

        // Fade out
        for (BulbRGB bulb: mergedAllBulbs)
            rgbFade(bulb, Color.BLACK, 10);

        // Wait for fade
        skip(10);
    }
}
