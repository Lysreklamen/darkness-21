package darkness.simulator.graphics

import com.jme3.scene.Mesh
import com.jme3.scene.VertexBuffer.Type
import java.lang.Math.min

class HighPolyQuad(width: Float, height: Float, quadSize: Float = 0.1f) : Mesh() {

    init {
        // Split into a grid of quads
        val columnCount = Math.ceil((width/quadSize).toDouble()).toInt()+1
        val rowCount = Math.ceil((height/quadSize).toDouble()).toInt()+1


        val quadCount = columnCount*rowCount
        val vertexCount = (columnCount + 1)*(rowCount + 1)
        val vertexBuffer = FloatArray(vertexCount * 3) // There are 3 components per vertex
        val normalBuffer = FloatArray(vertexCount * 3) // Each vertex has its own (but equal) normal angle
        val textureBuffer = FloatArray(vertexCount * 2) // Each vertex has a texture mapping

        val indexBuffer = IntArray(quadCount * 6) // Each quad consists of 2 triangles, each requiring 3 indexes

        // Define all the vertices
        for (y in 0..rowCount) {
            val y0 = min(height, y*quadSize)
            for (x in 0..columnCount) {
                val x0 = min(width, x*quadSize)

                // Define the vertex
                val vertexIndex = (y*columnCount+x)
                val vertexBase = vertexIndex*3
                vertexBuffer[vertexBase + 0] = x0
                vertexBuffer[vertexBase + 1] = y0
                vertexBuffer[vertexBase + 2] = 0.0f

                // Define the normal for the vertex
                normalBuffer[vertexBase + 0] = 0.0f
                normalBuffer[vertexBase + 1] = 0.0f
                normalBuffer[vertexBase + 2] = 1.0f

                // Define the texture mapping for the vertex
                val textureBaseIndex = vertexIndex*2
                textureBuffer[textureBaseIndex + 0] = (x0/width)
                textureBuffer[textureBaseIndex + 1] = (y0/height)
            }
        }

        // Define the triangles for each quad

        // Split into rows
        for (y in 0 until rowCount) {
            // Split into columns
            for (x in 0 until columnCount) {
                val indexBase = 6*(y*columnCount+x)

                val bottomLeftIndex = y*columnCount+x
                val bottomRightIndex = bottomLeftIndex+1
                val topLeftIndex = (y+1)*columnCount+x
                val topRightIndex = topLeftIndex+1

                // First triangle - Top left - bottom left - bottom right
                indexBuffer[indexBase + 0] = topLeftIndex
                indexBuffer[indexBase + 1] = bottomLeftIndex
                indexBuffer[indexBase + 2] = bottomRightIndex

                // Second triangle - bottom right - top right - top left
                indexBuffer[indexBase + 3] = bottomRightIndex
                indexBuffer[indexBase + 4] = topRightIndex
                indexBuffer[indexBase + 5] = topLeftIndex

            }
        }


        setBuffer(Type.Position, 3, vertexBuffer)
        setBuffer(Type.Index, 3, indexBuffer)
        setBuffer(Type.Normal, 3, normalBuffer)
        setBuffer(Type.TexCoord, 2, textureBuffer)

        updateBound()
        setStatic()
    }

}
