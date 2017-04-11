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
        File file = new File(fileName);
        FileReader fileReader = new FileReader(file);
        BufferedReader reader = new BufferedReader(fileReader);

        BulbManager bulbManager = BulbManager.getInstance();
        darkness.generator.api.BulbManager generatorBulbManager = darkness.generator.api.BulbManager.getInstance();
        ChannelManager channelManager = ChannelManager.getInstance();

        float offsetX = 0;
        float offsetY = 0;
        float scaleX = 1;
        float scaleY = 1;

        int lineNumber = 0;
        for(String line = reader.readLine(); line != null; line = reader.readLine()) {
            lineNumber++;
            try {
                String[] parts = line.split(" ");
                String maybeInstruction = parts[0].toUpperCase();
                if (maybeInstruction.equals("OFFSET")) {
                    offsetX = Float.parseFloat(parts[1]);
                    offsetY = Float.parseFloat(parts[2]);
                    continue;
                }
                if (maybeInstruction.equals("SCALE")) {
                    scaleX = Float.parseFloat(parts[1]);
                    scaleY = Float.parseFloat(parts[2]);
                    continue;
                }
                if (parts.length < 7 || parts.length % 2 != 1) {
                    System.err.println("Parse error: " + line);
                    continue;
                }

                int id = Integer.parseInt(parts[0]);

                float positionX = (Float.parseFloat(parts[1]) - offsetX) * RENDER_SCALE * scaleX - RENDER_OFFSET_X;
                float positionY = RENDER_OFFSET_Y - (Float.parseFloat(parts[2]) - offsetY) * RENDER_SCALE * scaleY;

                int channelRed = Integer.parseInt(parts[4]);
                int channelGreen = Integer.parseInt(parts[5]);
                int channelBlue = Integer.parseInt(parts[6]);

                List<Float> perimeterX = new ArrayList<>();
                List<Float> perimeterY = new ArrayList<>();
                for (int i = 7; i < parts.length; i += 2) {
                    perimeterX.add(Float.parseFloat(parts[i]) * RENDER_SCALE * scaleX);
                    perimeterY.add(Float.parseFloat(parts[i + 1]) * RENDER_SCALE * scaleY);
                }

                BulbRGB bulb = bulbManager.registerBulb(id,
                        channelManager.getChannel(channelRed),
                        channelManager.getChannel(channelGreen),
                        channelManager.getChannel(channelBlue),
                        positionX, positionY,
                        perimeterX, perimeterY,
                        parentNode);
                if (arguments.getScriptClassName() != null) {
                    // If we want to use the generator, its bulb manager must also be populated
                    generatorBulbManager.registerBulb(id, channelRed, channelGreen, channelBlue, positionX, positionY);
                }

                float hue = (positionX * positionX + positionY * positionY) / (10.0f * 10.0f + 1.0f * 1.0f);
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
        String qualifiedScriptClassName = (arguments.getScriptClassName().contains(".") ? "" : "darkness.generator.scripts.uka15.") + arguments.getScriptClassName();
        ScriptBase script = (ScriptBase) Class.forName(qualifiedScriptClassName).newInstance();
        File tempFile = new File("sequences/" + arguments.getScriptClassName() + ".pgm");
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
