package darkness.simulator.graphics

import com.jme3.material.Material
import com.jme3.math.ColorRGBA
import com.jme3.scene.Geometry
import darkness.simulator.Application

class Aluminum(perimeter: List<Point>, closed: Boolean, name: String) : Geometry(name, AluminumMesh(perimeter, closed)) {
    init {
        val coverMat = Material(Application.instance.assetManager, "Common/MatDefs/Light/Lighting.j3md")
        coverMat.setBoolean("UseMaterialColors", true)
        coverMat.setColor("Ambient", ColorRGBA(.2f, .2f, .2f, 1f))
        coverMat.setColor("Diffuse", ColorRGBA.White)
        coverMat.setColor("Specular", ColorRGBA.White)
        coverMat.setFloat("Shininess", 96f)
        setMaterial(coverMat)
    }
}
