package darkness.generator.api;

import darkness.generator.api.effects.EffectBase;
import darkness.generator.output.ConsoleOutput;
import darkness.generator.output.FileOutput;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by janosa on 2/20/15.
 */
public class ScriptManager {
    private static ScriptManager instance = new ScriptManager();

    private final LinkedList<ScriptContext> scriptContextList = new LinkedList<ScriptContext>();

    private ScriptManager() {
    }

    public static ScriptManager getInstance() { return instance;  }

    public void Start(ScriptBase mainScript) {
        ScriptContext mainScriptContext = new ScriptContext(mainScript);
        scriptContextList.add(mainScriptContext);

        ConsoleOutput output = new ConsoleOutput();
        //FileOutput output = new FileOutput( "test.txt" );

        // Start the script
        while(!scriptContextList.isEmpty()) {
            // Start script
            output.beginScript();

            // Make all scripts generate a single frame.
            // We do it in the reverse order to make sure its the main script that will always win the channel value if multiple scripts are working on a channel
            Iterator<ScriptContext> scriptContextIterator = scriptContextList.descendingIterator();
            while(scriptContextIterator.hasNext()) {
                ScriptContext sctx = scriptContextIterator.next();
                if(!sctx.doFrame()) {
                    // The script is done executing. Remove it
                    scriptContextIterator.remove();
                    output.endScript( sctx.script.getClass().getSimpleName() );
                }
            }

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
