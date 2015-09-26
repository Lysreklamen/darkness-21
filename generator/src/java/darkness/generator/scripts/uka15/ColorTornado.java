package darkness.generator.scripts.uka15;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.BulbRGB;

/**
 * Created by janosa on 25.09.15.
 */
public class ColorTornado extends BaseScript {

    @Override
    public void run() {
        super.run();
        
			
		    skip(20);
		    for(BulbGroup letter: letters) {
		        rgbFade(letter, 0, 0, 0, 20);
		    }
		   	skip(30);
		   	
		   	for (int t = 0; t < 5;t++){
		   		for (int c =0; c < 5;c++){
		   			hsbFade(letters[0], new float[]{(float)c/5, 1f, 1.0f}, 1);
		   			hsbFade(letters[6], new float[]{(float)c/5, 1f, 0f}, 1);
		   			skip(10-t*2+1);
		   			for (int l =1; l < letters.length;l++){
		   				hsbFade(letters[l], new float[]{(float)c/5, 1f, 1.0f}, 1);
		   				hsbFade(letters[l-1], new float[]{(float)c/5, 1f, 0f}, 1);
		   				skip(10-t*2+1);
		   			}
		   		}
		   		
		   		
		   	}
		    
		  	for(BulbGroup letter: letters) {
		        rgbFade(letter, 0, 0, 0, 20);
		    }
		    skip(20);
 
    }
}
