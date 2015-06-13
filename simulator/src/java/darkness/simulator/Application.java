package darkness.simulator;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import darkness.generator.api.ScriptBase;
import darkness.generator.api.ScriptManager;
import darkness.generator.output.PgmOutput;
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
		setPauseOnLostFocus(false);
    }

    public static void main(String[] args) throws IOException {
        instance = new Application(parseArguments(args));
        instance.start(); // start the game
    }

    public static Application getInstance() { return instance; }

    @Override
    public void simpleInitApp() {
        // Load samfundet scene
        final Node scene = (Node) assetManager.loadModel("scenes/uka_generic/samfundet.scene");

        // attach to root node
        rootNode.attachChild(scene);

        rootNode.addLight(new AmbientLight());
        //rootNode.addLight(new PointLight());

        // Move the camera to the crossing, looking up towards gesimsen
        cam.setLocation(new Vector3f(-10.450743f, 2.7112355f, 35.287804f));
        cam.setRotation(new Quaternion(-0.024610115f, 0.9680668f, 0.105350286f, 0.22614653f));
        getFlyByCamera().setMoveSpeed(10.0f);

        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(1, 0, -2).normalizeLocal());
        sun.setColor(ColorRGBA.White.mult(0.05f));
        rootNode.addLight(sun);

        Node skiltNode = (Node)rootNode.getChild("skilt");
        Node skiltBottomLeft = (Node) skiltNode.getChild("bottom_left");

        try {
            parsePatternFile(arguments.getPatternFileName(), skiltBottomLeft);
            List<PgmReader> pgmReaders = new ArrayList<PgmReader>();
            if (arguments.getScriptClassName() != null) {
                pgmReaders.add(generatePgmFromScript());
            }
            for (String pgmFileName : arguments.getSequenceFileNames()) {
                pgmReaders.add(new PgmReader(pgmFileName));
            }
            player = new PgmPlayer(pgmReaders);
            player.start();
			new CommandSocket(player).start();
        } catch (IOException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    public void parsePatternFile(String fileName, Node parentNode) throws IOException {
        File file = new File(fileName);
        FileReader fileReader = new FileReader(file);
        BufferedReader reader = new BufferedReader(fileReader);

        BulbManager bulbManager = BulbManager.getInstance();
        darkness.generator.api.BulbManager generatorBulbManager = darkness.generator.api.BulbManager.getInstance();
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

            float posX = (Float.parseFloat(parts[1]) + offsetX - 16.0f) / 9.0f;
            float posY = 3.4f - (Float.parseFloat(parts[2]) + offsetY) / 10.0f ;



            int channelRed = Integer.parseInt(parts[4]);
            int channelGreen = Integer.parseInt(parts[5]);
            int channelBlue = Integer.parseInt(parts[6]);

            BulbRGB bulb = bulbManager.registerBulb(id,
                    channelManager.getChannel(channelRed),
                    channelManager.getChannel(channelGreen),
                    channelManager.getChannel(channelBlue), parentNode, new Vector3f(posX, posY, 0.0f));
            if (arguments.getScriptClassName() != null) {
                // If we want to use the generator, its bulb manager must also be populated
                generatorBulbManager.registerBulb(id, channelRed, channelGreen, channelBlue);
            }

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

    private PgmReader generatePgmFromScript() throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException {
        String qualifiedScriptClassName = (arguments.getScriptClassName().contains(".") ? "" : "darkness.generator.scripts.uka15.") + arguments.getScriptClassName();
        ScriptBase script = (ScriptBase) Class.forName(qualifiedScriptClassName).newInstance();
        File tempFile = File.createTempFile("darkness-sequence-" + qualifiedScriptClassName + "-", ".pgm");
        tempFile.deleteOnExit();
        ScriptManager scriptManager = ScriptManager.getInstance();
        scriptManager.start(script, new PgmOutput(tempFile.getPath()));
        return new PgmReader(tempFile.getPath());
    }

    private static Arguments parseArguments(String[] args) throws IOException {
        String patternFileName = null;
        List<String> sequenceFileNames = new ArrayList<String>();
        String scriptClassName = null;
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
            } else if (args[i].equals("--script")) {
                scriptClassName = args[++i];
            }
        }
        return new Arguments(patternFileName, sequenceFileNames, scriptClassName);
    }

    private static class Arguments {
        private final String patternFileName;
        private final List<String> sequenceFileNames;
        private final String scriptClassName;

        public Arguments(String patternFileName, List<String> sequenceFileNames, String scriptClassName) {
            this.patternFileName = patternFileName;
            this.sequenceFileNames = sequenceFileNames;
            this.scriptClassName = scriptClassName;
        }

        public String getPatternFileName() {
            return patternFileName;
        }

        public List<String> getSequenceFileNames() {
            return sequenceFileNames;
        }

        public String getScriptClassName() {
            return scriptClassName;
        }
    }
}
