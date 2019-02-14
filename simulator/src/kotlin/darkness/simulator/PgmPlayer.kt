package darkness.simulator

import darkness.simulator.dmx.ChannelManager
import darkness.simulator.dmx.Frame
import java.io.IOException
import java.util.*

class PgmPlayer(private val readers: List<PgmReader>) : Runnable {
    private val overlays: LinkedList<Iterator<Frame>>
    private val thread: Thread
    private val frameDuration: Long = 1000 / 20

    private var currentReaderIndex: Int = 0
    private var currentFrameIndex: Int = 0
    private var lastUpdateTime: Long = 0

    init {
        if (readers.isEmpty()) {
            throw IllegalArgumentException("readers is null or empty")
        }
        this.overlays = LinkedList()
        this.thread = Thread(this, "PgmPlayer Worker")
        this.thread.isDaemon = true
    }

    fun start() {
        thread.start()
    }

    override fun run() {
        for (reader in readers) {
            try {
                reader.read()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
    }

    fun update() {
        val now = System.currentTimeMillis()
        if (now - lastUpdateTime < frameDuration) {
            return
        }
        lastUpdateTime = now

        while (true) {
            val currentReader = readers[currentReaderIndex]
            if (currentReader.frameCount == 0) {
                System.err.println(String.format("Waiting for header of PGM file '%s' to be loaded", currentReader.fileName))
                break
            }
            if (currentFrameIndex < currentReader.frameCount) {
                val currentFrame = currentReader.getFrame(currentFrameIndex)
                if (currentFrame == null) {
                    System.err.println(String.format("Waiting for frame %d of PGM file '%s' to be loaded", currentFrameIndex, currentReader.fileName))
                    break
                } else {
                    display(currentFrame)
                    currentFrameIndex++
                    break
                }
            } else {
                currentFrameIndex = 0
                currentReaderIndex = (currentReaderIndex + 1) % readers.size
            }
        }

        val overlayIterator = overlays.iterator()
        while (overlayIterator.hasNext()) {
            val overlay = overlayIterator.next()
            if (overlay.hasNext()) {
                display(overlay.next())
            } else {
                overlayIterator.remove()
            }
        }
    }

    fun addOverlay(overlayReader: PgmReader) {
        if (overlayReader.frameCount == 0) {
            throw IllegalArgumentException("An overlay PgmReader must be loaded by the time it is added")
        }
        synchronized(overlays) {
            overlays.add(overlayReader.iterator())
        }
    }

    private fun display(frame: Frame) {
        for (i in 1..Frame.size) {
            val channel = ChannelManager.instance.getChannel(i)
            val channelValue = frame.getChannelValue(i)
            if (channelValue != Frame.transparent) {
                channel.value = frame.getChannelValue(i)
            }
        }
    }
}
