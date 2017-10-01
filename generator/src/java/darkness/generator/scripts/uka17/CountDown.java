package darkness.generator.scripts.uka17;

public class CountDown extends BaseScript {
    @Override
    public void run() {
        super.run();
        for (int i = 5; i > 1; i--) {
            setCounters(i);
            skip(60 * 20);
        }
        for (int i = 59; i >= 0; i--) {
            setCounters(i);
            skip(20);
        }
    }
}
