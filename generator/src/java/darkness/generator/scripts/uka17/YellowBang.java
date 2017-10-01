package darkness.generator.scripts.uka17;


import darkness.generator.api.BulbGroup;

public class YellowBang extends BaseScript{
    int nScroll = 4; // How many times should the scroll scroll
    int timeFrames =4; //How long should the last character blink
    @Override
    public void run() {
        super.run();
        scrollIn(nScroll);
        bang(timeFrames);
        skip(4);
    }

    private void scrollIn(int nScroll)
    {
        for(int j=0; j<nScroll; j++) {

            skip(10);
            for (BulbGroup letters : allBulbsBG) {
			    set(letters, 0, 0, 0);
		    }
            for (int i = 0; i < columns.length - 3; i++) {
                BulbGroup column = columns[i];
                set(column, 0, 128, 128);
                skip(1);
            }
        }
        skip(10);

    }

    private void bang(int timeFrames)
    {
        for(int i=0;i<timeFrames;i++){
            set(I, 0, 128, 128);
            skip(3);
            set(I, 0, 0, 0);
            skip(3);
            set(I, 0, 128, 128);
            skip(3);
        }
    }
}
