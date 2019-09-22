package darkness.generator.scripts.uka19

import darkness.generator.api.BulbGroup

class RGBTest: BaseScript() {
    override fun run() {
        super.run()

	for (letter in letters) {
        	set(letter, 255, 0, 0)
	}
	skip(50)
        for (letter in letters) {
		set(letter, 0, 255, 0)
	}
	skip(50)
        for (letter in letters) {
		set(letter, 0, 0, 255)
	}
	skip(50)
    }
}
