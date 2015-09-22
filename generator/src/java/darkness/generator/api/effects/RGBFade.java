package darkness.generator.api.effects;

import darkness.generator.api.BulbSet;

import java.awt.Color;

public class RGBFade extends EffectBase {
    private final Color startColor;
    private final Color endColor;
    private final BulbSet bulbSet;
    private final int frames;
    private boolean cancelled = false;
    private final float delta_r, delta_g, delta_b;

    public RGBFade(BulbSet bulbSet, Color endColor, int frames) {
        this.startColor = bulbSet.getColor();
        this.endColor = endColor;
        this.bulbSet = bulbSet;
        this.frames = frames;
        this.delta_r = (float)(endColor.getRed() - startColor.getRed()) / (float)frames;
        this.delta_g = (float)(endColor.getGreen() - startColor.getGreen()) / (float)frames;
        this.delta_b = (float)(endColor.getBlue() - startColor.getBlue()) / (float)frames;
    }

    @Override
    public void run() {
        for(int frame = 1; frame <= frames; frame++) {
            if(cancelled) {
                return;
            }
            int red  = startColor.getRed() + (int)(delta_r*frame);
            int green  = startColor.getGreen() + (int)(delta_g*frame);
            int blue = startColor.getBlue() + (int)(delta_b*frame);

            set(bulbSet, red, green, blue);
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
