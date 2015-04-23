package darkness.simulator;

import darkness.simulator.dmx.Frame;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PgmReader {
    private final String fileName;
    private final List<Frame> frames = new ArrayList<Frame>();
    /** The number of frames in the file. Is null until {@link #read()} has been called and the headers have been read. */
    private Integer frameCount;
    private BufferedReader reader;
    private int lineNumber = 0;

    public PgmReader(String fileName) {
        this.fileName = fileName;
    }

    public void read() throws IOException, ParseException {
        reader = null;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            readHeaders();
            readFrames();
        } catch (Throwable t) {
            // Handle files with errors gracefully by pretending that they're empty
            frameCount = 0;
            synchronized (frames) {
                frames.clear();
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    public String getFileName() {
        return fileName;
    }

    public Integer getFrameCount() {
        return frameCount;
    }

    public Frame getFrame(int index) {
        synchronized (frames) {
            return index >= 0 && index < frames.size() ? frames.get(index) : null;
        }
    }

    private void readHeaders() throws IOException, ParseException {
        String magicHeader = reader.readLine();
        if(!magicHeader.equals("P2")) {
            throw new ParseException(fileName, 1, "Invalid magic header");
        }

        String[] dimensions = reader.readLine().split(" ");
        if (dimensions.length != 2) {
            throw new ParseException(fileName, 2, "Invalid dimension header");
        }
        frameCount = Integer.parseInt(dimensions[1]);

        String maxValue = reader.readLine();
        if (!maxValue.equals("255")) {
            throw new ParseException(fileName, 3, "Unsupported max value header");
        }
        lineNumber = 3;
    }

    private void readFrames() throws IOException, ParseException {
        synchronized (frames) {
            frames.clear();
        }
        String line;
        while ((line = reader.readLine()) != null) {
            lineNumber++;
            String[] parts = line.split(" ");
            if (parts.length != Frame.SIZE) {
                throw new ParseException(fileName, lineNumber, String.format("Line contains %d values (expected %d)", parts.length, Frame.SIZE));
            }
            Frame frame = new Frame();
            for(int i = 0; i < parts.length; i++) {
                int channel = i + 1;
                try {
                    int value = Integer.parseInt(parts[i]);
                    frame.setChannelValue(channel, value);
                } catch (NumberFormatException e) {
                    throw new ParseException(fileName, lineNumber, String.format("Value at index %d has non-integer value '%s'", channel, parts[i]));
                } catch (IllegalArgumentException e) {
                    throw new ParseException(fileName, lineNumber, e.getMessage());
                }
            }
            synchronized (frames) {
                frames.add(frame);
            }
        }
    }
}
