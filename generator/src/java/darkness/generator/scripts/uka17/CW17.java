package darkness.generator.scripts.uka17;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.effects.CW;

import java.awt.*;

public class CW17 extends BaseScript {

    @Override
    public void run() {
        super.run();

        // Change up colours
        Color warmerWhite = new Color(255, 214, 170);
        Color warmWhite = new Color(255, 197, 143);
        Color skyBlue = new Color(64, 156, 255);

        // Defs
        Color restColor = warmWhite;
        Color morseColor = skyBlue;

        rgbFade(A, restColor, 10);
        rgbFade(B, restColor, 10);
        //rgbFade(C, restColor, 10);
        rgbFade(D, restColor, 10);
        rgbFade(E, restColor, 10);
        //rgbFade(F, restColor, 10);
        rgbFade(G, restColor, 10);
        rgbFade(H, restColor, 10);
        //rgbFade(I, restColor, 10);
        skip(10);

        skip(20);

        int framesPerDit = 3;


        // TODO: Change letters to actual UKA name
        CW cwA = new CW(A, "a", morseColor, framesPerDit);
        CW cwB = new CW(B, "b", morseColor, framesPerDit);
        //CW cwC = new CW(C, "c", morseColor, framesPerDit);
        CW cwD = new CW(D, "d", morseColor, framesPerDit);
        CW cwE = new CW(E, "e", morseColor, framesPerDit);
        //CW cwF = new CW(F, "f", morseColor, framesPerDit);
        CW cwG = new CW(G, "g", morseColor, framesPerDit);
        CW cwH = new CW(H, "h", morseColor, framesPerDit);
        //CW cwI = new CW(I, "i", morseColor, framesPerDit);

        effect(cwA);
        skip(cwA.getTotalFrames());
        skip(framesPerDit * 2);
        cwA.cancel();

        effect(cwB);
        skip(cwB.getTotalFrames());
        skip(framesPerDit * 2);
        cwB.cancel();

        /*
        effect(cwC);
        skip(cwC.getTotalFrames());
        skip(framesPerDit * 2);
        cwC.cancel();
        */

        effect(cwD);
        skip(cwD.getTotalFrames());
        skip(framesPerDit * 2);
        cwD.cancel();

        effect(cwE);
        skip(cwE.getTotalFrames());
        skip(framesPerDit * 2);
        cwE.cancel();

        /*
        effect(cwF);
        skip(cwF.getTotalFrames());
        skip(framesPerDit * 2);
        cwF.cancel();
        */

        effect(cwG);
        skip(cwG.getTotalFrames());
        skip(framesPerDit * 2);
        cwG.cancel();

        effect(cwH);
        skip(cwH.getTotalFrames());
        skip(framesPerDit * 2);
        cwH.cancel();

        /*
        effect(cwI);
        skip(cwI.getTotalFrames());
        skip(framesPerDit * 2);
        cwI.cancel();
        */

        skip(20);


        rgbFade(A, Color.black, 10);
        rgbFade(B, Color.black, 10);
        rgbFade(C, Color.black, 10);
        rgbFade(D, Color.black, 10);
        rgbFade(E, Color.black, 10);
        rgbFade(F, Color.black, 10);
        rgbFade(G, Color.black, 10);
        rgbFade(H, Color.black, 10);
        rgbFade(I, Color.black, 10);
        skip(10);

        skip(20);
    }
}
