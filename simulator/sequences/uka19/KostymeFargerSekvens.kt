package darkness.generator.scripts.uka19

import darkness.generator.api.BulbGroup
import java.awt.Color
// fargene til kostymene som går rundt over bokstavene

class KostymeFargerSekvens: BaseScript() {
    override suspend fun run() {
        super.run()

        val yellow = Color(236,252,3)
        val redish = Color (201,14, 73)
        val green = Color(53,112,60)
        val blue = Color(29,54,102)
        val turkis = Color(18,119,122)
        val sort = Color(138, 135, 135)
        val white = Color(255, 255, 255)

        val fargeListe = listOf(yellow, redish, green, blue, turkis, sort, white)

        for (startPoint in 0 until letters.size) {
            var colorIndex = startPoint
            for (letter in letters) {
                set (letter, fargeListe[colorIndex])
                colorIndex = (colorIndex - 1 + letters.size) % letters.size
            }
            skip(10)
        }
    }
}