package darkness.generator.scripts.uka19

import java.awt.Color
import java.util.*

// bokstavene blinker i tilfeldig farge

class IkkeVeldigTilfeldigBlinkish: BaseScript() {
    override suspend fun run() {
        super.run()


for (i in 0..4) {
    var random = Random(i.toLong())
    for (letter in letters) {
        set(letter, random.nextInt(255), random.nextInt(255), random.nextInt(255))
    }
    skip(1)
}
}
}