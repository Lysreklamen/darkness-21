package darkness.generator.scripts.uka19

import darkness.generator.api.BulbRGB
import java.awt.Color
import java.util.*

//

class FadeOverFargerFort: BaseScript() {
    override suspend fun run() {
        super.run()

        val gul = Color(246, 255, 0)
        val gul2 = Color(140, 255, 0)
        val grønn1 = Color(77, 255, 0)
        val grønn2 = Color(0, 255, 51)
        val grønn3 = Color(0, 255, 145)
        val blå1 = Color(0, 255, 221)
        val blå2 = Color(0, 225, 255)

        val blå3 = Color(0, 106, 255)
        val blå4 = Color(38, 0, 255)
        val lilla1 = Color(119, 0, 255)
        val lilla2 = Color(191, 0, 255)
        val rosa1 = Color(225, 0, 255)
        val rosa2 = Color(255, 0, 234)
        val rosa3 = Color(255, 0, 162)

        val BokstaverListe = listOf(A,B,C,D,E,F,G)
        val fargeListe1 = listOf(gul, gul2, grønn1, grønn2, grønn3, blå1, blå2)
        val fargeListe2 = listOf(blå3, blå4, lilla1, lilla2, rosa1, rosa2, rosa3)

        for(farge in fargeListe1){
            for(bokstav in BokstaverListe){
            set(bokstav, farge)
            skip(1)
            }

        }
        for(farge in fargeListe2) {
            for (bokstav in BokstaverListe) {
                set(bokstav, farge)
                skip(1)
            }
        }

        }
    }
