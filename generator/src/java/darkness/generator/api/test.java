package darkness.generator.api;

/**
 * Created by janosa on 2/20/15.
 */
public class test {
    private static test ourInstance = new test();

    public static test getInstance() {
        return ourInstance;
    }

    private test() {
    }
}
