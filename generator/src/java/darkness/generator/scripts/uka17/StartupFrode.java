package darkness.generator.scripts.uka17;

import darkness.generator.api.BulbGroup;
import darkness.generator.api.BulbRGB;
import java.awt.Color;

public class StartupFrode extends BaseScript {

	private int[] rainbow48 = {255, 255, 255, 255, 255, 255, 255, 255, 255, 223, 191, 159, 128, 96, 64, 32, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 32, 64, 96, 128, 159, 191, 223, 255, 255, 255, 255, 255, 255, 255, 255};

	@Override
	public void run() {
		super.run();
		allon(new Color(210, 200, 0), 1);
		sparkleFade(240, 100, new Color(224, 170, 16), new Color(32, 32, 16), 0, 60, 40);
		for(int i=0; i<100; i=i+20)
			sparkleFade(20, 100, new Color(224, 150, 16), new Color(32, 32, 16), i, 80, 20);
		sparkleFade(240, 100, new Color(224, 120, 16), new Color(32, 32, 16), 100, 80, 20);
	}

	private void allon(Color c, int time)
	{
		for(BulbGroup bulb : allBulbs)
			set(bulb, c);
		skip(20*time);
	}

	private void sparkleFade(int time, int probability, Color mid, Color var, int fade, int intensmid, int intensvar)
	{
		boolean flag = false;
		if(time>=20)
		{
			flag = true;
			time = time/20;
		}

		for(int i=0; i<time; i++)
		{
			for(BulbGroup letter : letters)
				for(BulbRGB bulb : letter)
				{
					double val = Math.random();
					if(randint(1000) < probability || flag)
					{
						int intens = (randint(intensvar*2)-intensvar)+intensmid;
						int randR = randint(var.getRed()*2)-var.getRed();
						int randG = randint(var.getGreen()*2)-var.getGreen();
						int randB = randint(var.getBlue()*2)-var.getBlue();
						Color c = new Color(((mid.getRed()+randR)*(100-fade)/100)*intens/100, ((mid.getGreen()+randG)*(100-fade)/100)*intens/100, ((mid.getBlue()+randB)*(100-fade)/100)*intens/100);
						for(BulbGroup digit : digits)
							for(BulbRGB dbulb : digit)
								if(bulb == dbulb)
									c = new Color(c.getRed()+255*fade/100, c.getGreen()+255*fade/100, c.getBlue()+0*fade/100);
						rgbFade(bulb, c, 20);
					}
				}
			if(flag)
				skip(20);
			else
				skip(1);
		}
	}

	private void sparkle(int time, int saturation, int probability, int desatR, int desatG, int desatB, int fade, int blkval)
	{
		for(int i=0; i<time; i++)
		{
			for(BulbGroup letter : letters)
				for(BulbRGB bulb : letter)
				{
					double val = Math.random();
					if(randint(100) < probability)
					{
						int rb = randint(48);
						int Rval = desatR*(100-saturation)/100 + rainbow48[rb]*saturation/100;
						int Gval = desatG*(100-saturation)/100 + rainbow48[(rb+16)%48]*saturation/100;
						int Bval = desatB*(100-saturation)/100 + rainbow48[(rb+32)%48]*saturation/100;
						if(letter == A || letter == D || letter == G)
						{
							Rval = 255*(100-fade)/100 + Rval*fade/100;
							Gval = 255*(100-fade)/100 + Gval*fade/100;
							Bval = 64*(100-fade)/100 + Bval*fade/100;
						}
						set(bulb, Rval, Gval, Bval);
					}
					else
					{
						int val2 = 255*fade*blkval/10000;
						set(bulb, val2, val2, val2);
					}
				}
			skip(1);
		}
	}

	private int randint(int val)
	{
		return (int)(Math.random()*val);
	}
}
