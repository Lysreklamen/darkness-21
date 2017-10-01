package darkness.generator.scripts.uka17;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.BulbRGB;

public class SingleFill extends BaseScript {
    @Override
    public void run(){
        super.run();
		BulbRGB previousBulb = null;
		BulbRGB lastBulb = letters[letters.length-1].getBulb(letters[letters.length-1].numBulbs-1);
		BulbGroup lastLetter = letters[letters.length-1];

		do{
            for (BulbGroup letter : letters){
                for (BulbRGB bulb : letter) {
                    set(bulb, 255, 255, 255);
                    if (bulb == lastBulb) {
                        lastBulb = previousBulb;
                        skip(2);
                        lastLetter = letter;
                        break;
                    } else {
                        if (previousBulb != null) {
                            set(previousBulb, 0, 0, 0);
                        }
                        previousBulb = bulb;
                        skip(1);
                    }
                }
                if(lastLetter == letter){
                    break;
                }
            }
        }while(letters[0].getBulb(0) != lastBulb);

		skip(20);
    }
}
