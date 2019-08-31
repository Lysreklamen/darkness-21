package darkness.generator.scripts.uka19

import darkness.generator.api.BulbGroup
import darkness.generator.api.BulbRGB
import darkness.generator.api.ScriptBase

open class BaseScript : ScriptBase() {
    protected lateinit var A: BulbGroup
    protected lateinit var B: BulbGroup
    protected lateinit var C: BulbGroup
    protected lateinit var D: BulbGroup
    protected lateinit var E: BulbGroup
    protected lateinit var F: BulbGroup
    protected lateinit var G: BulbGroup
    protected lateinit var letters: List<BulbGroup>
    protected lateinit var allBulbs: List<BulbRGB>

    override fun run() {
        A = group(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
        B = group(10, 11, 12, 13, 14)
        C = group(20, 21, 22, 23, 24, 25, 26, 27)
        D = group(30, 31, 32, 33, 34)
        E = group(40, 41, 42, 43, 44, 45)
        F = group(50, 51, 52, 53, 54)
        G = group(60, 61, 62, 63, 64, 65, 66, 67, 68, 69)
        letters = listOf(A, B, C, D, E, F, G)

        allBulbs = letters.flatMap { letter -> letter.allBulbs }
    }
}
