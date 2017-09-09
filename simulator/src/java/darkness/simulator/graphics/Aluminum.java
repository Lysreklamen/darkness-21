package darkness.simulator.graphics;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import darkness.simulator.Application;

import java.util.List;

public class Aluminum extends Geometry {
	public Aluminum(List<Point> perimeter, boolean closed, String name) {
		super(name, new AluminumMesh(perimeter, closed));

		Material coverMat = new Material(Application.getInstance().getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
		coverMat.setBoolean("UseMaterialColors", true);
		coverMat.setColor("Ambient", new ColorRGBA(.2f, .2f, .2f, 1));
		coverMat.setColor("Diffuse", ColorRGBA.White);
		coverMat.setColor("Specular", ColorRGBA.White);
		coverMat.setFloat("Shininess", 96f);
		setMaterial(coverMat);
	}
}
