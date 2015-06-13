package darkness.generator.output;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

public class FileOutput extends BaseOutput {
    public FileOutput(String fileName) throws IOException {
        writer = new BufferedWriter(new FileWriter(fileName));
    }

    @Override
    public void flush() throws IOException {
        writer.flush();
    }
}
