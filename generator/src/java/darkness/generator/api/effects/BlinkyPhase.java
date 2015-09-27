package darkness.generator.api.effects;

import darkness.generator.api.BulbManager;
import darkness.generator.api.BulbRGB;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by knutaldrin on 27.09.2015.
 */
public class BlinkyPhase extends EffectBase {
    private boolean cancelled = false;
    // Pseudorandom with fixed seed. Can be changed to get a different, but similar effect
    private Random random = new Random(1337);
    @Override
    public void run() {
        LinkedList<BulbRGB> freeBulbs = new LinkedList<BulbRGB>(BulbManager.getInstance().getAllBulbs());
        while (!(cancelled || freeBulbs.isEmpty()))
        {
            int bulbIndex = random.nextInt(freeBulbs.size());
            BulbRGB bulb = freeBulbs.get(bulbIndex);
            if(bulb.getRed() != 0 || bulb.getGreen() != 0 || bulb.getBlue() != 0)
            {
                // This bulb is in use. Lets find a new one
                freeBulbs.remove(bulbIndex);
                continue;
            }
            setHSB(bulb, random.nextFloat(), 0.6f, 0.7f);
            Color color = bulb.getColor();
            next();
            // The color has not changed by another effect. Turn it off
            if(bulb.getColor().equals(color))
            {
                set(bulb, 0, 0, 0);
            }
        }
    }

    @Override
    public String toString() {
        return "BlinkyPhase";
    }

    @Override
    public void cancel() {
        cancelled = true;
    }

}