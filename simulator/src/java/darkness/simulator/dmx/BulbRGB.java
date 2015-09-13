package darkness.simulator.dmx;

import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import darkness.simulator.Application;
import darkness.simulator.dmx.Channel;
import darkness.simulator.dmx.ChannelOwner;

import java.awt.*;

/**
 * Created by janosa on 2/19/15.
 */
public class BulbRGB {
    private final Channel channelRed;
    private final Channel channelGreen;
    private final Channel channelBlue;

    private ChannelController controller = new ChannelController();

    private final Geometry geometry;
    private final PointLight light;


    private static int counter = 0;
    BulbRGB(Channel channelRed, Channel channelGreen, Channel channelBlue, Node parentNode, Vector3f localTranslation) {
        this.channelRed = channelRed;
        this.channelRed.setOwner(controller);
        this.channelGreen = channelGreen;
        this.channelGreen.setOwner(controller);
        this.channelBlue = channelBlue;
        this.channelBlue.setOwner(controller);

        Node bulbNode = new Node(toString());
        bulbNode.setLocalTranslation(localTranslation);
        parentNode.attachChild(bulbNode);

        ColorRGBA color = ColorRGBA.Black;
        Sphere sphere = new Sphere(5, 5, 0.05f);

        geometry = new Geometry(toString(), sphere);


        Material mat = new Material(Application.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Front);
        mat.setColor("Color", color);   // set color of material to blue
        geometry.setMaterial(mat);


        light = new PointLight();
        light.setColor(color);
        light.setRadius(0.25f);

        bulbNode.attachChild(geometry);

        //lamp_light.setPosition(new Vector3f(1.0f, 2f, 0.1f));
        light.setPosition(bulbNode.getWorldTranslation());

        //if(++counter == 7) {
            Application.getInstance().getRootNode().addLight(light);
        //}



    }

    public Channel getChannelRed() {
        return channelRed;
    }

    public Channel getChannelGreen() {
        return channelGreen;
    }

    public Channel getChannelBlue() {
        return channelBlue;
    }

    public void set(int red, int green, int blue) {
        channelRed.setValue(red);
        channelGreen.setValue(green);
        channelBlue.setValue(blue);
    }

    public void update() {
        ColorRGBA color = new ColorRGBA();
        color.a = 1.0f;
        color.r = (float)getRed() / 255.0f;
        color.g = (float)getGreen() / 255.0f;
        color.b = (float)getBlue() / 255.0f;

        geometry.getMaterial().setColor("Color", color);
        light.setColor(color);
    }

    public void set(Color color) {
        set(color.getRed(), color.getGreen(), color.getBlue());
    }

    public void set(String hexColor) {
        set(Color.decode(hexColor));
    }

    /**
     * Set a HSB color to this bulb
     * @param hue The floor of this number is subtracted from it to create a fraction between 0 and 1. This fractional number is then multiplied by 360 to produce the hue angle in the HSB color model.
     * @param saturation In the range 0.0..1.0
     * @param brightness In the range 0.0..1.0
     */
    public void setHSB(float hue, float saturation, float brightness) {
        set(Color.getHSBColor(hue, saturation, brightness));
    }

    public Color getColor() {
        return new Color(getRed(), getGreen(), getBlue());
    }

    public int getRed() {
        return channelRed.getValue();
    }

    public int getGreen() {
        return channelGreen.getValue();
    }

    public int getBlue() {
        return channelBlue.getValue();
    }

    @Override
    public String toString() {
        return "Bulb{R:"+getChannelRed()+",G:"+getChannelGreen()+"B:"+getChannelBlue()+"}";
    }

    // TODO wrong naming...
    private class ChannelController implements ChannelOwner {

        @Override
        public void onChannelUpdated(int newValue) {
            update();
        }
    }
}
