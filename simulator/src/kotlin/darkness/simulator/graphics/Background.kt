package darkness.simulator.graphics

import com.jme3.material.Material
import com.jme3.material.RenderState
import com.jme3.math.ColorRGBA
import com.jme3.renderer.queue.RenderQueue
import com.jme3.scene.Geometry
import com.jme3.scene.shape.Quad
import darkness.simulator.Application

class Background(x: Float, y: Float, width: Float, height: Float, texture: String) : Geometry("Background", Quad(width, height, true)) {

    init {

        this.setLocalTranslation(x, y, -0.01f)

        val mat = Material(Application.instance.assetManager, "Common/MatDefs/Light/Lighting.j3md")

        mat.setBoolean("UseMaterialColors", true)
        mat.setColor("Ambient", ColorRGBA(.2f, .2f, .2f, 1f))
        mat.setColor("Diffuse", ColorRGBA.White)
        mat.setColor("Specular", ColorRGBA.White)
        mat.setFloat("Shininess", 0.3f)
        mat.additionalRenderState.blendMode = RenderState.BlendMode.Alpha;

        if (!texture.isBlank()) {
            val cube1Tex = Application.instance.assetManager.loadTexture(
                texture)
            mat.setTexture("DiffuseMap", cube1Tex)
        } else {
            mat.setColor("Diffuse", ColorRGBA.DarkGray)
        }


        this.material = mat
        this.queueBucket = RenderQueue.Bucket.Transparent


    }
}
