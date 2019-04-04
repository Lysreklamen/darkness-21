package darkness.simulator.dmx

import com.jme3.scene.Node
import darkness.simulator.graphics.Point
import java.util.NoSuchElementException

object BulbManager {
    private val bulbMap = mutableMapOf<Int, BulbRGB>()
    private val bulbNameMap = mutableMapOf<String, BulbRGB>()

    val allBulbs: Collection<BulbRGB>
        get() = bulbMap.values

    fun getBulb(id: Int): BulbRGB {
        return bulbMap[id]!!
    }

    fun getBulb(name: String): BulbRGB {
        return bulbNameMap[name]!!
    }

    /**
     * Creates a bulb with the given integer id and registers it
     */
    fun registerBulb(id: Int, channelRed: Channel, channelGreen: Channel, channelBlue: Channel,
                     position: Point, parentNode: Node): BulbRGB {
        if (bulbMap.containsKey(id)) {
            throw IllegalArgumentException("The bulb with the ID $id already exists.")
        }

        val bulb = BulbRGB(channelRed, channelGreen, channelBlue, position, parentNode)
        bulbMap[id] = bulb
        return bulb
    }


    fun nameBulb(bulb: BulbRGB, name: String): BulbRGB {
        if (bulbNameMap.containsKey(name)) {
            throw IllegalArgumentException("There is already a bulb with the name: $name")
        }
        bulbNameMap[name] = bulb
        return bulb
    }

    fun nameBulb(id: Int, name: String): BulbRGB {
        if (!bulbMap.containsKey(id)) {
            throw NoSuchElementException("There is no bulb with the ID: $id")
        }
        return nameBulb(getBulb(id), name)
    }
}
