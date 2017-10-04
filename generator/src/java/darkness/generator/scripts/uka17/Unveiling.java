package darkness.generator.scripts.uka17;

public class Unveiling extends BaseScript {
    @Override
    public void run() {
        super.run();
        merge(new CountDown());
        skip(6000);
        merge(new Genesis());
        skip(20);
        turnOffCounter();
    }
}
