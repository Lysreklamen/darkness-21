package darkness.simulator

import darkness.simulator.dmx.Frame

import java.io.BufferedReader
import java.io.FileReader
import java.util.ArrayList

class ParseException(fileName: String, lineNumber: Int, message: String)
    : Exception(String.format("Parse error in '%s', line %d: %s", fileName, lineNumber, message))

class PgmReader(val fileName: String) : Iterable<Frame> {
    private val frames = ArrayList<Frame>()
    /** The number of frames in the file. Is zero until [read] has been called and the headers have been read.  */
    var frameCount = 0
        private set
    private var supportsTransparency: Boolean = false
    private lateinit var reader: BufferedReader
    private var lineNumber = 0

    fun read() {
        try {
            reader = BufferedReader(FileReader(fileName))
            readHeaders()
            readFrames()
        } catch (t: Throwable) {
            // Handle files with errors gracefully by pretending that they're empty
            frameCount = 0
            synchronized(frames) {
                frames.clear()
            }
        } finally {
            if (::reader.isInitialized) {
                reader.close()
            }
        }
    }

    fun getFrame(index: Int): Frame? {
        synchronized(frames) {
            return if (index >= 0 && index < frames.size) frames[index] else null
        }
    }

    override fun iterator(): Iterator<Frame> {
        return PgmFrameIterator()
    }

    private fun readHeaders() {
        val magicHeader = reader.readLine()
        if (magicHeader != "P2") {
            throw ParseException(fileName, 1, "Invalid magic header")
        }

        val dimensions = reader.readLine().split(" ").dropLastWhile { it.isEmpty() }
        if (dimensions.size != 2) {
            throw ParseException(fileName, 2, "Invalid dimension header")
        }
        frameCount = Integer.parseInt(dimensions[1])

        val maxValue = reader.readLine()
        if (!(maxValue == "255" || maxValue == "256")) {
            throw ParseException(fileName, 3, "Unsupported max value header")
        }
        supportsTransparency = maxValue == "256"
        lineNumber = 3
    }

    private fun readFrames() {
        synchronized(frames) {
            frames.clear()
        }
        var line: String? = reader.readLine()
        while (line != null) {
            lineNumber++
            val parts = line.split(" ").dropLastWhile { it.isEmpty() }
            if (parts.size != Frame.size) {
                throw ParseException(fileName, lineNumber, String.format("Line contains %d values (expected %d)", parts.size, Frame.size))
            }
            val frame = Frame(supportsTransparency)
            for (i in parts.indices) {
                val channel = i + 1
                try {
                    val value = Integer.parseInt(parts[i])
                    frame.setChannelValue(channel, value)
                } catch (e: NumberFormatException) {
                    throw ParseException(fileName, lineNumber, String.format("Value at index %d has non-integer value '%s'", channel, parts[i]))
                } catch (e: IllegalArgumentException) {
                    throw ParseException(fileName, lineNumber, e.message!!)
                }

            }
            synchronized(frames) {
                frames.add(frame)
            }
            line = reader.readLine()
        }
    }

    private inner class PgmFrameIterator : Iterator<Frame> {
        private var frameIndex: Int = 0

        init {
            if (frameCount == 0) {
                throw IllegalStateException(
                    "Can't create a PgmFrameIterator for a PGM file that is empty or not loaded")
            }
        }

        override fun hasNext(): Boolean {
            return frameIndex < frameCount
        }

        override fun next(): Frame {
            return frames[frameIndex++]
        }
    }
}
