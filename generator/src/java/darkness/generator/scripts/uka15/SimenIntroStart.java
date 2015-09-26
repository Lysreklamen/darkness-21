package darkness.generator.scripts.uka15;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.BulbRGB;

/**
 * Created by janosa on 25.09.15.
 */
public class SimenIntroStart extends BaseScript {

    @Override
    public void run() {
        super.run();
        
        	
        	for(BulbGroup letter: letters) {
		        set(letter, 0, 0, 0);
			}
			
			
		    skip(20);
		    for(BulbGroup letter: letters) {
		        rgbFade(letter, 100, 100, 100, 20);
		    }
		    skip(20);
		   for(BulbGroup letter: letters) {
		        rgbFade(letter, 0, 0, 0, 1);
		    }

			skip(20);
		    for(BulbGroup letter: letters) {
		        rgbFade(letter, 155, 155, 155, 40);
		    }
		    skip(40);
		    for(BulbGroup letter: letters) {
		        rgbFade(letter, 0, 0, 0, 1);
		    }
		    skip(20);
			
		    for(BulbGroup letter: letters) {
		        rgbFade(letter, 255, 255, 255, 40);
		    }
		    skip(60);
		    for(BulbGroup letter: letters) {
		        rgbFade(letter, 100, 100, 100, 20);
		    }
		    skip(20);
		    for(BulbGroup letter: letters) {
		        rgbFade(letter, 255, 255, 255, 20);
		    }
		    skip(20);
		    skip(20);
		    for(BulbGroup letter: letters) {
		        rgbFade(letter, 200, 200, 200, 20);
		    }
		    skip(20);
		    for(BulbGroup letter: letters) {
		        rgbFade(letter, 255, 255, 255, 20);
		    }
		    
		  
		    skip(80);
 
    }
}
