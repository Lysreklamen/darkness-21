package darkness.simulator.graphics

import com.jme3.math.Vector3f
import com.jme3.scene.Mesh
import com.jme3.scene.VertexBuffer.Type

class AluminumMesh(perimeter: List<Point>, closed: Boolean) : Mesh() {
    private val width = 0.15f

    init {
        // Each perimeter entry creates four triangles (one quad facing in and one quad facing out),
        // each with three vertices. Sadly, the vertices mostly cannot be shared between adjacent quads because
        // the normal vectors are tied to the vertices. However, we do share vertices between the two
        // triangles that make up a quad. Thus, two quads with four vertices each and three vector components per vertex
        // make a total of 2 * 4 * 3 = 24 vector components per quad pair. The two quads contain two triangles each,
        // which each need three indices for a total of 12 indices per quad pair. Each of the 8 vertices needs
        // a normal vector, also with three components for a total of 24 per quad pair.
        val quadCount = perimeter.size - if (closed) 0 else 1
        val vertexBuffer = FloatArray(quadCount * 24)
        val indexBuffer = IntArray(quadCount * 12)
        val normalBuffer = FloatArray(quadCount * 24)

        for (i in 0 until quadCount) {
            val currentPoint = perimeter[i]
            val nextPoint = perimeter[(i + 1) % perimeter.size]

            for (j in 0..1) {
                // First iteration is the front face, second iteration is the back face

                // Vertices
                val vertexBase = i * 24 + j * 12 // 12 vector components per face
                setVector(vertexBuffer, vertexBase + 0, currentPoint.x, currentPoint.y, 0f)     // Current inner perimeter vertex (close to wall)
                setVector(vertexBuffer, vertexBase + 3, currentPoint.x, currentPoint.y, width) // Current outer perimeter vertex (out from wall)
                setVector(vertexBuffer, vertexBase + 6, nextPoint.x, nextPoint.y, 0f)           // Next inner perimeter vertex
                setVector(vertexBuffer, vertexBase + 9, nextPoint.x, nextPoint.y, width)       // Next outer perimeter vertex

                // Indices of the triangle corners, referencing the vertices
                val vertexIndexBase = vertexBase / 3 // Each vertex consists of 3 components
                val indexBase = vertexBase / 2 // 6 indices per face
                val currentInner = vertexIndexBase + 0 // Index of current inner perimeter vertex
                val currentOuter = vertexIndexBase + 1 // Index of current outer perimeter vertex
                val nextInner = vertexIndexBase + 2    // Index of next inner perimeter vertex
                val nextOuter = vertexIndexBase + 3    // Index of next outer perimeter vertex
                val reversed = j == 1
                setTriangle(indexBuffer, indexBase + 0, intArrayOf(currentInner, currentOuter, nextOuter), reversed)
                setTriangle(indexBuffer, indexBase + 3, intArrayOf(currentInner, nextOuter, nextInner), reversed)

                // Normal vectors
                val normal = getNormal(vertexBuffer, currentInner, currentOuter, nextOuter)
                if (reversed) {
                    normal.negateLocal()
                }
                for (k in 0..3) {
                    setVector(normalBuffer, vertexBase + k * 3, normal)
                }
            }
        }


        setBuffer(Type.Position, 3, vertexBuffer)
        setBuffer(Type.Index, 3, indexBuffer)
        setBuffer(Type.Normal, 3, normalBuffer)

        updateBound()
        setStatic()
    }

    private fun setVector(buffer: FloatArray, baseIndex: Int, v: Vector3f) {
        setVector(buffer, baseIndex, v.x, v.y, v.z)
    }

    private fun setVector(buffer: FloatArray, baseIndex: Int, x: Float, y: Float, z: Float) {
        buffer[baseIndex + 0] = x
        buffer[baseIndex + 1] = y
        buffer[baseIndex + 2] = z
    }

    private fun setTriangle(buffer: IntArray, baseIndex: Int, indices: IntArray, reversed: Boolean) {
        for (i in 0..2) {
            buffer[baseIndex + i] = indices[if (reversed) 2 - i else i]
        }
    }

    private fun getVector(buffer: FloatArray, baseIndex: Int): Vector3f {
        return Vector3f(buffer[baseIndex], buffer[baseIndex + 1], buffer[baseIndex + 2])
    }

    private fun getNormal(vertexBuffer: FloatArray, a: Int, b: Int, c: Int): Vector3f {
        val va = getVector(vertexBuffer, a * 3)
        val vb = getVector(vertexBuffer, b * 3)
        val vc = getVector(vertexBuffer, c * 3)
        val leftSide = vb.subtract(va)
        val rightSide = vc.subtract(vb)
        return leftSide.cross(rightSide).normalizeLocal()
    }
}
