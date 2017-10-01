package darkness.generator.scripts.uka17;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.BulbRGB;

import darkness.generator.api.effects.WeightedStrobe;

import java.awt.*;

import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

public class WaterFill extends BaseScript {
    @Override
    public void run() {
        super.run();

        Color c = new Color(0, 127, 255);
        int t = 3;
        int fade_ext = 7;

        float [] positionBuffer;
	SortedMap<Float, ArrayList<BulbRGB>> orderedBulbs;
        for (BulbGroup letter : letters) {
		orderedBulbs = new TreeMap<Float, ArrayList<BulbRGB>>();
		ArrayList<BulbRGB> bulbList;
	        for (BulbRGB bulb : letter) {
		        positionBuffer = bulb.getPosition();
			bulbList = orderedBulbs.getOrDefault(positionBuffer[1], new ArrayList<BulbRGB>());
			bulbList.add(bulb);
			orderedBulbs.put(positionBuffer[1], bulbList);
	        }
		
		float lastHeight;
		for (Entry<Float, ArrayList<BulbRGB>> orderedBulbsElement : orderedBulbs.entrySet()) {
			for (BulbRGB bulb : orderedBulbsElement.getValue()) {
				rgbFade(bulb, c, t+fade_ext);
			}
			skip(t);
			lastHeight = orderedBulbsElement.getKey();
		}
		// TODO: filling drops through thingy missing
            skip(fade_ext);
	    }
	skip(10);
    }
}
