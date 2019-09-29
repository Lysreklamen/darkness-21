package darkness.generator.scripts.uka19

import darkness.generator.api.BulbRGB
import java.awt.Color
import java.util.*

//

class KlubbenSofa: BaseScript() {
    override suspend fun run() {
        super.run()

        val lilla = Color(125, 0, 115)
        val gul = Color(224, 240, 0)

        val letterList = listOf(A,B,C,D,E,F,G)

        set(A, lilla)
        set(G, lilla)
        skip(10)
        set(A, gul)
        set(B, lilla)
        set(G, gul)
        set(F, lilla)
        skip(10)
        set(E, lilla)
        set(C, lilla)
        set(B, gul)
        set(F, gul)
        set(G, lilla)
        set(A, lilla)
        skip(10)

        for (i in 0..10) {
            set(A, gul)
            set(B, lilla)
            set(C, gul)
            set(D, lilla)
            set(E, gul)
            set(F, lilla)
            set(G, gul)
            skip(10)

            set(A, lilla)
            set(B, gul)
            set(C, lilla)
            set(D, gul)
            set(E, lilla)
            set(F, gul)
            set(G, lilla)
            skip(10)

        }
    }
}