package darkness.generator.scripts.uka19

import darkness.generator.api.BulbGroup

class Heart : BaseScript() {
    override suspend fun run() {
        super.run()

        for (letter in letters) {
            set(letter, 255, 196, 0)
        }

        set(C, 100, 0, 0)
        for (i in 1..10) {
            skip(3)
            rgbFade(C, 255, 0, 0, 4)
            rgbFade(B, 255, 0, 0, 12)
            rgbFade(D, 255, 0, 0, 6)
            skip(5)
            rgbFade(E, 255, 0, 0, 3)
            rgbFade(C, 150, 0, 0, 4)
            skip(4)
	    skip(1)
            rgbFade(C, 200, 0, 0, 4)
            rgbFade(D, 255, 128, 0, 6)
            rgbFade(A, 255, 0, 0, 12)
            rgbFade(F, 255, 0, 0, 6)
            skip(5)
            rgbFade(C, 100, 0, 0, 25)
            rgbFade(B, 255, 128, 0, 6)
            rgbFade(E, 255, 128, 0, 6)
            rgbFade(G, 255, 0, 0, 6)
            skip(2)
            skip(3)
            rgbFade(F, 255, 128, 0, 6)
            skip(5)
            rgbFade(G, 255, 128, 0, 6)
            rgbFade(A, 255, 128, 0, 6)
            skip(5)
            skip(5)
            skip(10)
            skip(15)
            skip(3)
        }
    }
}
