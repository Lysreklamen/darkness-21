package darkness.simulator.graphics

import com.jme3.material.Material
import com.jme3.material.RenderState
import com.jme3.math.ColorRGBA
import com.jme3.renderer.queue.RenderQueue
import com.jme3.scene.Geometry
import com.jme3.scene.Node
import com.jme3.texture.Texture2D
import com.jme3.texture.plugins.AWTLoader
import darkness.simulator.Application
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Polygon
import java.awt.geom.Area
import java.awt.image.BufferedImage

var aluminiumGlobalCounter = 0

class Aluminum(outline: List<Point>, holes: List<List<Point>>, closed: Boolean, name: String) : Node(name) {
    init {
        // Calculate the bounding box
        val bb = calcBoundingBox(outline)

        // The small offset depending on the counter is a nasty hack to avoid two textures being on the exact same
        // z offset. This results in some rendering issues
        this.setLocalTranslation(bb[0], bb[1], 0.001f * aluminiumGlobalCounter++)

        // Make all points relative to the bounding box
        for (p in outline) {
            p.x -= bb[0]
            p.y -= bb[1]
        }
        for (hole in holes) {
            for (p in hole) {
                p.x -= bb[0]
                p.y -= bb[1]
            }
        }

        // Define the alu meshes
        addAluMesh(outline, closed, "$name-outline")
        for ((index, path) in holes.withIndex()) {
            addAluMesh(path, true, "$name-hole$index")
        }

        if (closed) {
            // Create a white background for the inside of the alu
            addBackground(bb, outline, holes, "$name-background")
        }
    }

    private fun calcBoundingBox(points: List<Point>): FloatArray {
        var minX = Float.MAX_VALUE
        var minY = Float.MAX_VALUE
        var maxX = -Float.MAX_VALUE
        var maxY = -Float.MAX_VALUE

        for (p in points) {
            if (p.x < minX) {
                minX = p.x
            }
            if (p.y < minY) {
                minY = p.y
            }

            if (p.x > maxX) {
                maxX = p.x
            }
            if (p.y > maxY) {
                maxY = p.y
            }
        }

        return floatArrayOf(minX, minY, maxX - minX, maxY - minY)
    }

    fun addAluMesh(path: List<Point>, closed: Boolean, name: String) {
        val geom = Geometry(name, AluminumMesh(path, closed))


        val coverMat = Material(Application.instance.assetManager, "Common/MatDefs/Light/Lighting.j3md")
        coverMat.setBoolean("UseMaterialColors", true)
        coverMat.setColor("Ambient", ColorRGBA(.2f, .2f, .2f, 1f))
        coverMat.setColor("Diffuse", ColorRGBA.White)
        coverMat.setColor("Specular", ColorRGBA.White)
        coverMat.setFloat("Shininess", 96f)
        geom.setMaterial(coverMat)

        this.attachChild(geom)
    }

    fun addBackground(bb: FloatArray, path: List<Point>, holes: List<List<Point>>, name: String) {
        val width = bb[2]
        val height = bb[3]
        val quad = HighPolyQuad(width, height)
        val geom = Geometry(name, quad)

        // For now we create square background with a textured imag
        // A proper solution would be to triangulate the polygon and create a proper background mesh
        // This could be done with Delaunay triangulations, however as that algorithm does not
        // allow for collinear edges a proper implementation here is a challenge

        val mat = Material(Application.instance.assetManager, "Common/MatDefs/Light/Lighting.j3md")
        mat.setBoolean("UseMaterialColors", true)
        mat.setColor("Ambient", ColorRGBA(.2f, .2f, .2f, 1f))
        mat.setColor("Diffuse", ColorRGBA.White)
        mat.setColor("Specular", ColorRGBA.White)
        mat.setFloat("Shininess", 0.3f)
        mat.additionalRenderState.blendMode = RenderState.BlendMode.Alpha;

        val resolution = 100.0f
        val imageWidth = (width * resolution).toInt()
        val imageHeight = (height * resolution).toInt()
        val bi = BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_4BYTE_ABGR)
        val g2 = bi.graphics as Graphics2D

        g2.color = Color(0, 0, 0, 0)
        g2.fillRect(0, 0, imageWidth, imageHeight)

        // Draw the polygon
        val area = convertToArea(path, holes, resolution)
        g2.color = Color.WHITE
        g2.fill(area)

        val image = AWTLoader().load(bi, false)

        val texture = Texture2D(image)
        mat.setTexture("DiffuseMap", texture)

        geom.material = mat
        geom.queueBucket = RenderQueue.Bucket.Translucent

        this.attachChild(geom)
    }

    private fun convertToPoly(path: List<Point>, resolution: Float): Polygon {
        val xpoints = IntArray(path.size)
        val ypoints = IntArray(path.size)
        for (i in 0 until path.size) {
            xpoints[i] = (path[i].x * resolution).toInt()
            ypoints[i] = (path[i].y * resolution).toInt()
        }
        return Polygon(xpoints, ypoints, path.size)

    }

    private fun convertToArea(path: List<Point>, holes: List<List<Point>>, resolution: Float): Area {
        val area = Area(convertToPoly(path, resolution))
        for (hole in holes) {
            area.subtract(Area(convertToPoly(hole, resolution)))
        }
        return area
    }
}
