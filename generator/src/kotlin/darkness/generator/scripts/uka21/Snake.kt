package darkness.generator.scripts.uka21

import darkness.generator.api.effects.PointRainbow
import kotlin.math.sin
import java.awt.Color

class Snake : BaseScript() {
    override suspend fun run() {
        super.run()

        set(allBulbsGroup, uke_lilla)
        skip(20)

        for (letter in letters) {
            set(letter, uke_turkis)
            skip(5)
        }

        set(allBulbsGroup, uke_lilla)
        skip(20)
        var i = 0
        for (bulb in allBulbsGroup) {
            var bulb_nr1 = i - 4*1 
            var bulb_nr2 = i - 4*2
            var bulb_nr3 = i - 4*3
            set(bulb, uke_gull)
            if (bulb_nr1 > 0) {
                set(allBulbs[bulb_nr1], uke_blå)
            }
            if (bulb_nr2 > 0) {
                set(allBulbs[bulb_nr2], uke_gull)
            }
            if (bulb_nr3 > 0) {
                set(allBulbs[bulb_nr3], uke_blå)
                set(allBulbs[0], uke_blå)
            }
            skip(1)
            i = i + 1
        }

        i = allBulbs.size
        for (bulb in allBulbsGroup) {
            var bulb_nr1 = i + 4*1 
            var bulb_nr2 = i + 4*2
            var bulb_nr3 = i + 4*3
            set(bulb, uke_gull)
            if (bulb_nr1 < allBulbs.size) {
                set(allBulbs[bulb_nr1], uke_blå)
            }
            if (bulb_nr2 < allBulbs.size) {
                set(allBulbs[bulb_nr2], uke_gull)
            }
            if (bulb_nr3 < allBulbs.size) {
                set(allBulbs[bulb_nr3], uke_blå)
                //set(allBulbs[0], uke_blå)
            }
            skip(1)
            i = i - 1
        }

        rgbFade(allBulbsGroup, uke_gull,5)
        skip(10)
        rgbFade(allBulbsGroup, uke_blå,5)
        skip(10)
        rgbFade(allBulbsGroup, uke_gull,5)
        skip(10)
        rgbFade(allBulbsGroup, uke_blå,5)
        skip(10)
        rgbFade(allBulbsGroup, Color.black, 50)

    }
}

