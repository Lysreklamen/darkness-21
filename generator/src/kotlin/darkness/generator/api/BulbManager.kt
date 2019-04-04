package darkness.generator.api

import java.util.NoSuchElementException

object BulbManager {
    private val bulbMap = mutableMapOf<Int, BulbRGB>()
    private val bulbNameMap = mutableMapOf<String, BulbRGB>()

    val allBulbs: Collection<BulbRGB>
        get() = bulbMap.values

    fun getBulb(id: Int): BulbRGB {
        return bulbMap.getOrElse(id) { throw IllegalArgumentException("There is no bulb with id $id") }
    }

    fun getBulb(name: String): BulbRGB {
        return bulbNameMap.getOrElse(name) { throw IllegalArgumentException("There is no bulb with name '$name'") }
    }

    /**
     * Registers a bulb with the given integer id
     * @param id
     * @param channelRed
     * @param channelGreen
     * @param channelBlue
     * @return
     */
    fun registerBulb(id: Int, channelRed: Channel, channelGreen: Channel, channelBlue: Channel, posX: Float, posY: Float): BulbRGB {
        if (bulbMap.containsKey(id)) {
            throw IllegalArgumentException("The bulb with the ID: $id already exists.")
        }

        val bulb = BulbRGB(id, channelRed, channelGreen, channelBlue, posX, posY)
        bulbMap[id] = bulb
        return bulb
    }

    fun registerBulb(id: Int, channelRed: Channel, channelGreen: Channel, channelBlue: Channel): BulbRGB {
        if (bulbMap.containsKey(id)) {
            throw IllegalArgumentException("The bulb with the ID: $id already exists.")
        }

        return registerBulb(id, channelRed, channelGreen, channelBlue, -1.0f, -1.0f)
    }

    /**
     * Registers a bulb with the given integer id, universe and channel numbers
     * @param id
     * @param universe
     * @param channelRed
     * @param channelGreen
     * @param channelBlue
     * @return
     */
    fun registerBulb(id: Int, universe: Int, channelRed: Int, channelGreen: Int, channelBlue: Int): BulbRGB {
        if (bulbMap.containsKey(id)) {
            throw IllegalArgumentException("The bulb with the ID: $id already exists.")
        }
        return registerBulb(id, ChannelManager.getChannel(universe, channelRed), ChannelManager.getChannel(universe, channelGreen), ChannelManager.getChannel(universe, channelBlue))
    }

    /**
     * Registers a bulb with the given integer id and channel numbers. The universe is implicitly set to 0
     * @param id
     * @param channelRed
     * @param channelGreen
     * @param channelBlue
     * @return
     */
    fun registerBulb(id: Int, channelRed: Int, channelGreen: Int, channelBlue: Int): BulbRGB {
        if (bulbMap.containsKey(id)) {
            throw IllegalArgumentException("The bulb with the ID: $id already exists.")
        }
        return registerBulb(id, ChannelManager.getChannel(0, channelRed), ChannelManager.getChannel(0, channelGreen), ChannelManager.getChannel(0, channelBlue))
    }

    fun registerBulb(id: Int, channelRed: Int, channelGreen: Int, channelBlue: Int, posX: Float, posY: Float): BulbRGB {
        if (bulbMap.containsKey(id)) {
            throw IllegalArgumentException("The bulb with the ID: $id already exists.")
        }
        return registerBulb(id, ChannelManager.getChannel(0, channelRed), ChannelManager.getChannel(0, channelGreen), ChannelManager.getChannel(0, channelBlue), posX, posY)
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
