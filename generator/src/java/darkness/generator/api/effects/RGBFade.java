package darkness.generator.api.effects;

import darkness.generator.api.BulbRGB;
import darkness.generator.api.ScriptBase;

import java.awt.*;

/**
 * Created by janosa on 2/20/15.
 */
public class RGBFade extends EffectBase {
    private final Color startColor;
    private final Color endColor;
    private final BulbRGB bulb;
    private final int frames;
    private boolean cancelled = false;

    public RGBFade(BulbRGB bulb, Color endColor, int frames) {
        this.startColor = bulb.getColor();
        this.endColor = endColor;
        this.bulb = bulb;
        this.frames = frames;
    }

    @Override
    public void run() {
        for(int frame = 0; frame < frames; frame++) {
            if(cancelled) {
                return;
            }
            int red  = startColor.getRed() + ((endColor.getRed() - startColor.getRed())*frame) / frames;
            int green  = startColor.getGreen() + ((endColor.getGreen() - startColor.getGreen())*frame) / frames;
            int blue = startColor.getBlue() + ((endColor.getBlue() - startColor.getBlue())*frame) / frames;

            bulb.set(red, green, blue);
            next();
        }
    }

    @Override
    public void cancel() {
        cancelled = true;
    }

    @Override
    public String toString() {
        return "Effect RGBFade on "+bulb + "from color: "+startColor+" to color " + endColor + " over "+frames+" frames.";
    }
}
