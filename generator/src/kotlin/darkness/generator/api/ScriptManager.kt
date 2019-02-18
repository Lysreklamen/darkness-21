package darkness.generator.api

import darkness.generator.api.effects.EffectBase
import darkness.generator.output.BaseOutput
import java.util.*

object ScriptManager {
    private val scriptContextList = LinkedList<ScriptContext>()

    fun start(mainScript: ScriptBase, output: BaseOutput) {
        val mainScriptContext = ScriptContext(mainScript)
        scriptContextList.add(mainScriptContext)

        // Start the script
        while (!scriptContextList.isEmpty()) {
            // Start script
            output.beginScript()

            // Make all scripts generate a single frame.
            // We do it in the reverse order to make sure its the main script that will always win the channel value if multiple scripts are working on a channel.
            // We make a copy of the script list in order to avoid ConcurrentModificationException (in case the script itself adds another script and because we might remove the script if it's finished).
            val contextsReversed = scriptContextList.reversed()
            for (context in contextsReversed) {
                if (!context.doFrame()) {
                    // The script is done executing, so we'll remove it
                    scriptContextList.remove(context)
                    output.endScript(context.script.javaClass.simpleName)
                }
            }

            output.beginFrame()

            ChannelManager.dumpChannels(output)
            ChannelManager.nextFrame()

            output.endFrame()
        }

        // Flush output
        output.flush()
    }

    fun registerEffect(effect: EffectBase) {
        val context = ScriptContext(effect)
        scriptContextList.add(context)
    }

    fun registerScript(script: ScriptBase) {
        val context = ScriptContext(script)
        scriptContextList.add(context)
    }

    private class ScriptContext(val script: ScriptBase) {
        var yieldIterator: Iterator<Void>? = null

        /**
         * Makes the script do a frame
         * @return false if the script can be unregistered. (This happens when the script is done executing)
         */
        fun doFrame(): Boolean {
            if (yieldIterator == null) {
                yieldIterator = script.iterator()
            }

            return if (yieldIterator!!.hasNext()) {
                yieldIterator!!.next()
                true
            } else {
                false
            }
        }
    }
}
