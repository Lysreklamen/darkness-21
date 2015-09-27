package darkness.generator.scripts.uka15;

import darkness.generator.api.effects.PointRainbow;

/**
 * Created by janosa on 27.09.15.
 */
public class SphericalRainbow extends BaseScript {

    @Override
    public void run() {
        super.run();

        PointRainbow pointRainbow = new PointRainbow(allBulbs, new float[]{5.0f, 0.7f}, 4f);
        // Turn off the brightness, and start fading in
        pointRainbow.setBrightness(0.0f);
        effect(pointRainbow);

        next();
        for(int frame = 1; frame <= 30; frame++) {
            pointRainbow.setBrightness((1.0f*frame)/30.0f);
            next();
        }

        // Start moving the center point around
        for(int frame = 0; frame < 30*20; frame++) {
            float y = 0.7f+0.5f*(float)Math.sin(frame*0.03f);
            float x = 5.0f+6f*(float)Math.sin(frame*0.002f);
            pointRainbow.setCenterPos(new float[]{x, y});
            next();
        }
        skip(40);

        pointRainbow.cancel();

        // Fade back to black
        for(int frame = 1; frame <= 30; frame++) {
            pointRainbow.setBrightness(1.0f-(1.0f*frame)/30.0f);
            next();
        }


    }
}
