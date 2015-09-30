package darkness.generator.scripts.uka15;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.effects.CW;

import java.awt.*;

/**
 * Created by knutaldrin on 30.09.2015.
 */
public class CW15 extends BaseScript {

    @Override
    public void run() {
        super.run();

        Color warmerWhite = new Color(255, 214, 170);
        Color warmWhite = new Color(255, 197, 143);
        Color skyBlue = new Color(64, 156, 255);

        // Defs
        Color restColor = warmWhite;
        Color morseColor = skyBlue;

        rgbFade(A, restColor, 10);
        rgbFade(B, restColor, 10);
        rgbFade(C, restColor, 10);
        rgbFade(D, restColor, 10);
        rgbFade(E, restColor, 10);
        rgbFade(F, restColor, 10);
        rgbFade(G, restColor, 10);
        skip(10);

        skip(20);

        int framesPerDit = 3;


        CW cwA = new CW(A, "l", morseColor, framesPerDit);
        CW cwB = new CW(B, "u", morseColor, framesPerDit);
        CW cwC = new CW(C, "r", morseColor, framesPerDit);
        CW cwD = new CW(D, "i", morseColor, framesPerDit);
        CW cwE = new CW(E, "f", morseColor, framesPerDit);
        CW cwF = new CW(F, "a", morseColor, framesPerDit);
        CW cwG = new CW(G, "x", morseColor, framesPerDit);

        effect(cwA);
        skip(cwA.getTotalFrames());
        skip(framesPerDit * 2);
        cwA.cancel();

        effect(cwB);
        skip(cwB.getTotalFrames());
        skip(framesPerDit * 2);
        cwB.cancel();

        effect(cwC);
        skip(cwC.getTotalFrames());
        skip(framesPerDit * 2);
        cwC.cancel();

        effect(cwD);
        skip(cwD.getTotalFrames());
        skip(framesPerDit * 2);
        cwD.cancel();

        effect(cwE);
        skip(cwE.getTotalFrames());
        skip(framesPerDit * 2);
        cwE.cancel();

        effect(cwF);
        skip(cwF.getTotalFrames());
        skip(framesPerDit * 2);
        cwF.cancel();

        effect(cwG);
        skip(cwG.getTotalFrames());
        skip(framesPerDit * 2);
        cwG.cancel();

        skip(20);


        rgbFade(A, Color.black, 10);
        rgbFade(B, Color.black, 10);
        rgbFade(C, Color.black, 10);
        rgbFade(D, Color.black, 10);
        rgbFade(E, Color.black, 10);
        rgbFade(F, Color.black, 10);
        rgbFade(G, Color.black, 10);
        skip(10);

        skip(20);
        /*




        //BulbGroup cCentre = group(37, 38, 39, 40);


        CW cw = new CW(cCentre, "lurifax", warmWhite, 3);
        effect(cw);

        skip(cw.getTotalFrames());

        skip(10);
        */
    }
}
