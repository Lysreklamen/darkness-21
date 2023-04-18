package darkness.generator.scripts.uka21

import java.awt.Color
import java.util.Random

class LetterColorDance : BaseScript() {
    override suspend fun run() {
        super.run()
        val rnd = Random(1337)
        val startFargeListe = listOf(uke_blå, uke_gull, uke_lilla, uke_rød, uke_turkis)
        val delay = 5
        val fade = 2
        for (i in 0..startFargeListe.size - 1) {
            for (letter in letters) {
                val new_idx = rnd.nextInt(startFargeListe.size)
                rgbFade(letter, startFargeListe[new_idx], fade)
                skip(delay)
            }
        }
        skip(10)
        rgbFade(allBulbsGroup, Color.black, 10)
    }
}
