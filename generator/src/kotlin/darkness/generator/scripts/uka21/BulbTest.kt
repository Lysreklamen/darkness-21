package darkness.generator.scripts.uka21

import java.awt.Color

class BulbTest : BaseScript() {
    override suspend fun run() {
        super.run()
        
        for(bulb in allBulbs){
            set(bulb, 255, 0, 0)
            skip(2)
            set(bulb, 0, 255, 0)
            skip(2)
            set(bulb, 0, 0, 255)
            skip(2)
            set(bulb, 0, 0, 0)
            skip(2)
        }

    }
}