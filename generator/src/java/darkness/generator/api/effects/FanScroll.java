package darkness.generator.api.effects;

import darkness.generator.api.BulbRGB;

import java.awt.Color;
import java.util.*;

/**
 * Fan scrolling effect
 */
public class FanScroll extends EffectBase {
    private List<BulbRGB> bulbs;
    private float centerX = 0.f;
    private float centerY = 0.f;
    private double farLeftAngle = 100.f;
    private double farRightAngle = -100.f;
    private Color fanColor = Color.BLACK;
    private boolean rightToLeft = false;
    private boolean alternate = false;

    private double epsilon = 1.0; // TODO: Angle epsilon
    private int period = 0;
    private double anglePerFrame = 0.0;

    public FanScroll(Collection<BulbRGB> bulbs, int period, Color fanColor, boolean alternate, boolean rightToLeft, double scaling) {
        this.bulbs = new ArrayList<BulbRGB>(bulbs);
        this.fanColor = fanColor;
        this.period = period;
        this.alternate = alternate;
        this.rightToLeft = rightToLeft;
        this.epsilon = scaling;

        setup();
    }

    public FanScroll(BulbRGB[] bulbs, int period, Color fanColor, boolean alternate, boolean rightToLeft, double scaling) {
        this.bulbs = Arrays.asList(bulbs);
        this.fanColor = fanColor;
        this.period = period;
        this.alternate = alternate;
        this.rightToLeft = rightToLeft;
        this.epsilon = scaling;

        setup();
    }

    public FanScroll(Collection<BulbRGB> bulbs, int period, Color fanColor) {
        this(bulbs, period, fanColor, false, false, 1.0);
    }

    public FanScroll(BulbRGB[] bulbs, int period, Color fanColor) {
        this(bulbs, period, fanColor, false, false, 1.0);
    }

    private void setup() {
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
        this.centerY -= 3; // Seems to look alright

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
        epsilon *= anglePerFrame * 3;

        if (rightToLeft)
            anglePerFrame *= -1;

    }

    @Override
    public void run() {
        double state;

        if (rightToLeft)
            state = farRightAngle;
        else
            state = farLeftAngle;

        while (!isCancelled()) {
            state += anglePerFrame;

            for (BulbRGB bulb: bulbs) {
                double angle = Math.atan2(bulb.getPosition()[0] - centerX, bulb.getPosition()[1] - centerY);

                if (Math.abs(angle - state) < epsilon) {
                    set(bulb, fanColor);
                }
                else {
                    relinquish(bulb);
                }
            }

            if (alternate) {
                if ( state >= farRightAngle && anglePerFrame >= 0
                  || state <= farLeftAngle && anglePerFrame <= 0)
                    anglePerFrame *= -1;
            }
            else {
                if (rightToLeft) {
                    if (state <= farLeftAngle)
                        state = farRightAngle;
                } else {
                    if (state >= farRightAngle)
                        state = farLeftAngle;
                }
            }

            next();
        }

    }

    @Override
    public String toString() {
        return "FanScroll running on (some bulb data here)";
    }
}
