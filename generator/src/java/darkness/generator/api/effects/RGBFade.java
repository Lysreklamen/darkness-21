package darkness.generator.api.effects;

import darkness.generator.api.BulbSet;

import java.awt.Color;

public class RGBFade extends EffectBase {
    private final Color startColor;
    private final Color endColor;
    private final BulbSet bulbSet;
    private final int frames;
    private boolean cancelled = false;

    public RGBFade(BulbSet bulbSet, Color endColor, int frames) {
        this.startColor = bulbSet.getColor();
        this.endColor = endColor;
        this.bulbSet = bulbSet;
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

            bulbSet.set(red, green, blue);
            next();
        }
    }

    @Override
    public void cancel() {
        cancelled = true;
    }

    @Override
    public String toString() {
        return "Effect RGBFade on " + bulbSet + " from color: " + startColor + " to color: " + endColor + " over " + frames + " frames.";
    }
}
