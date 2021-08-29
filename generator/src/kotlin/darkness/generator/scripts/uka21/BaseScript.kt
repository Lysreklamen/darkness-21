package darkness.generator.scripts.uka21

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
    protected lateinit var H: BulbGroup
    protected lateinit var I: BulbGroup
    protected lateinit var J: BulbGroup
    protected lateinit var letters: List<BulbGroup>
    protected lateinit var allBulbs: List<BulbRGB>
    protected lateinit var allBulbsGroup: BulbGroup

    override suspend fun run() {
        A = group(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13)
        B = group(21, 22, 23, 24, 25, 26, 27)
        C = group(31, 32, 33, 34, 35, 36, 37, 38)
        D = group(41, 42, 43, 44, 45, 46, 47, 48)
        E = group(51, 52, 53, 54, 55, 56, 57, 58, 59)
        F = group(61, 62, 63, 64, 65, 66, 67)
        G = group(71, 72, 73, 74, 75, 76)
        H = group(81, 82, 83, 84, 85, 86, 87, 88)
        I = group(91, 92, 93, 94, 95, 96, 97, 98)
        J = group(101, 102, 103, 104, 105, 106, 107, 108)
        letters = listOf(A, B, C, D, E, F, G, H, I, J)

        allBulbs = letters.flatMap { letter -> letter.allBulbs }
        allBulbsGroup = BulbGroup(allBulbs)

    }
}
