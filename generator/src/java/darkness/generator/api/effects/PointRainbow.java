package darkness.generator.api.effects;

import darkness.generator.api.BulbRGB;

import java.util.*;

/**
 * A rainbow effect that sources out from a single point with a given radius.
 * The center point can be moved during the effect giving cool results
 * Created by janosa on 27.09.15.
 */
public class PointRainbow extends EffectBase {
    private boolean cancelled = false;
    private List<BulbRGB> bulbs;
    private float[] centerPos = {0.f, 0.0f, 0.0f};
    private float radius;
    private float saturation = 1.0f, brightness = 1.0f;

    public PointRainbow(Collection<BulbRGB> bulbs, float[] centerPos, float radius) {
        this.bulbs = new ArrayList<BulbRGB>(bulbs);
        setCenterPos(centerPos);
        this.radius = radius;
    }

    public PointRainbow(BulbRGB[] bulbs, float[] centerPos, float radius) {
        this.bulbs = Arrays.asList(bulbs);
        setCenterPos(centerPos);
        this.radius = radius;
    }

    @Override
    public void cancel() {
        cancelled = true;

    }

    @Override
    public void run() {
        while (!cancelled) {
            for(BulbRGB bulb: bulbs) {
                // calculate the distance
                float[] pos = bulb.getPosition();

                float distance = (float)Math.sqrt(((centerPos[0]-pos[0])*(centerPos[0]-pos[0])
                        + (centerPos[1]-pos[1])*(centerPos[1]-pos[1])
                        + (centerPos[2]-pos[2])*(centerPos[2]-pos[2])));

                float colorAngle = (distance/radius) % 1.0f; // It's actually possible to do modulus on floating points in java :o
                setHSB(bulb, colorAngle, saturation, brightness);

            }
            next();
        }

    }

    @Override
    public String toString() {
        return "PointRainbow running on (some bulb data here)";
    }

    public void setCenterPos(float[] centerPos) {
        this.centerPos[0] = centerPos[0];
        this.centerPos[1] = centerPos[1];
        if(centerPos.length >= 3) {
            this.centerPos[2] = centerPos[2];
        }
    }

    public float[] getCenterPos() {
        return centerPos;
    }

    public float getRadius() {

        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getSaturation() {
        return saturation;
    }

    public void setSaturation(float saturation) {
        this.saturation = saturation;
    }

    public float getBrightness() {
        return brightness;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
    }
}
