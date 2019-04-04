package darkness.simulator.graphics

import com.jme3.light.AmbientLight
import com.jme3.light.DirectionalLight
import com.jme3.math.ColorRGBA
import com.jme3.math.Quaternion
import com.jme3.math.Vector3f
import com.jme3.scene.Node
import darkness.simulator.Application

class Scene {
    private val rootNode: Node

    val parentNodeForBulbs: Node
        get() {
            val skiltNode = rootNode.getChild("skilt") as Node
            return skiltNode.getChild("top_left") as Node
        }

    init {
        val application = Application.instance
        val scene = application.assetManager.loadModel("scenes/uka_generic/samfundet.scene") as Node
        this.rootNode = application.rootNode
        this.rootNode.attachChild(scene)
        this.rootNode.addLight(AmbientLight())

        // Move the camera to the crossing, looking up towards gesimsen
        val camera = application.camera

        // From the street, looking up
        // camera.setLocation(new Vector3f(-10.450743f, 2.7112355f, 35.287804f));
        // camera.setRotation(new Quaternion(-0.024610115f, 0.9680668f, 0.105350286f, 0.22614653f));

        // Close up, more usable
        camera.location = Vector3f(-0.3624857f, 9.472308f, 25.097445f)
        camera.rotation = Quaternion(-0.0001166f, 0.9978704f, 0.06520383f, 0.0017843109f)

        application.flyByCamera.moveSpeed = 10.0f

        val sun = DirectionalLight()
        sun.direction = Vector3f(1f, 0f, -2f).normalizeLocal()
        sun.color = ColorRGBA.White.mult(0.05f)
        this.rootNode.addLight(sun)
    }
}
