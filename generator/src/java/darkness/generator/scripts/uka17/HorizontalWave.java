package darkness.generator.scripts.uka17;

import darkness.generator.api.BulbGroup;

public class HorizontalWave extends BaseScript {

	private int[] rainbow48 = {255, 255, 255, 255, 255, 255, 255, 255, 255, 223, 191, 159, 128, 96, 64, 32, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 32, 64, 96, 128, 159, 191, 223, 255, 255, 255, 255, 255, 255, 255, 255};

	@Override
	public void run() {
		super.run();
		waveover(5);
	}

	private double phase = 0.0;

	private void waveover(int loops)
	{
		for (int k=0; k<loops*rainbow48.length; k++) {
			for (int j=0; j<columns.length; j++) {
				int valR = (int)(rainbow48[(k)%rainbow48.length]*(2+Math.sin(phase+j*2.0*Math.PI/40))/3.0);
				int valG = (int)(rainbow48[(k+6)%rainbow48.length]*(2+Math.sin(phase+j*2.0*Math.PI/40))/3.0);
				int valB = (int)(rainbow48[(k+12)%rainbow48.length]*(2+Math.sin(phase+j*2.0*Math.PI/40))/3.0);
				set(columns[j], valR, valG, valB);
			}
			skip(1);
			phase = phase - 2*Math.PI/40;
		}
	}
}
