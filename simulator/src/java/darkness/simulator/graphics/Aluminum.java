package darkness.simulator.graphics;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import darkness.simulator.Application;

import java.util.List;

public class Aluminum extends Geometry {
	public Aluminum(List<Float> perimeterX, List<Float> perimeterY, String name) {
		super(name, new AluminumMesh(perimeterX, perimeterY));

		Material coverMat = new Material(Application.getInstance().getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
		coverMat.setBoolean("UseMaterialColors", true);
		coverMat.setColor("Diffuse", ColorRGBA.White);
		coverMat.setColor("Specular", ColorRGBA.White);
		coverMat.setFloat("Shininess", 96f);
		setMaterial(coverMat);
	}
}
