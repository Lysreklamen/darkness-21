package darkness.generator.api

import java.awt.Color
import java.util.*
import java.util.function.Consumer
import java.lang.IllegalArgumentException

/**
 * A group of one or more bulbs, that can mostly be treated as one "big" bulb through the [BulbSet] interface.
 *
 * Constructor creates a bulb group that consists of the given bulbs.
 */
class BulbGroup(val allBulbs: List<BulbRGB>) : BulbSet, Iterable<BulbRGB> {
    val numBulbs: Int

    constructor(vararg bulbs: BulbRGB) : this(bulbs.toList())

    /** The red component of the first bulb in the group. */
    override val red: Int
        get() = allBulbs[0].red

    /** The green component of the first bulb in the group. */
    override val green: Int
        get() = allBulbs[0].green

    /** The blue component of the first bulb in the group. */
    override val blue: Int
        get() = allBulbs[0].blue

    /** The RGB color of the first bulb in the group. */
    override val color: Color
        get() = allBulbs[0].color

    override val position: FloatArray
        get() {
            val averagePosition = floatArrayOf(0.0f, 0.0f, 0.0f)
            var count = 0
            for (bulb in allBulbs) {
                val pos = bulb.position
                averagePosition[0] += pos[0]
                averagePosition[1] += pos[1]
                averagePosition[2] += pos[2]
                count++
            }

            averagePosition[0] /= count.toFloat()
            averagePosition[1] /= count.toFloat()
            averagePosition[2] /= count.toFloat()
            return averagePosition

        }

    init {
        if (allBulbs.isEmpty()) {
            throw IllegalArgumentException("bulbs must be non-null and contain elements")
        }
        this.numBulbs = allBulbs.size
    }

    /**
     * Get a single bulb
     * @param idx Bulb index
     * @return
     */
    fun getBulb(idx: Int): BulbRGB {
        return allBulbs[idx]
    }

    /**
     * Set all bulbs in the group to the given RGB color.
     */
    override fun set(red: Int, green: Int, blue: Int, setter: ScriptBase) {
        for (bulb in allBulbs) {
            bulb[red, green, blue] = setter
        }
    }

    /**
     * Set all bulbs in the group to the given RGB color.
     */
    override fun set(color: Color, setter: ScriptBase) {
        for (bulb in allBulbs) {
            bulb[color] = setter
        }
    }

    /**
     * Set all bulbs in the group to the given HSB color.
     */
    override fun setHSB(hue: Float, saturation: Float, brightness: Float, setter: ScriptBase) {
        for (bulb in allBulbs) {
            bulb.setHSB(hue, saturation, brightness, setter)
        }
    }

    override fun relinquish(setter: ScriptBase) {
        for (bulb in allBulbs) {
            bulb.relinquish(setter)
        }
    }

    /**
     * @return A string describing the channels and current values of the bulbs in this group.
     */
    override fun toString(): String {
        val sb = StringBuilder("BulbSet{")
        for (bulb in allBulbs) {
            sb.append(bulb)
            sb.append(',')
        }
        sb.setCharAt(sb.length - 1, '}') // Overwrite trailing comma
        return sb.toString()
    }

    override fun iterator(): Iterator<BulbRGB> {
        return allBulbs.iterator()
    }

    override fun forEach(action: Consumer<in BulbRGB>) {
        allBulbs.forEach(action)
    }

    override fun spliterator(): Spliterator<BulbRGB> {
        return allBulbs.spliterator()
    }
}
