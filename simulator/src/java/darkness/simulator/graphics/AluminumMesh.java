package darkness.simulator.graphics;

import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;

import java.util.List;

public class AluminumMesh extends Mesh {
	private static final float WIDTH = 0.15f;
	
	public AluminumMesh(List<Point> perimeter, boolean closed) {
		// Each perimeter entry creates four triangles (one quad facing in and one quad facing out),
		// each with three vertices. Sadly, the vertices mostly cannot be shared between adjacent quads because
		// the normal vectors are tied to the vertices. However, we do share vertices between the two
		// triangles that make up a quad. Thus, two quads with four vertices each and three vector components per vertex
		// make a total of 2 * 4 * 3 = 24 vector components per quad pair. The two quads contain two triangles each,
		// which each need three indices for a total of 12 indices per quad pair. Each of the 8 vertices needs
		// a normal vector, also with three components for a total of 24 per quad pair.
		int quadCount = perimeter.size() - (closed ? 0 : 1);
		float[] vertexBuffer = new float[quadCount * 24];
		int[] indexBuffer = new int[quadCount * 12];
		float[] normalBuffer = new float[quadCount * 24];

		for (int i = 0; i < quadCount; i++) {
			Point currentPoint = perimeter.get(i);
			Point nextPoint = perimeter.get((i + 1) % perimeter.size());

			for (int j = 0; j < 2; j++) {
				// First iteration is the front face, second iteration is the back face

				// Vertices
				int vertexBase = i * 24 + j * 12; // 12 vector components per face
				setVector(vertexBuffer, vertexBase + 0, currentPoint.x, currentPoint.y, 0);     // Current inner perimeter vertex (close to wall)
				setVector(vertexBuffer, vertexBase + 3, currentPoint.x, currentPoint.y, WIDTH); // Current outer perimeter vertex (out from wall)
				setVector(vertexBuffer, vertexBase + 6, nextPoint.x, nextPoint.y, 0);           // Next inner perimeter vertex
				setVector(vertexBuffer, vertexBase + 9, nextPoint.x, nextPoint.y, WIDTH);       // Next outer perimeter vertex

				// Indices of the triangle corners, referencing the vertices
				int vertexIndexBase = vertexBase / 3; // Each vertex consists of 3 components
				int indexBase = vertexBase / 2; // 6 indices per face
				int currentInner = vertexIndexBase + 0; // Index of current inner perimeter vertex
				int currentOuter = vertexIndexBase + 1; // Index of current outer perimeter vertex
				int nextInner = vertexIndexBase + 2;    // Index of next inner perimeter vertex
				int nextOuter = vertexIndexBase + 3;    // Index of next outer perimeter vertex
				boolean reversed = j == 1;
				setTriangle(indexBuffer, indexBase + 0, new int[]{currentInner, currentOuter, nextOuter}, reversed);
				setTriangle(indexBuffer, indexBase + 3, new int[]{currentInner, nextOuter, nextInner}, reversed);

				// Normal vectors
				Vector3f normal = getNormal(vertexBuffer, currentInner, currentOuter, nextOuter);
				if (reversed) {
					normal.negateLocal();
				}
				for (int k = 0; k < 4; k++) {
					setVector(normalBuffer, vertexBase + k * 3, normal);
				}
			}
		}


		setBuffer(Type.Position, 3, vertexBuffer);
		setBuffer(Type.Index, 3, indexBuffer);
		setBuffer(Type.Normal, 3, normalBuffer);

		updateBound();
		setStatic();
	}

	private void setVector(float[] buffer, int baseIndex, Vector3f v) {
		setVector(buffer, baseIndex, v.x, v.y, v.z);
	}

	private void setVector(float[] buffer, int baseIndex, float x, float y, float z) {
		buffer[baseIndex + 0] = x;
		buffer[baseIndex + 1] = y;
		buffer[baseIndex + 2] = z;
	}

	private void setTriangle(int[] buffer, int baseIndex, int[] indices, boolean reversed) {
		for (int i = 0; i < 3; i++) {
			buffer[baseIndex + i] = indices[reversed ? 2 - i : i];
		}
	}

	private Vector3f getVector(float[] buffer, int baseIndex) {
		return new Vector3f(buffer[baseIndex], buffer[baseIndex + 1], buffer[baseIndex + 2]);
	}

	private Vector3f getNormal(float[] vertexBuffer, int a, int b, int c) {
		Vector3f va = getVector(vertexBuffer, a * 3);
		Vector3f vb = getVector(vertexBuffer, b * 3);
		Vector3f vc = getVector(vertexBuffer, c * 3);
		Vector3f leftSide = vb.subtract(va);
		Vector3f rightSide = vc.subtract(vb);
		return leftSide.cross(rightSide).normalizeLocal();
	}
}
