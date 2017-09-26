package darkness.generator.api.effects;

import darkness.generator.api.BulbRGB;

import java.awt.*;
import java.util.*;

/**
 * Fan scrolling effect
 */
public class FanScroll extends EffectBase {
    private boolean cancelled = false;
    private LinkedList<BulbRGB> bulbs;
    private float centerX = 0.f;
    private float centerY = 0.f;
    private double farLeftAngle = 100.f;
    private double farRightAngle = -100.f;

    private double epsilon = 0.0; // TODO: Angle epsilon
    private double anglePerFrame = 0.0;

    // TODO: Implement reversing
    public FanScroll(BulbRGB[] bulbs, int period, boolean reverse) {
        //this.bulbs = new ArrayList<BulbRGB>(bulbs);
        this.bulbs = new LinkedList<>(Arrays.asList(bulbs));

        float minX = 100f;
        float maxX = -100f;

        // TODO: There is probably a smarter way to do this
        for (BulbRGB bulb: bulbs) {

            float posX = bulb.getPosition()[0];
            float posY = bulb.getPosition()[1];

            if (posX < minX)
                minX = posX;
            else if (posX > maxX)
                maxX = posX;

            if (posY < centerY)
                centerY = posY;

        }

        this.centerX = (minX + maxX) / 2;
        this.centerY -= 3; // Tweak this

        for (BulbRGB bulb: bulbs) {

            float posX = bulb.getPosition()[0];
            float posY = bulb.getPosition()[1];

            double angle = Math.atan2(posX - centerX, posY - centerY);

            if (angle < farLeftAngle) {
                farLeftAngle = angle;
            }
            else if (angle > farRightAngle) {
                farRightAngle = angle;
            }
        }

        anglePerFrame = (farRightAngle - farLeftAngle) / (period - 8);
        farLeftAngle -= 4 * anglePerFrame;
        farRightAngle += 4 * anglePerFrame;
        epsilon = anglePerFrame * 3;
    }

    @Override
    public void cancel() {
        cancelled = true;
    }

    @Override
    public void run() {

        double state = farLeftAngle;
        Color colorOn = Color.red;
        Color colorOff = Color.blue;

        while (!cancelled) {
            state += anglePerFrame;

            for (BulbRGB bulb: bulbs) {
                double angle = Math.atan2(bulb.getPosition()[0] - centerX, bulb.getPosition()[1] - centerY);

                if (Math.abs(angle - state) < epsilon) {
                    set(bulb, colorOn);
                }
                else {
                    set(bulb, colorOff);
                }
            }

            if (state >= farRightAngle)
                state = farLeftAngle;


            // Next frame
            next();
        }

    }

    @Override
    public String toString() {
        return "FanScroll running on (some bulb data here)";
    }
}
