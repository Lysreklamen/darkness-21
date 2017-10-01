package darkness.generator.api;

import darkness.generator.api.effects.EffectBase;
import darkness.generator.output.BaseOutput;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
            // We do it in the reverse order to make sure its the main script that will always win the channel value if multiple scripts are working on a channel.
            // We make a copy of the script list in order to avoid ConcurrentModificationException (in case the script itself adds another script and because we might remove the script if it's finished).
            List<ScriptContext> contextsReversed = new ArrayList<>(scriptContextList);
            Collections.reverse(contextsReversed);
            for (ScriptContext context : contextsReversed) {
                if (!context.doFrame()) {
                    // The script is done executing, so we'll remove it
                    scriptContextList.remove(context);
                    output.endScript(context.script.getClass().getSimpleName());
                }
            }

            output.beginFrame();

            ChannelManager.getInstance().dumpChannels(output);
            ChannelManager.getInstance().nextFrame();

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
