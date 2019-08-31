package darkness.generator

import darkness.generator.api.BulbManager
import darkness.generator.api.ScriptManager
import darkness.generator.output.PgmOutput
import darkness.generator.scripts.uka19.AuroraDemo
import kotlinx.coroutines.runBlocking

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        println("Darkness sequence generator started")

        for (i in 0..69) {
            BulbManager.registerBulb(i, 200 + i, 300 + i, 400 + i)
        }
        for (i in 101..114) {
            BulbManager.registerBulb(i, i - 100, i - 50, i)
        }

        PgmOutput("demo.pgm").use { output ->
            runBlocking {
                ScriptManager.start(AuroraDemo(), output)
            }
        }
    }
}
