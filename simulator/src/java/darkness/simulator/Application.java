package darkness.simulator;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.jme3.util.TangentBinormalGenerator;
import com.simsilica.lemur.geom.MBox;
import darkness.simulator.dmx.BulbManager;
import darkness.simulator.dmx.BulbRGB;
import darkness.simulator.dmx.ChannelManager;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * The simulator's main class.
 */
public class Application extends SimpleApplication {

    private static Application instance;

    private final Arguments arguments;
    private PgmPlayer player;

    public Application(Arguments arguments) {
        this.arguments = arguments;
        setShowSettings(false);
        AppSettings setting = new AppSettings(true);
        setting.put("Width", 1280);
        setting.put("Height", 720);
        setting.put("Title", "Darkness Simulator");
        setting.put("VSync", true);
        setSettings(setting);
    }

    public static void main(String[] args) throws IOException {
        instance = new Application(parseArguments(args));
        instance.start(); // start the game
    }

    public static Application getInstance() { return instance; }

    @Override
    public void simpleInitApp() {
        // Set up gesimsen
        Node gesimsenLowerLeft = new Node(); // Set up a new coordinate system with respect to the lower left corner (north) of gesimsen
        rootNode.attachChild(gesimsenLowerLeft);

        MBox gesimsenBox = new MBox(10.0f, 1.0f, 0.05f, 100, 10, 1);
        Geometry gesimsenGeometry = new Geometry("Gesimsen", gesimsenBox);


        TangentBinormalGenerator.generate(gesimsenBox);
        Material gesimsenMaterial = new Material(assetManager,
                "Common/MatDefs/Light/Lighting.j3md");

        gesimsenMaterial.setBoolean("UseMaterialColors", true);
        gesimsenMaterial.setColor("Diffuse", ColorRGBA.White);
        gesimsenMaterial.setColor("Specular", ColorRGBA.Black);
        gesimsenMaterial.setFloat("Shininess", 32.f);  // [0,128]

        gesimsenGeometry.setMaterial(gesimsenMaterial);

        gesimsenGeometry.setLocalTranslation(10.0f, 1f, -0.10f);
        gesimsenLowerLeft.attachChild(gesimsenGeometry);

        getFlyByCamera().setMoveSpeed(10.0f);

        try {
            parsePatternFile(arguments.getPatternFileName(), gesimsenLowerLeft);
            List<PgmReader> pgmReaders = new ArrayList<PgmReader>();
            for (String pgmFileName : arguments.getSequenceFileNames()) {
                pgmReaders.add(new PgmReader(pgmFileName));
            }
            player = new PgmPlayer(pgmReaders);
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        player.update();
    }

    private static Arguments parseArguments(String[] args) throws IOException {
        String patternFileName = null;
        List<String> sequenceFileNames = new ArrayList<String>();
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--pattern")) {
                patternFileName = args[++i];
            }
            else if (args[i].equals("--playlist")) {
                String playlist = args[++i];
                sequenceFileNames.addAll(Files.readAllLines(Paths.get(playlist), StandardCharsets.UTF_8));
            }
            else if (args[i].equals("--sequence")) {
                sequenceFileNames.add(args[++i]);
            }
        }
        return new Arguments(patternFileName, sequenceFileNames);
    }

    private static class Arguments {
        private final String patternFileName;
        private final List<String> sequenceFileNames;

        public Arguments(String patternFileName, List<String> sequenceFileNames) {
            this.patternFileName = patternFileName;
            this.sequenceFileNames = sequenceFileNames;
        }

        public String getPatternFileName() {
            return patternFileName;
        }

        public List<String> getSequenceFileNames() {
            return sequenceFileNames;
        }
    }
}
