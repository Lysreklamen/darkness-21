package darkness.generator.scripts.uka19

import darkness.generator.api.BulbRGB
import java.awt.Color
import java.util.*

//

class SlårPåEnOgEnPære: BaseScript() {
    override suspend fun run() {
        super.run()

        val hvit = Color(255, 255, 255)
        val rød = Color(237, 0, 0)
        val turkis = Color(0, 208, 255)
        val gul = Color(246, 255, 0)
        val rosa = Color(255, 0, 247)
        val sort = Color(0,0,0)
        val grønn = Color(0, 240, 8)
        val blå = Color(40, 0, 240)
        val orange = Color(255, 162, 0)
        val lilla = Color(138, 0, 108)

        //v
        set(bulb(0), rød)
        skip(1)
        set(bulb(1), rød)
        skip(1)
        set(bulb(2), rød)
        skip(1)
        set(bulb(3), rød)
        skip(1)
        set(bulb(4), rød)
        skip(1)
        set(bulb(9), rød)
        skip(1)
        set(bulb(5), rød)
        skip(1)
        set(bulb(6), rød)
        skip(1)
        set(bulb(7), rød)
        skip(1)
        set(bulb(8), rød)
        skip(1)

        //i
        set(bulb(10),rosa)
        skip(1)
        set(bulb(11), rosa)
        skip(1)
        set(bulb(12), rosa)
        skip(1)
        set(bulb(13), rosa)
        skip(1)
        set(bulb(14), rosa)
        skip(1)

        //v
        set(bulb(20), grønn)
        skip(1)
        set(bulb(21), grønn)
        skip(1)
        set(bulb(22), grønn)
        skip(1)
        set(bulb(23), grønn)
        skip(1)
        set(bulb(27), grønn)
        skip(1)
        set(bulb(24), grønn)
        skip(1)
        set(bulb(25), grønn)
        skip(1)
        set(bulb(26), grønn)
        skip(1)

        //i
        set(bulb(30), blå)
        skip(1)
        set(bulb(31), blå)
        skip(1)
        set(bulb(32), blå)
        skip(1)
        set(bulb(33), blå)
        skip(1)
        set(bulb(34), blå)
        skip(1)

        //l
        set(bulb(40), orange)
        skip(1)
        set(bulb(41), orange)
        skip(1)
        set(bulb(42), orange)
        skip(1)
        set(bulb(43), orange)
        skip(1)
        set(bulb(44), orange)
        skip(1)
        set(bulb(45), orange)
        skip(1)

        //l
        set(bulb(50), lilla)
        skip(1)
        set(bulb(51), lilla)
        skip(1)
        set(bulb(52), lilla)
        skip(1)
        set(bulb(53), lilla)
        skip(1)
        set(bulb(54), lilla)
        skip(1)

        //e
        set(bulb(67), turkis)
        skip(1)
        set(bulb(66), turkis)
        skip(1)
        set(bulb(65), turkis)
        skip(1)
        set(bulb(64), turkis)
        skip(1)
        set(bulb(63), turkis)
        skip(1)
        set(bulb(62), turkis)
        skip(1)
        set(bulb(61), turkis)
        skip(1)
        set(bulb(60), turkis)
        skip(1)

        set(bulb(68), gul)
        skip(1)
        set(bulb(69), gul)
        skip(1)

        skip(40)


        }
    }
