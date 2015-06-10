package darkness.generator.api.effects;

import darkness.generator.api.BulbSet;

import java.awt.Color;

/**
 * @todo Implement HSVColor instead of float array
 */
public class HSVFade extends EffectBase {

    private final float[] startHSV;
    private final float[] endHSV;
    private final BulbSet bulbSet;
    private final int frames;
    private boolean cancelled = false;

    public HSVFade(BulbSet bulbSet, float[] endHSV, int frames) {
        startHSV = Color.RGBtoHSB( bulbSet.getRed(), bulbSet.getGreen(), bulbSet.getBlue(), null );
        this.endHSV = endHSV;
        this.bulbSet = bulbSet;
        this.frames = frames;
    }

    @Override
    public void run() {
        for(int frame = 0; frame < frames; ++frame) {
            if(cancelled)
                return;

            float h = startHSV[0] + ( ( endHSV[0] - startHSV[0] ) * frame ) / frames;
            float s = startHSV[1] + ( ( endHSV[1] - startHSV[1] ) * frame ) / frames;
            float v = startHSV[2] + ( ( endHSV[2] - startHSV[2] ) * frame ) / frames;

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
        return "Effect HSVFade on " + bulbSet + " from color: [h=" + startHSV[0] + ",s=" + startHSV[1] + ",v=" + startHSV[2]
                + "] to color: [h=" + endHSV[0] + ",s=" + endHSV[1] + ",v=" + endHSV[2] + "] over " + frames + " frames.";
    }
}
