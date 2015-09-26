package darkness.generator.scripts.uka15;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.BulbRGB;

/**
 * Created by janosa on 25.09.15.
 */
public class SimenIntro extends BaseScript {

    @Override
    public void run() {
        super.run();
        
        
        
        	// start with white
		    for (BulbGroup letter: letters) {
		    	
		        set(letter, 20, 20, 20);
		    }
		    skip(40);
		    
		    // start glowing
		    for (BulbGroup letter: letters) {
		        rgbFade(letter, 100, 100, 100, 40);
		    }
		    skip(40);
		    
		    // increase I 
		    for (BulbGroup letter: letters) {
		        rgbFade(letter, 20, 20, 20, 40);
		    }
		    rgbFade(letters[3], 255, 255, 255, 40);
		    skip(60);
		    
		    // flow copper
		    int centerColunt = columns.length/2-4;
		   
		   //rgbFade((BulbGroup)columns[centerColunt],20,20,20,8);
		    
		    hsbFade( letters[3],  new float[]{27.0f/360, 1f, 0.5f}, 24);
		    
		    for(int i = 1; i < columns.length/2-3; i++) {
		    		hsbFade( (BulbGroup)columns[centerColunt-i],  new float[]{27.0f/360, 1f, 0.5f}, 8);			
		    		hsbFade( (BulbGroup)columns[centerColunt+i],  new float[]{27.0f/360, 1f, 0.5f}, 8);
            	skip(4);
        	}
        	
        	
        	for(int i =columns.length/2-3 ; i < columns.length/2+5; i++) {
		    				
		    		hsbFade( (BulbGroup)columns[centerColunt+i],  new float[]{27.0f/360, 1f, 0.5f}, 8);
            	skip(4);
        	}
        	
        	skip(60);
        	
        	
        	for(BulbGroup letter: letters) {
		        hsbFade( letter,  new float[]{27.0f/360, 1f, 1f}, 15);
		        skip(20);
		        hsbFade( letter,  new float[]{27.0f/360, 1f, 0.5f}, 10);
		        skip(10);
		     
		    }
		    
		    skip(20);
		    for(BulbGroup letter: letters) {
		        hsbFade( letter,  new float[]{27.0f/360, 0f, 1f}, 40);

		    }
		    skip(80);
		    
        	
      
    }
}
