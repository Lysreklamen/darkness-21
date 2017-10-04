package darkness.generator.scripts.uka17;

public class CountDown extends BaseScript {
    @Override
    public void run() {
        super.run();
        for (int i = 5; i > 1; i--) {
            setCounter(i, true);
            skip(60 * 20);
        }
        for (int i = 60; i >= 0; i--) {
            setCounter(i, false);
            skip(20);
        }
    }
}
