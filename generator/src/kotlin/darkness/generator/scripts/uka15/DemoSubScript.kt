package darkness.generator.scripts.uka15

import darkness.generator.api.BulbRGB

class DemoSubScript : BaseScript() {
    override suspend fun run() {
        super.run()
        for (letter in letters) {
            for (bulb in letter) {
                set(bulb, 255, 0, 0)
                next()
            }
        }
    }
}
