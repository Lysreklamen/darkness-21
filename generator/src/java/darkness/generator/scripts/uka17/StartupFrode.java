package darkness.generator.scripts.uka17;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.BulbRGB;
import java.awt.Color;

public class StartupFrode extends BaseScript {

	int[] timeoutfade = new int[150];

	@Override
	public void run() {
		super.run();

		for(int i=0; i<timeoutfade.length; i++)
			timeoutfade[i] = 0;

		allon(new Color(218, 165, 32), 3);
		sparkleFade(10*20, 500, new Color(218, 165, 32), new Color(32, 10, 16), new Color(218, 165, 32), 0, 55, 45);
		for(int i=0; i<100; i++)
			sparkleFade(1, 500, new Color(218, 165, 32), new Color(32, 10, 16), new Color(218, 165, 32), i, 40, 40);
		sparkleFade(10*20, 500, new Color(218, 165, 32), new Color(32, 10, 16), new Color(218, 165, 32), 100, 50, 50);
	}

	private void allon(Color c, int time)
	{
		for(BulbGroup bulb : allBulbs)
			set(bulb, c);
		skip(20*time);
	}

	private void sparkleFade(int time, int probability, Color mid, Color var, Color feat, int fade, int intensmid, int intensvar)
	{
		for(int i=0; i<time; i++)
		{
			for(BulbGroup letter : letters)
				for(BulbRGB bulb : letter)
				{
					double val = Math.random();
					if(randint(1000) < probability)
					{
						int intens = (randint(intensvar*2)-intensvar)+intensmid;
						int randR = randint(var.getRed()*2)-var.getRed();
						int randG = randint(var.getGreen()*2)-var.getGreen();
						int randB = randint(var.getBlue()*2)-var.getBlue();

						Color c = new Color(((mid.getRed()+randR)*(100-fade)/100)*intens/100, ((mid.getGreen()+randG)*(100-fade)/100)*intens/100, ((mid.getBlue()+randB)*(100-fade)/100)*intens/100);
						for(BulbGroup digit : digits)
							for(BulbRGB dbulb : digit)
								if(bulb == dbulb)
									c = new Color(c.getRed()+feat.getRed()*fade/100, c.getGreen()+feat.getGreen()*fade/100, c.getBlue()+feat.getBlue()*fade/100);
						
						if(timeoutfade[bulb.getId()]==0)
						{
							rgbFade(bulb, c, 15);
							timeoutfade[bulb.getId()]=15;
						}
					}
				}
			skip(1);
			for(int j=0; j<timeoutfade.length; j++)
				if(timeoutfade[j] != 0)
					timeoutfade[j]--;
		}
	}

	private int randint(int val)
	{
		return (int)(Math.random()*val);
	}
}
