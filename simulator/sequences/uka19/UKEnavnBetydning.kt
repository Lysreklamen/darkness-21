package darkness.generator.scripts.uka19

import darkness.generator.api.BulbRGB
import java.awt.Color
import java.util.*

//

class UKEnavnBetydning: BaseScript() {
    override suspend fun run() {
        super.run()

        val hvit = Color(255, 255, 255)
        val rød = Color(237, 0, 0)
        val turkis = Color(0, 208, 255)
        val gul = Color(246, 255, 0)
        val rosa = Color(255, 0, 247)
        val sort = Color(0,0,0)

        //vi-vil-le

        set(A, hvit)
        set(B, hvit)
        skip(10)
        set(A, sort)
        set(B, sort)
        set(C, hvit)
        set(D, hvit)
        set(E, hvit)
        skip(10)
        set(C, sort)
        set(D, sort)
        set(E, sort)
        set(F, hvit)
        set(G, hvit)
        skip(10)

        //viville
        set(A, gul)
        set(B, gul)
        set(C, gul)
        set(D, gul)
        set(E, gul)
        set(F, gul)
        set(G, gul)
        skip(30)

        //vi-ville
        set(A, rosa)
        set(B, rosa)
        skip(10)
        set(C, turkis)
        set(D, turkis)
        set(E, turkis)
        set(F, turkis)
        set(bulb(67), turkis)
        set(bulb(66), turkis)
        set(bulb(65), turkis)
        set(bulb(64), turkis)
        set(bulb(63), turkis)
        set(bulb(62), turkis)
        set(bulb(61), turkis)
        set(bulb(60), turkis)
        skip(30)


        }
    }
