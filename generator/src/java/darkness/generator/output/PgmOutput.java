package darkness.generator.output;

import darkness.generator.api.Channel;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates a PGM file from a sequence. PGM is a simple image format, and we let each row in the image
 * represent the channel values in a frame: the value of channel c in frame f is represented by the grayscale value
 * of the pixel at column c-1 in row f. PGM files are text files with the following structure:
 * - First line: the magic string "P2", meaning a grayscale PGM file
 * - Second line: the width (number of channels) and height (number of frames) of the picture
 * - Third line: the grayscale value that represents white; the pixel values will be between 0 and this value, inclusive.
 *               This will normally be 255, but if we use the transparency hack, transparency is represented by 256.
 * - Subsequent lines: each line contains the values of the channels in a frame, from beginning to end
 * Because the number of lines must be specified early on, this class stores all of the frames as they are being generated,
 * and only outputs the PGM file at the very end.
 */
public class PgmOutput extends BaseOutput {
    private final String fileName;
    private final List<int[]> frames = new ArrayList<>();
    private boolean hasTransparency;
    private final static int NUM_CHANNELS = 512;

    public PgmOutput(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void beginFrame() {
        frames.add(new int[NUM_CHANNELS]);
    }

    @Override
    public void writeChannelValue(Channel channel) {
        frames.get(frames.size() - 1)[channel.getChannel() - 1] = channel.getValue();
        if (channel.getValue() == 256) {
            hasTransparency = true;
        }
    }

    @Override
    public void flush() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("P2\n");
            writer.write(NUM_CHANNELS + " " + frames.size() + "\n");
            writer.write((hasTransparency ? 256 : 255) + "\n");
            for (int[] frame : frames) {
                for (int i = 0; i < frame.length; i++) {
                    writer.write(frame[i] + " ");
                }
                writer.write("\n");
            }
            writer.flush();
        }
    }

    // Override everything else in order to silence the superfluous output from BaseOutput
    @Override public void writeString(String str) { }
    @Override public void writeNewline() { }
    @Override public void writeComment(String comment) { }
    @Override public void beginScript() { }
    @Override public void beginScript(String name) { }
    @Override public void endScript() { }
    @Override public void endScript(String name) { }
    @Override public void endFrame() { }
    @Override public void nextFrame() { }
}
