package darkness.simulator.graphics;

import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import darkness.simulator.Application;

public class Scene {
	private final Node rootNode;

	public Scene() {
		Application application = Application.getInstance();
		final Node scene = (Node) application.getAssetManager().loadModel("scenes/uka_generic/samfundet.scene");
		this.rootNode = application.getRootNode();
		this.rootNode.attachChild(scene);
		this.rootNode.addLight(new AmbientLight());

		// Move the camera to the crossing, looking up towards gesimsen
		Camera camera = application.getCamera();
		camera.setLocation(new Vector3f(-10.450743f, 2.7112355f, 35.287804f));
		camera.setRotation(new Quaternion(-0.024610115f, 0.9680668f, 0.105350286f, 0.22614653f));
		application.getFlyByCamera().setMoveSpeed(10.0f);

		DirectionalLight sun = new DirectionalLight();
		sun.setDirection(new Vector3f(1, 0, -2).normalizeLocal());
		sun.setColor(ColorRGBA.White.mult(0.05f));
		this.rootNode.addLight(sun);
	}

	public Node getParentNodeForBulbs() {
		Node skiltNode = (Node) rootNode.getChild("skilt");
		return (Node) skiltNode.getChild("top_left");
	}
}
