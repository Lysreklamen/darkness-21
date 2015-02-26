package darkness.simulator;

import darkness.simulator.dmx.Channel;
import darkness.simulator.dmx.ChannelManager;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by janosa on 2/23/15.
 */
public class PgmPlayer implements Runnable {
    private final String fileName;
    private BufferedReader reader;
    private final int maxLoadedFrames = 40;
    private final int headerLineSize = 3;

    // The number of frames in the file
    private  int frames = 0;
    // Holds the current frame being viewed now
    private int currentFrameNumber = 0;
    private int lastViewedFrame = 0;
    private int lineNumber = 0;

    private LinkedList<Integer> loadedFrameNumbers = new LinkedList<Integer>();
    private HashMap<Integer, FrameContainer> loadedFrames = new HashMap<Integer, FrameContainer>();

    public PgmPlayer(String fileName) throws FileNotFoundException {
        this.fileName = fileName;
        reader = new BufferedReader(new FileReader(new File(fileName)));
    }

    public void Start() {
        Thread thread = new Thread(this, "PgmPlayer Worker");
        thread.setDaemon(true);
        thread.start();
    }


    @Override
    public void run() {
        try {
            if(!readHeader()) {
                return;
            }

            while(true) {
                loadFrames();

                Thread.sleep(1000/24);
                currentFrameNumber++;
                if(currentFrameNumber == frames) {
                    currentFrameNumber = 0;
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        int frame = currentFrameNumber;
        if(lastViewedFrame == frame) {
            return; // We have already shown this frame, so no need to update
        }


        FrameContainer container = loadedFrames.get(frame);
        if(container == null) {
            System.err.println("PgmReader is lagging behind!!! :/");
            return;
        }



        ChannelManager channelManager = ChannelManager.getInstance();
        synchronized (container) {
            for(int i = 0; i < 512; i++) {
                Channel channel = channelManager.getChannel(i+1);
                if(channel != null) {
                    channel.setValue(container.channelValues[i]);
                }
            }
        }

        lastViewedFrame = frame;
    }

    private void loadFrames() throws IOException {
        // We want to have +- maxLoadedFrames in memory at all times
        for(int frameNumber = currentFrameNumber-maxLoadedFrames/2; frameNumber < currentFrameNumber+maxLoadedFrames/2; frameNumber++) {
            if(frameNumber < 0) {
                // TODO load end of file
                continue; // There is no negative frame numbers...
            }
            if(frameNumber >= frames) {
                // TODO load start of file
                continue;
            }
            if(loadedFrames.containsKey(frameNumber)) {
                continue;
            }
            // Already loaded. Great...

            // We do not have this frame in memory, lets load it
            if(frameNumber+headerLineSize < lineNumber) {
                // TODO reload the file
                System.err.println("Should reload! Want frame "+frameNumber+". We are on line: "+lineNumber);
                return;
            }

            String line = null;
            do {
                line = reader.readLine();
                lineNumber++;
            } while(lineNumber != frameNumber+headerLineSize && lineNumber < frames+headerLineSize);


            if(line == null) {
                System.err.println("Could not find frame: "+frameNumber+" we are now on line: "+lineNumber);
            }

            FrameContainer container = null;
            // Now lets get a slot for the frame
            /*if(loadedFrameNumbers.size() > maxLoadedFrames) {
                int replaceFrameNumber = loadedFrameNumbers.getFirst();
                container = loadedFrames.remove(replaceFrameNumber);
            }
            else {*/
                container = new FrameContainer();
            //}

            synchronized (container) {
                container.frameNumber = frameNumber;
                // We got the line representing the frame... Great
                String[] parts = line.split(" ");
                for(int i = 0; i < 512 && i < parts.length; i++) {
                    int val = Integer.parseInt(parts[i]);
                    container.channelValues[i] = val;
                }

                loadedFrames.put(frameNumber, container);
                loadedFrameNumbers.addLast(frameNumber);
            }

            System.out.println("Loaded frame: "+frameNumber);

        }
    }

    private boolean readHeader() throws IOException {
        // Read header
        String header = reader.readLine();
        if(!header.equals("P2")) {
            System.err.println("The PGM file: "+fileName+" is not in the correct format");
            return false;
        }
        // Skip the next two lines. We don't need them
        String header2 = reader.readLine();
        String[] header2Parts = header2.split(" ");
        if(header2Parts.length == 2) {
            frames = Integer.parseInt(header2Parts[1]);
        }
        reader.readLine();
        lineNumber = headerLineSize-1;

        return true;
    }

    private class FrameContainer {
        public int frameNumber = -1;
        public int[] channelValues = new int[512];
    }


}
