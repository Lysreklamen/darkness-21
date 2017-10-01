package darkness.generator.api.effects;

import darkness.generator.api.BulbSet;

import java.awt.Color;

public class WeightedStrobe extends EffectBase {
    private final BulbSet bulbSet;
    //private final Color color;
    private final int tOn;
    private final int tOff;
    private final int repeat;

    public WeightedStrobe(BulbSet bulbSet, int tOn, int tOff, int repeat) {
        //this.color = bulbSet.getColor();
        this.bulbSet = bulbSet;
	this.tOn = tOn;
	this.tOff = tOff;
	this.repeat = repeat;
    }

    @Override
    public void run() {
        for (int i = 0; i < repeat; i++) {
	    Color color = bulbSet.getColor();
            set(bulbSet, 0, 0, 0);
            skip(tOff);
            set(bulbSet, color);
            skip(tOn);
        }
    }

    @Override
    public String toString() {
        return "Effect WeightedStrobe on " + bulbSet + " with t on: " + tOn + " t off: " + tOff + " for " + repeat + " times.";
    }
}
