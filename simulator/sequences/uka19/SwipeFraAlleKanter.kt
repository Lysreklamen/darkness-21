package darkness.generator.scripts.uka19

import darkness.generator.api.BulbRGB
import java.awt.Color
import java.util.*

//

class SwipeFraAlleKanter: BaseScript() {
    override suspend fun run() {
        super.run()

        val hvit = Color(255, 255, 255)
        val rød = Color(237, 0, 0)
        val turkis = Color(0, 208, 255)
        val gul = Color(246, 255, 0)
        val rosa = Color(255, 0, 247)

        set(A, hvit)
        set(B, hvit)
        set(C, hvit)
        set(D, hvit)
        set(E, hvit)
        set(F, hvit)
        set(G, hvit)
        skip(10)

        val bokstavListe = listOf(A,B,C,D,E,F,G)

        for (bokstav in bokstavListe) {
            set(bokstav, turkis)
            skip(1)
        }
        skip(10)
        set(D, rosa)
        skip(2)
        set(C, rosa)
        set(E, rosa)
        skip(2)
        set(B, rosa)
        set(F, rosa)
        skip(2)
        set(A, rosa)
        set(G,rosa)
        skip(2)
        skip(10)

        for(i in bokstavListe.size-1 downTo 0){
           var letter = bokstavListe[i]
            set(letter, gul)
            skip(2)
        }

        skip(10)
        set(G, hvit)
        set(A, hvit)
        skip(2)
        set(F, hvit)
        set(B, hvit)
        skip(2)
        set(C, hvit)
        set(E, hvit)
        skip(2)
        set(D, hvit)
        skip(2)
        skip(10)
}
}