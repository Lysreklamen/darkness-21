package darkness.generator.scripts.uka17;

public class CountUp extends BaseScript {
    @Override
    public void run() {
        super.run();
        for (int i = 0; i < 100; i++) {
            setCounter(i, true);
            skip(10);
        }
    }
}
