package darkness.generator.scripts.uka19

import java.awt.Color
// hvite bokstaver, bokstav C blinker rødt

class HjerteBank: BaseScript() {
    override suspend fun run() {
        super.run()

        set(C, 255, 0, 0)
        set(A, 255, 255, 255)
        set(B, 255, 255, 255)
        set(D, 255, 255, 255)
        set(E, 255, 255, 255)
        set(F, 255, 255, 255)
        set(G, 255, 255, 255)

        skip(10)
        set(C, 191, 0, 0)

        skip(5)
        set(C, 120, 0, 0)

        skip(5)

    }
}