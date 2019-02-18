package darkness.generator

import darkness.generator.api.BulbManager
import darkness.generator.api.ScriptManager
import darkness.generator.output.PgmOutput
import darkness.generator.scripts.uka13.DemoScript

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        println("Darkness sequence generator started")

        for (i in 0..140) {
            BulbManager.registerBulb(i, 50 + i, 200 + i, 350 + i)
        }

        PgmOutput("demo.pgm").use { output ->
            ScriptManager.start(DemoScript(), output)
        }
    }
}
