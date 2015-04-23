package darkness.generator.output;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Created by knutaldrin on 26.02.15.
 */
public class FileOutput extends BaseOutput {

    public FileOutput( String fileName ) {
        try {
            writer = new BufferedWriter( new FileWriter( fileName ) );
        } catch (IOException e) {
            e.printStackTrace();
        }
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
