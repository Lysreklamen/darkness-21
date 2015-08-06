package darkness.generator.api.effects;

import darkness.generator.api.BulbSet;

import java.awt.Color;

/**
 * @todo Implement HSBColor instead of float array
 */
public class HSBFade extends EffectBase {

    private final float[] startHSB;
    private final float[] endHSB;
    private final BulbSet bulbSet;
    private final int frames;
    private boolean cancelled = false;

    public HSBFade(BulbSet bulbSet, float[] endHSB, int frames) {
        startHSB = Color.RGBtoHSB( bulbSet.getRed(), bulbSet.getGreen(), bulbSet.getBlue(), null );
        this.endHSB = endHSB;
        this.bulbSet = bulbSet;
        this.frames = frames;
    }

    @Override
    public void run() {
        for(int frame = 0; frame < frames; ++frame) {
            if(cancelled)
                return;

            float h = startHSB[0] + ( ( endHSB[0] - startHSB[0] ) * frame ) / frames;
            float s = startHSB[1] + ( ( endHSB[1] - startHSB[1] ) * frame ) / frames;
            float v = startHSB[2] + ( ( endHSB[2] - startHSB[2] ) * frame ) / frames;

            bulbSet.setHSB(h, s, v);
            next();
        }
    }

    @Override
    public void cancel() {
        cancelled = true;
    }

    @Override
    public String toString() {
        return "Effect HSBFade on " + bulbSet + " from color: [h=" + startHSB[0] + ",s=" + startHSB[1] + ",v=" + startHSB[2]
                + "] to color: [h=" + endHSB[0] + ",s=" + endHSB[1] + ",v=" + endHSB[2] + "] over " + frames + " frames.";
    }
}
