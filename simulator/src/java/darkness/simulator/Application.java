package darkness.simulator;


import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.FXAAFilter;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;
import com.jme3.util.TangentBinormalGenerator;
import com.simsilica.lemur.geom.MBox;
import darkness.simulator.dmx.BulbManager;
import darkness.simulator.dmx.BulbRGB;
import darkness.simulator.dmx.ChannelManager;

import java.awt.*;
import java.io.*;

/** Sample 1 - how to get started with the most simple JME 3 application.
 * Display a blue 3D cube and view from all sides by
 * moving the mouse and pressing the WASD keys. */
public class Application extends SimpleApplication {

    private static Application instance;

    public static void main(String[] args){
        instance = new Application();
        instance.setShowSettings(false);
        AppSettings setting = new AppSettings(true);
        setting.put("Width", 1280);
        setting.put("Height", 720);
        setting.put("Title", "Darkness Simulator");
        setting.put("VSync", true);

        instance.setSettings(setting);

        instance.start(); // start the game
    }

    public static Application getInstance() { return instance; }

    PgmPlayer player;

    @Override
    public void simpleInitApp() {
        /*
        viewPort.addProcessor(dsp = new DeferredSceneProcessor(this));
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        fpp.addFilter(new BloomFilter(BloomFilter.GlowMode.Scene));
        fpp.addFilter(new FXAAFilter());
        fpp.addFilter(new FXAAFilter());
        viewPort.addProcessor(fpp);
        */
        // Set up gesimsen
        Node gesimsenLowerLeft = new Node(); // Set up a new coordinate system with respect to the lower left corner (north) of gesimsen
        rootNode.attachChild(gesimsenLowerLeft);

        MBox gesimsenBox = new MBox(10.0f, 1.0f, 0.05f, 100, 10, 1);
        Geometry gesimsenGeometry = new Geometry("Gesimsen", gesimsenBox);

        TangentBinormalGenerator.generate(gesimsenBox);
        Material sphereMat = new Material(assetManager,
                "Common/MatDefs/Light/Lighting.j3md");
        //sphereMat.setTexture("DiffuseMap",
        //        assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall.jpg"));
        //sphereMat.setTexture("NormalMap",
        //        assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall_normal.jpg"));
        sphereMat.setBoolean("UseMaterialColors",true);
        sphereMat.setColor("Diffuse",ColorRGBA.White);
        sphereMat.setColor("Specular",ColorRGBA.Black);
        sphereMat.setFloat("Shininess", 32.f);  // [0,128]

        gesimsenGeometry.setMaterial(sphereMat);

        //Material mat = assetManager.loadMaterial("Textures/FlatMaterial.j3m");
        //gesimsenGeometry.setMaterial(mat);
        gesimsenGeometry.setLocalTranslation(10.0f, 1f, -0.10f);
        //gesimsenGeometry.setLocalTranslation(gesimsenBox.getXExtent()/2.0f, gesimsenBox.getYExtent()/2.0f, -gesimsenBox.getZExtent()*2.0f);
        gesimsenLowerLeft.attachChild(gesimsenGeometry);

        getFlyByCamera().setMoveSpeed(10.0f);


        BulbManager bulbManager = BulbManager.getInstance();
        ChannelManager channelManager = ChannelManager.getInstance();

        try {
            parsePatternFile("patterns/skilt-013.txt", gesimsenLowerLeft);
            player = new PgmPlayer("sequences/uka13/uka13.pgm");
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.Start();

        /** Must add a light to make the lit object visible! */

        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(1, 0, -2).normalizeLocal());
        sun.setColor(ColorRGBA.White.mult(0.05f));
        rootNode.addLight(sun);


    }

    public void parsePatternFile(String fileName, Node parentNode) throws IOException {
        File file = new File(fileName);
        FileReader fileReader = new FileReader(file);
        BufferedReader reader = new BufferedReader(fileReader);

        BulbManager bulbManager = BulbManager.getInstance();
        ChannelManager channelManager = ChannelManager.getInstance();

        for(String line = reader.readLine(); line != null; line = reader.readLine()) {
            String[] parts = line.split(" ");
            if(parts.length < 7) {
                System.err.println("Parse error: "+line);
                continue;
            }

            int id = Integer.parseInt(parts[0]);

            float offsetX = 0;
            float offsetY = 0;
            for(int i = 7; i < parts.length; i+= 2) {
                offsetX = (offsetX + Float.parseFloat(parts[i])) / 2;
                offsetY = (offsetY + Float.parseFloat(parts[i+1])) / 2;
            }

            float posX = - 2.0f + (Float.parseFloat(parts[1]) + offsetX) / 5.0f;
            float posY = 3.5f - (Float.parseFloat(parts[2]) + offsetY) / 10.0f ;



            int channelRed = Integer.parseInt(parts[4]);
            int channelGreen = Integer.parseInt(parts[5]);
            int channelBlue = Integer.parseInt(parts[6]);

            BulbRGB bulb = bulbManager.registerBulb(id,
                    channelManager.getChannel(channelRed),
                    channelManager.getChannel(channelGreen),
                    channelManager.getChannel(channelBlue), parentNode, new Vector3f(posX, posY, 0.0f));

            float hue = (posX*posX+posY*posY) / (10.0f*10.0f + 1.0f*1.0f);
            Color color = Color.getHSBColor(hue, 1f, 0.9f);
            bulb.set(color);

        }

        reader.close();
    }

    @Override
    public void simpleUpdate(float tpf) {
        /*BulbManager bulbManager = BulbManager.getInstance();
        for(BulbRGB bulb: bulbManager.getAllBulbs()) {

            float[] hsb = Color.RGBtoHSB(bulb.getRed(), bulb.getGreen(), bulb.getBlue(), null);
            hsb[0] += 1.0f / 50f;
            //bulb.setHSB(0.5f, 0.5f, 0.5f);
            bulb.setHSB(hsb[0], hsb[1], hsb[2]);
        }
        */
        player.update();
    }
}
