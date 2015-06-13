package darkness.generator.api;

import darkness.generator.api.effects.EffectBase;
import darkness.generator.output.BaseOutput;
import darkness.generator.output.ConsoleOutput;
import darkness.generator.output.FileOutput;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ScriptManager {
    private static ScriptManager instance = new ScriptManager();

    private final LinkedList<ScriptContext> scriptContextList = new LinkedList<ScriptContext>();

    private ScriptManager() {
    }

    public static ScriptManager getInstance() { return instance;  }

    public void start(ScriptBase mainScript, BaseOutput output) throws IOException {
        ScriptContext mainScriptContext = new ScriptContext(mainScript);
        scriptContextList.add(mainScriptContext);

        // Start the script
        while (!scriptContextList.isEmpty()) {
            // Start script
            output.beginScript();

            // Make all scripts generate a single frame.
            // We do it in the reverse order to make sure its the main script that will always win the channel value if multiple scripts are working on a channel
            Iterator<ScriptContext> contextIterator = scriptContextList.descendingIterator();
            Set<ScriptContext> contextsToRemove = new HashSet<>();
            while (contextIterator.hasNext()) {
                ScriptContext context = contextIterator.next();
                if (!context.doFrame()) {
                    // The script is done executing. Remove it (but this must be done afterwards because doFrame() might cause scriptContextList to be modified)
                    contextsToRemove.add(context);
                    output.endScript(context.script.getClass().getSimpleName());
                }
            }
            scriptContextList.removeAll(contextsToRemove);

            output.beginFrame();

            ChannelManager.getInstance().dumpChannels(output);

            output.endFrame();
        }

        // Flush output
        output.flush();
    }

    public void registerEffect(EffectBase effect) {
        ScriptContext context = new ScriptContext(effect);
        scriptContextList.add(context);
    }

    public void registerScript(ScriptBase script) {
        ScriptContext context = new ScriptContext(script);
        scriptContextList.add(context);
    }


    private class ScriptContext  {
        public final ScriptBase script;
        public Iterator<Void> yieldIterator;

        public ScriptContext(ScriptBase script) {
            this.script = script;
        }

        /**
         * Makes the script do a frame
         * @return false if the script can be unregistered. (This happens when the script is done executing)
         */
        public boolean doFrame() {
            if(yieldIterator == null) {
                yieldIterator = script.iterator();
            }

            if(yieldIterator.hasNext()) {
                yieldIterator.next();
                return true;
            }
            else {
                return false;
            }

        }
    }
}
