package darkness.generator.api.effects;

import darkness.generator.api.BulbSet;

import java.awt.Color;

public class Cycle extends EffectBase {
	private final BulbSet[] bulbSets;
	private final int numSets;
	private final Color onColor, offColor;
	private final int period;

	public Cycle(Color onColor, Color offColor, int period, BulbSet... bulbSets) {
		this.bulbSets = bulbSets;
		this.numSets = bulbSets.length;
        this.onColor = onColor;
		this.offColor = offColor;
		this.period = period;
	}

	@Override
	public void run() {
	    int currentSet = 0;

	    while (!isCancelled()) {
	        for( int f = 0; f < period; ++f) {
                for (int i = 0; i < numSets; ++i) {
                    if (i == currentSet) {
                        set(bulbSets[i], onColor);
                    } else {
                        if (offColor == null)
                            relinquish(bulbSets[i]);
                        else
                            set(bulbSets[i], offColor);
                    }
                }

                next();
            }

	        ++currentSet;

	        if (currentSet == numSets)
	            currentSet = 0;
        }
	}

	@Override
	public String toString() {
		return "Effect Cycle on " + bulbSets + " for color " + onColor + " (off color " + offColor + ") with period" + period + ".";
	}
}
