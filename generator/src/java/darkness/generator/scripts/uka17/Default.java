package darkness.generator.scripts.uka17;

import darkness.generator.api.BulbGroup;

public class Default extends BaseScript{

    @Override
    public void run() {
        super.run();
        yellow();
    }

    private void yellow ()
    {
        for (BulbGroup letters : allBulbs)
        {
            set(letters,218,165,32);
        }

        for(BulbGroup digits : digits)
        {
            set(digits,255,83,16);
        }
        set(I,255,83,16);
    }
}
