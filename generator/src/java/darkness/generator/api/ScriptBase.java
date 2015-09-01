package darkness.generator.api;

import com.zoominfo.util.yieldreturn.Generator;
import darkness.generator.api.effects.EffectBase;
import darkness.generator.api.effects.RGBFade;
import darkness.generator.api.effects.HSBFade;

import java.awt.*;

/**
 * A {@link ScriptBase} can be anything that controls channel values, either over time or as a one-shot action.
 * Subclasses of {@link EffectBase} will typically be one single effect (which may last across several frames),
 * while direct subclasses of {@link ScriptBase} will typically be scripts that are a collection of effects.
 * Subclasses should implement {@link #run()} as a sequence of calls to the methods in this class.
 * Each method's effect will start (and possibly end) in the current frame, and {@link #next()} and {@link #skip(int)}
 * will advance to the next frame.
 */
public abstract class ScriptBase extends Generator<Void> {
    /**
     * Convenience method for accessing the bulb with id {@code id}.
     */
    protected BulbRGB bulb(int id) {
        return BulbManager.getInstance().getBulb(id);
    }

    /**
     * Convenience method for creating a {@link BulbGroup} for accessing all the bulbs with the ids given by {@code ids}.
     */
    protected BulbGroup group(int... ids) {
        BulbRGB[] bulbs = new BulbRGB[ids.length];
        for (int i = 0; i < bulbs.length; i++) {
            bulbs[i] = bulb(ids[i]);
        }
        return new BulbGroup(bulbs);
    }

    /**
     * The run function of the script is responsible for generating the sequence.
     */
    public abstract void run();

    /*************************************************
     * Sequence functions below
     *************************************************/

    /**
     * Declares the current frame in this script or effect to be finished, and starts a new frame.
     */
    protected void next() {
        yield(null);
    }

    /**
     * Skips a given number of frames before returning.
     * This is equivalent to calling {@link #next() next} a given number of times.
     * @param frames The number of frames to wait.
     */
    protected void skip(int frames) {
        for(int i = 0; i < frames; i++) {
            next();
        }
    }

    /**
     * Runs an effect in parallel to the current script.
     */
    protected void effect(EffectBase effect) {
        ScriptManager.getInstance().registerEffect(effect);
    }

    /**
     * Runs another script in parallel to the current script.
     */
    protected void merge(ScriptBase script) {
        ScriptManager.getInstance().registerScript(script);
    }

    /**
     * Sets a color on a bulb.
     * @param bulbSet The bulb or bulb group to set the color on
     * @param red Value between 0..255
     * @param green Value between 0..255
     * @param blue Value between 0..255
     */
    protected void setRGB(BulbSet bulbSet, int red, int green, int blue) {
        bulbSet.set(red, green, blue);
    }

    /**
     * Sets a color on a bulb.
     */
    protected void setRGB(BulbSet bulbSet, Color color) {
        bulbSet.set(color);
    }

    /**
     * Sets a HSB color on a bulb.
     * @param bulbSet The bulb or bulb group to set the color on
     * @param hue The floor of this number is subtracted from it to create a fraction between 0 and 1. This fractional number is then multiplied by 360 to produce the hue angle in the HSB color model.
     * @param saturation In the range 0.0..1.0
     * @param brightness In the range 0.0..1.0
     */
    protected void setHSB(BulbSet bulbSet, int hue, int saturation, int brightness) {
        bulbSet.setHSB(hue, saturation, brightness);
    }

    /**
     * Starts a new {@link RGBFade} effect on the given bulb(s).
     */
    protected void rgbFade(BulbSet bulbSet, Color color, int duration) {
        effect(new RGBFade(bulbSet, color, duration));
    }

    /**
     * Starts a new {@link HSBFade} effect on the given bulb(s).
     */
    protected void hsbFade(BulbSet bulbSet, float[] color, int duration) {
        effect(new HSBFade(bulbSet, color, duration));
    }

    /**
     * Starts a new {@link RGBFade} effect on the given bulb(s).
     */
    protected void rgbFade(BulbSet bulbSet, int red, int green, int blue, int duration) {
        rgbFade(bulbSet, new Color(red, green, blue), duration);
    }
}
