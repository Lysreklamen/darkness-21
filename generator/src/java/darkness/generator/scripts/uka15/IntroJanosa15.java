package darkness.generator.scripts.uka15;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.BulbManager;
import darkness.generator.api.BulbRGB;
import darkness.generator.api.effects.EffectBase;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

public class IntroJanosa15 extends BaseScriptUka15 {
	@Override
	public void run() {
		super.run();

		/*MiniLysreklamen15
		Phase 1, blink bulbs randomly
		Phase 2: for each letter fill up with one color.
				Then increase the intensity, and decrease it again. This will make a glowing effect
		Phase 3: Scroll the current colors out of the sign to right
		Phase 4: Fade the whole sign in one color
		Phase 5: Fade out to black
		Phase 6: Phase one and one letter in
		Phase 7: Morph into a rainbow
		Phase 8: Do the rainbow dance
		Phase 9: Fade back to the color in phase 4
		Phase 10: Fade to black and exit
		 */

		// Start phase 1 in a parallel.
        BlinkyPhase blinky = new BlinkyPhase();
        effect(blinky);
        // Keep blinking for a couple of seconds
        skip(30);

        // Start fading in letters
        fadeIn();
        blinky.cancel();

        skip(30);

        // Scroll out
        scrollOut();

        skip(10);

        // Fade to color

        for(BulbGroup letter: letters) {
            rgbFade(letter, 255, 147, 41, 10);
        }

        skip(20);

        for(BulbGroup letter: letters) {
            rgbFade(letter, 0, 0, 0, 20);
        }

        skip(40);


        for(BulbGroup letter: letters) {
            rgbFade(letter, 255, 147, 41, 10);
            skip(20);
        }



    }

    private void scrollOut() {
        for(int iteration = 0; iteration < columns.length; iteration++) {
            for(int column = columns.length-1; column >= 0; column--) {
                int preColumn = column-1;
                Color preColumnColor = Color.black;
                if(preColumn >= 0) {
                    // Valid colums
                    preColumnColor = columns[preColumn].getBulb(0).getColor();
                }
                for(BulbRGB bulb: columns[column]) {
                    set(bulb, preColumnColor);
                }
            }
            next();
        }
    }

    private void fadeIn() {
        Random random = new Random(1);

        BulbGroup[] fadeInOrder = {C, F, A, D, G, E, B};


        for (int idx = 0; idx < fadeInOrder.length; idx++) {
            BulbGroup letter = fadeInOrder[idx];
            // Bulbs
            LinkedList<BulbRGB> freeBulbs = new LinkedList<BulbRGB>(letter.getAllBulbs());
            // Get a color for our letter
            float colorAngle = ((float)idx)/((float)fadeInOrder.length);

            int i = 0;
            while(!freeBulbs.isEmpty()) {
                int bulbIndex = random.nextInt(freeBulbs.size());
                BulbRGB bulb = freeBulbs.get(bulbIndex);
                setHSB(bulb, colorAngle, 1f, 0.6f);
                freeBulbs.remove(bulbIndex);
                if(++i%2 == 0)
                    skip(1);
            }

            rgbFade(letter, 255, 255, 255, 2);
            //hsbFade(letter, new float[]{colorAngle, 1.0f, 1.0f}, 4);
            skip(4);
            hsbFade(letter,  new float[]{colorAngle, 1f, 0.8f}, 4);
            skip(4);
        }
    }

	private class BlinkyPhase extends EffectBase {
		private boolean cancelled = false;
		// Pseudorandom with fixed seed. Can be changed to get a different, but similar effect
		private Random random = new Random(1337);
		@Override
		public void run() {
            LinkedList<BulbRGB> freeBulbs = new LinkedList<BulbRGB>(BulbManager.getInstance().getAllBulbs());
			while (!(cancelled || freeBulbs.isEmpty()))
			{
                int bulbIndex = random.nextInt(freeBulbs.size());
                BulbRGB bulb = freeBulbs.get(bulbIndex);
                if(bulb.getRed() != 0 || bulb.getGreen() != 0 || bulb.getBlue() != 0)
                {
                    // This bulb is in use. Lets find a new one
                    freeBulbs.remove(bulbIndex);
                    continue;
                }
                setHSB(bulb, random.nextFloat(), 0.6f, 0.7f);
                Color color = bulb.getColor();
                next();
                // The color has not changed by another effect. Turn it off
                if(bulb.getColor().equals(color))
                {
                    set(bulb, 0, 0, 0);
                }
			}
		}

		@Override
		public String toString() {
			return "BlinkyEffect";
		}

		@Override
		public void cancel() {
			cancelled = true;
		}

	}
}
