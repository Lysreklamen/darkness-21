package darkness.generator.scripts.uka19

import darkness.generator.api.BulbRGB
import java.awt.Color
import java.util.*

//

class FadeColorFade: BaseScript() {
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

        set(A, gul)
        set(B, gul2)
        set(C, grønn1)
        set(D, grønn2)
        set(E, grønn3)
        set(F, blå1)
        set(G, blå2)
        skip(10)

        rgbFade(A, gul, blå3, 2)
        rgbFade(B, gul2, blå4, 2)
        rgbFade(C, grønn1, lilla1, 2)
        rgbFade(D, grønn2, lilla2, 2)
        rgbFade(E, grønn3, rosa1, 2)
        rgbFade(F, blå1, rosa2, 2)
        rgbFade(G, blå2, rosa3, 2)
        skip(10)

        rgbFade(A, blå3, gul,2)
        rgbFade(B,  blå4, gul2,2)
        rgbFade(C,  lilla1, grønn1, 2)
        rgbFade(D,  lilla2, grønn2,2)
        rgbFade(E,  rosa1, grønn3, 2)
        rgbFade(F,  rosa2, blå1,2)
        rgbFade(G,  rosa3, blå2,2)

        }
    }
