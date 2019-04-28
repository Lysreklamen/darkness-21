package darkness.simulator.graphics

import com.jme3.light.PointLight
import com.jme3.material.Material
import com.jme3.material.RenderState
import com.jme3.math.ColorRGBA
import com.jme3.math.Vector3f
import com.jme3.scene.Geometry
import com.jme3.scene.Node
import com.jme3.scene.shape.Sphere
import darkness.simulator.Application

class Bulb(position: Point, parentNode: Node, name: String) : Node(name) {
    private val geometry: Geometry
    private val light: PointLight

    init {
        localTranslation = Vector3f(position.x, position.y, 0f)
        parentNode.attachChild(this)

        val color = ColorRGBA.Black
        val mat = Material(Application.instance.assetManager, "Common/MatDefs/Misc/Unshaded.j3md")  // create a simple material
        mat.additionalRenderState.faceCullMode = RenderState.FaceCullMode.Front
        mat.setColor("Color", color)

        val sphere = Sphere(5, 5, 0.04f)
        this.geometry = Geometry(toString(), sphere)
        this.geometry.material = mat
        // Move the bulb out from the billboard (we don't want to do translate the whole node like this, since that will also shift the aluminum)
        this.geometry.localTranslation = Vector3f(0f, 0f, 0.05f)
        attachChild(this.geometry)

        this.light = PointLight()
        this.light.color = color
        this.light.radius = 0.5f
        this.light.position = worldTranslation.add(Vector3f(0.0f, 0.0f, 0.05f))
        Application.instance.rootNode.addLight(this.light)
    }

    fun update(red: Int, green: Int, blue: Int) {
        val color = ColorRGBA(red / 255f, green / 255f, blue / 255f, 1f)
        geometry.material.setColor("Color", color)
        light.color = color
    }
}
