package darkness.generator.output;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class ConsoleOutput extends BaseOutput {

    public ConsoleOutput() {
        try {
            writer = new BufferedWriter(new OutputStreamWriter(System.out));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void nextFrame() {
        // Flush output so we get each frame on a separate line
        flush();

        super.nextFrame();
    }

    @Override
    public void flush() {
        try {
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
