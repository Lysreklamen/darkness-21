package darkness.generator.api.effects

import darkness.generator.api.ScriptBase

abstract class EffectBase : ScriptBase() {
    abstract override fun run()
    abstract override fun toString(): String
}
