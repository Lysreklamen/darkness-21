package darkness.simulator.graphics;

import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;

import java.util.List;

public class AluminumMesh extends Mesh {
	private static final float WIDTH = 0.15f;
	
	public AluminumMesh(List<Float> perimeterX, List<Float> perimeterY) {
		if (perimeterX.size() != perimeterY.size()) {
			throw new IllegalArgumentException("Perimeter lists must have same length");
		}
		if (perimeterX.size() == 0) {
			perimeterX.add(-WIDTH);
			perimeterX.add(-WIDTH);
			perimeterX.add(WIDTH);
			perimeterX.add(WIDTH);
			perimeterY.add(-WIDTH);
			perimeterY.add(WIDTH);
			perimeterY.add(WIDTH);
			perimeterY.add(-WIDTH);
		}

		float[] vertexBuffer = new float[perimeterX.size() * 6]; // Each perimeter entry creates an inner and an outer point, each with (x,y,z)
		for (int i = 0; i < perimeterX.size(); i++) {
			int base = i * 6;
			setVector(vertexBuffer, base, perimeterX.get(i), perimeterY.get(i), 0);        // Inner
			setVector(vertexBuffer, base + 3, perimeterX.get(i), perimeterY.get(i), WIDTH); // Outer
		}
		setBuffer(Type.Position, 3, vertexBuffer);

		int[] indexBuffer = new int[perimeterX.size() * 12]; // Each perimeter entry creates four triangles (one quad facing in and one quad facing out), each with three vertices
		float[] normalBuffer = new float[perimeterX.size() * 12];
		for (int i = 0; i < perimeterX.size(); i++) {
			int base = i * 12;
			int currentInner = i * 2;                          // Index of current inner perimeter vertex
			int currentOuter = currentInner + 1;               // Index of current outer perimeter vertex
			int nextInner = ((i + 1) % perimeterX.size()) * 2; // Index of next inner perimeter vertex
			int nextOuter = nextInner + 1;                     // Index of next outer perimeter vertex
			setTriangle(indexBuffer, base + 0, currentInner, currentOuter, nextOuter); // Facing in
			setTriangle(indexBuffer, base + 3, currentInner, nextOuter, nextInner);    // Facing in
			setTriangle(indexBuffer, base + 6, nextOuter, currentOuter, currentInner); // Facing out
			setTriangle(indexBuffer, base + 9, nextInner, nextOuter, currentInner);    // Facing out
			// Note that it doesn't matter whether the normal vector is pointing in or out, so we can use the same normal vector for all four triangles
			Vector3f normal = getNormal(vertexBuffer, currentInner, currentOuter, nextOuter);
			setVector(normalBuffer, base + 0, normal);
			setVector(normalBuffer, base + 3, normal);
			setVector(normalBuffer, base + 6, normal);
			setVector(normalBuffer, base + 9, normal);
		}
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

	private void setTriangle(int[] buffer, int baseIndex, int a, int b, int c) {
		buffer[baseIndex + 0] = a;
		buffer[baseIndex + 1] = b;
		buffer[baseIndex + 2] = c;
	}

	private Vector3f getVector(float[] buffer, int index) {
		return new Vector3f(buffer[index], buffer[index + 1], buffer[index + 2]);
	}

	private Vector3f getNormal(float[] vertexBuffer, int a, int b, int c) {
		Vector3f va = getVector(vertexBuffer, a);
		Vector3f vb = getVector(vertexBuffer, b);
		Vector3f vc = getVector(vertexBuffer, c);
		Vector3f rightSide = vb.subtract(va);
		Vector3f leftSide = vc.subtract(va);
		return rightSide.cross(leftSide);
	}
}
