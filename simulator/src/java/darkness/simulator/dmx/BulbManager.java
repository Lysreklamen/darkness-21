package darkness.simulator.dmx;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

import java.util.Collection;
import java.util.HashMap;
import java.util.NoSuchElementException;

/**
 * Created by janosa on 2/19/15.
 */
public class BulbManager {
    private static BulbManager instance = new BulbManager();

    private final HashMap<Integer, BulbRGB> bulbMap = new HashMap<Integer, BulbRGB>();
    private final HashMap<String, BulbRGB>  bulbNameMap = new HashMap<String, BulbRGB>();

    private BulbManager() {
    }

    public static BulbManager getInstance() { return instance; }

    public BulbRGB getBulb(int id) {
        return bulbMap.get(id);
    }

    public BulbRGB getBulb(String name) {
        return bulbNameMap.get(name);
    }

    /**
     * Registers a bulb with the given integer id
     * @param id
     * @param channelRed
     * @param channelGreen
     * @param channelBlue
     * @return
     */
    public BulbRGB registerBulb(int id, Channel channelRed, Channel channelGreen, Channel channelBlue, Node parentNode, Vector3f localTranslation) {
        if(bulbMap.containsKey(id)) {
            throw new IllegalArgumentException("The bulb with the ID: "+id+" already exists.");
        }

        BulbRGB bulb = new BulbRGB(channelRed, channelGreen, channelBlue, parentNode, localTranslation);
        bulbMap.put(id, bulb);
        return bulb;
    }


    public BulbRGB nameBulb(BulbRGB bulb, String name) {
        if(bulbNameMap.containsKey(name)) {
            throw new IllegalArgumentException("There is already a bulb with the name: "+name);
        }
        bulbNameMap.put(name, bulb);
        return  bulb;
    }

    public BulbRGB nameBulb(int id, String name) {
        if(!bulbMap.containsKey(id)) {
            throw new NoSuchElementException("There is no bulb with the ID: "+id);
        }
        return nameBulb(getBulb(id), name);
    }

    public Collection<BulbRGB> getAllBulbs() {
        return bulbMap.values();
    }
}
