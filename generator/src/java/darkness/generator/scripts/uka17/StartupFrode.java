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

		allon(new Color(218, 165, 32), 60);
		for(int i=0; i<20; i++)
		{
			for(int j=10; j>5; j--)
				sparkleFade(6, 500, new Color(218, 165, 32), new Color(64, 10, 32), new Color(218, 165, 32), 0, j*10, 100-(j*10));
			for(int j=5; j<10; j++)
				sparkleFade(6, 500, new Color(218, 165, 32), new Color(64, 10, 32), new Color(218, 165, 32), 0, j*10, 100-(j*10));
		}
		for(int i=0; i<100; i++)
			sparkleFade(2, 500, new Color(218, 165, 32), new Color(32, 10, 16), new Color(218, 165, 32), i, 60, 40);
		sparkleFade(60*20, 500, new Color(218, 165, 32), new Color(32, 10, 16), new Color(218, 155, 32), 100, 50, 50);
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
						if(intens>100)
							intens = 100;
						int randR = randint(var.getRed()*2)-var.getRed();
						int randG = randint(var.getGreen()*2)-var.getGreen();
						int randB = randint(var.getBlue()*2)-var.getBlue();
						int valR = ((mid.getRed()+randR)*(100-fade)/100)*intens/100;
						int valG = ((mid.getGreen()+randG)*(100-fade)/100)*intens/100;
						int valB = ((mid.getBlue()+randB)*(100-fade)/100)*intens/100;

						for(BulbGroup digit : digits)
							for(BulbRGB dbulb : digit)
								if(bulb == dbulb)
								{
									valR = valR+feat.getRed()*fade/100;
									valG = valG+feat.getGreen()*fade/100;
									valB = valB+feat.getBlue()*fade/100;
								}
						if(valR>255)
							valR = 255;
						if(valG>255)
							valG = 255;
						if(valB>255)
							valB = 255;

						Color c = new Color(valR, valG, valB);						

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
