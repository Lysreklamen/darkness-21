package darkness.simulator;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import darkness.generator.api.ScriptBase;
import darkness.generator.api.ScriptManager;
import darkness.generator.output.PgmOutput;
import darkness.simulator.dmx.BulbManager;
import darkness.simulator.dmx.BulbRGB;
import darkness.simulator.dmx.ChannelManager;
import darkness.simulator.graphics.Aluminum;
import darkness.simulator.graphics.Point;
import darkness.simulator.graphics.Scene;

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

    // These constants are rather uninteresting with their current values,
    // but we'll keep them in case we need to shift things around at some point
    private static final float RENDER_SCALE = 10f / 10f; // Render size: 10 x 2 graphics units; real size: 10 x 2 m(?)
    private static final float RENDER_OFFSET_X = 0;
    private static  final float RENDER_OFFSET_Y = 0;

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
        Scene scene = new Scene();
        try {
            parsePatternFile(arguments.getPatternFileName(), scene.getParentNodeForBulbs());
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
        File file;
        try {
            file = new File(fileName);
        }
        catch(NullPointerException ex) {
            System.err.println("Pattern file '" + fileName + "' does not exist!");
            throw ex; // TODO: Actually handle exception
        }

        FileReader fileReader = new FileReader(file);
        BufferedReader reader = new BufferedReader(fileReader);

        BulbManager bulbManager = BulbManager.getInstance();
        darkness.generator.api.BulbManager generatorBulbManager = darkness.generator.api.BulbManager.getInstance();
        ChannelManager channelManager = ChannelManager.getInstance();

        Point offset = new Point(0, 0);
        Point scale = new Point(1, 1); // Not really a point, but it consists of one float for x and one for y, so let's reuse the class

        int lineNumber = 0;
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            lineNumber++;
            try {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split("[ \\t]+");
                String maybeInstruction = parts[0].toUpperCase();
                if (maybeInstruction.equals("OFFSET")) {
                    offset = new Point(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]));
                    continue;
                }
                if (maybeInstruction.equals("SCALE")) {
                    scale = new Point(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]));
                    continue;
                }
                if (maybeInstruction.equals("ALU") || maybeInstruction.equals("ALUOPEN")) {
                    List<Point> perimeter = new ArrayList<>();
                    for (int i = 1; i < parts.length; i += 2) {
                        perimeter.add(parsePoint(parts[i], parts[i + 1], offset, scale));
                    }
                    boolean closed = maybeInstruction.equals("ALU");
                    parentNode.attachChild(new Aluminum(perimeter, closed, "Aluminum:" + lineNumber));
                    continue;
                }
                if (parts.length < 7 || parts.length % 2 != 1) {
                    System.err.println("Parse error on line " + lineNumber + ": " + line);
                    continue;
                }

                int id = Integer.parseInt(parts[0]);
                Point position = parsePoint(parts[1], parts[2], offset, scale);
                int channelRed = Integer.parseInt(parts[4]);
                int channelGreen = Integer.parseInt(parts[5]);
                int channelBlue = Integer.parseInt(parts[6]);

                BulbRGB bulb = bulbManager.registerBulb(id,
                        channelManager.getChannel(channelRed),
                        channelManager.getChannel(channelGreen),
                        channelManager.getChannel(channelBlue),
                        position,
                        parentNode);
                if (arguments.getScriptClassName() != null) {
                    // If we want to use the generator, its bulb manager must also be populated
                    generatorBulbManager.registerBulb(id, channelRed, channelGreen, channelBlue, position.x, position.y);
                }

                // Default color in case no sequence or script is supplied
                float hue = (position.x * position.x + position.y * position.y) / (10.0f * 10.0f + 1.0f * 1.0f);
                Color color = Color.getHSBColor(hue, 1f, 0.9f);
                bulb.set(color);
            }
            catch (Exception ex) {
                // Rethrow the exception but write the line number first
                System.err.println("An exception occurred while parsing line " + lineNumber + " of the pattern file.");
                throw  ex;
            }
        }

        reader.close();
    }

    @Override
    public void simpleUpdate(float tpf) {
        player.update();
    }

    private PgmReader generatePgmFromScript() throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException {
        String qualifiedScriptClassName = (arguments.getScriptClassName().contains(".") ? "" : "darkness.generator.scripts.uka17.") + arguments.getScriptClassName();
        ScriptBase script = (ScriptBase) Class.forName(qualifiedScriptClassName).newInstance();
        File tempFile = new File("sequences/uka17/" + arguments.getScriptClassName() + ".pgm");
        ScriptManager scriptManager = ScriptManager.getInstance();
        scriptManager.start(script, new PgmOutput(tempFile.getPath()));
        return new PgmReader(tempFile.getPath());
    }

    private static Point parsePoint(String xStr, String yStr, Point offset, Point scale) {
        float x = (Float.parseFloat(xStr.replaceAll("[,;()]", "")) - offset.x) * RENDER_SCALE * scale.x - RENDER_OFFSET_X;
        float y = RENDER_OFFSET_Y - (Float.parseFloat(yStr.replaceAll("[,;()]", "")) - offset.y) * RENDER_SCALE * scale.y;
        return new Point(x, y);
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
