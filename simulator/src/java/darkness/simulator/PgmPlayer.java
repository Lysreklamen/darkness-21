package darkness.simulator;

import darkness.simulator.dmx.Channel;
import darkness.simulator.dmx.ChannelManager;
import darkness.simulator.dmx.Frame;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class PgmPlayer implements Runnable {
    private final List<PgmReader> readers;
	private final LinkedList<Iterator<Frame>> overlays;
    private final Thread thread;

    private int currentReaderIndex;
    private int currentFrameIndex;
    private long lastUpdateTime = 0;

    private static final long frameDuration = 1000 / 24;

    public PgmPlayer(List<PgmReader> readers) {
        if (readers == null || readers.size() == 0) {
            throw new IllegalArgumentException("readers is null or empty");
        }
        this.readers = readers;
		this.overlays = new LinkedList<Iterator<Frame>>();
        this.thread = new Thread(this, "PgmPlayer Worker");
        this.thread.setDaemon(true);
    }

    public void start() {
        thread.start();
    }

    @Override public void run() {
        for (PgmReader reader : readers) {
            try {
                reader.read();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        long now = System.currentTimeMillis();
        if (now - lastUpdateTime < frameDuration) {
            return;
        }
        lastUpdateTime = now;

        while (true) {
            PgmReader currentReader = readers.get(currentReaderIndex);
            if (currentReader.getFrameCount() == null) {
                System.err.println(String.format("Waiting for header of PGM file '%s' to be loaded", currentReader.getFileName()));
                break;
            }
            if (currentFrameIndex < currentReader.getFrameCount()) {
                Frame currentFrame = currentReader.getFrame(currentFrameIndex);
                if (currentFrame == null) {
                    System.err.println(String.format("Waiting for frame %d of PGM file '%d' to be loaded", currentFrameIndex, currentReader.getFileName()));
                    break;
                } else {
                    display(currentFrame);
                    currentFrameIndex++;
                    break;
                }
            } else {
                currentFrameIndex = 0;
                currentReaderIndex = (currentReaderIndex + 1) % readers.size();
            }
        }

		Iterator<Iterator<Frame>> overlayIterator = overlays.iterator();
		while (overlayIterator.hasNext()) {
			Iterator<Frame> overlay = overlayIterator.next();
			if (overlay.hasNext()) {
				display(overlay.next());
			} else {
				overlayIterator.remove();
			}
		}
    }

	public void addOverlay(PgmReader overlayReader) {
		if (overlayReader.getFrameCount() == null) {
			throw new IllegalArgumentException("An overlay PgmReader must be loaded by the time it is added");
		}
		synchronized (overlays) {
			overlays.add(overlayReader.iterator());
		}
	}

    private void display(Frame frame) {
        for (int i = 1; i <= Frame.SIZE; i++) {
            Channel channel = ChannelManager.getInstance().getChannel(i);
			int channelValue = frame.getChannelValue(i);
            if (channel != null && channelValue != Frame.TRANSPARENT) {
                channel.setValue(frame.getChannelValue(i));
            }
        }
    }
}
