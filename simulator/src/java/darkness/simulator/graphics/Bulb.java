package darkness.simulator.graphics;

import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import darkness.simulator.Application;

import java.util.List;

public class Bulb extends Node {
	private final Geometry geometry;
	private final PointLight light;

	public Bulb(float positionX, float positionY, List<Float> perimeterX, List<Float> perimeterY,
			Node parentNode, String name) {
		super(name);
		setLocalTranslation(new Vector3f(positionX, positionY, 0));
		parentNode.attachChild(this);

		ColorRGBA color = ColorRGBA.Black;
		Material mat = new Material(Application.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
		mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Front);
		mat.setColor("Color", color);

		Sphere sphere = new Sphere(5, 5, 0.05f);
		this.geometry = new Geometry(toString(), sphere);
		this.geometry.setMaterial(mat);
		// Move the bulb out from the billboard (we don't want to do translate the whole node like this, since that will also shift the aluminum)
		this.geometry.setLocalTranslation(new Vector3f(0, 0, 0.05f));

		attachChild(this.geometry);
		attachChild(new Aluminum(perimeterX, perimeterY, "Aluminum:" + name));

		this.light = new PointLight();
		this.light.setColor(color);
		this.light.setRadius(0.3f);
		this.light.setPosition(getWorldTranslation());
		Application.getInstance().getRootNode().addLight(this.light);
	}

	public void update(int red, int green, int blue) {
		ColorRGBA color = new ColorRGBA(red / 255f, green / 255f, blue / 255f, 1f);
		geometry.getMaterial().setColor("Color", color);
		light.setColor(color);
	}
}
