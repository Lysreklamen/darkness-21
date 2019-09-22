package darkness.generator.scripts.uka19

import darkness.generator.api.effects.BitMap
import java.awt.Color

class ScratchBitmap : BaseScript() {
    override suspend fun run() {
        super.run()

	//
	// Make bitmap
	//
	val res_x: Int = 15
	val res_y: Int = 5
	var plane: BitMap = BitMap(allBulbs, res_x, res_y)
	effect(plane)

	//
	// Run effect
	//
	val colors = arrayOf(Color(255, 255, 255), Color(255, 0, 0), Color(0,128,255), Color(128,255,0), Color(255,0,255), Color(255,255,128), Color(255,128,0))
	for (c in colors) {
		for (y_px in 0 until res_y) {
			for (x_px in 0 until res_x) {
        			plane.setPixel(x_px, y_px, c)
        			skip(1)
			}
		}
	}
	plane.cancel()
    }
}
