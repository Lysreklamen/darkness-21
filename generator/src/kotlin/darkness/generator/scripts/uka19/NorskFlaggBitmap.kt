package darkness.generator.scripts.uka19

import darkness.generator.api.effects.BitMap
import java.awt.Color
import java.lang.Math

class NorskFlaggBitmap : BaseScript() {
    override suspend fun run() {
        super.run()

	val start_theta_factor: Int = 20

	//
	// Make bitmap
	//
	val res_x: Int = 44
	val res_y: Int = 16
	val scale_x: Int = 5
	val scale_y: Int = 5
	var plane: BitMap = BitMap(allBulbs, res_x*scale_x, res_y*scale_y, -2.8f, -1.2f, 12.0f, 4.0f)
	effect(plane)
	plane.setOffset(Math.round((scale_x.toFloat())*Math.sin((start_theta_factor.toDouble())/30.0f)).toInt(), Math.round(2.0f*(scale_y.toFloat())*Math.cos((start_theta_factor.toDouble())/15.0f)).toInt())
	
	//
	// Draw Norwegian flag
	//
	plane.fill(Color(0xBA, 0x12, 0x2B))
	plane.fillRectangle(0, 6*scale_y, res_x*scale_x, 4*scale_y, Color(255, 255, 255))
	plane.fillRectangle(0, 7*scale_y, res_x*scale_x, 2*scale_y, Color(0x00, 0x24, 0x69))
	plane.fillRectangle(6*scale_x, 0, 4*scale_x, res_y*scale_y, Color(255, 255, 255))
	plane.fillRectangle(7*scale_x, 0, 2*scale_x, res_y*scale_y, Color(0x00, 0x24, 0x69))	

	//
	// Fade in
	//
	for(i in 0..10)
	{
		plane.setBrightness((i).toFloat()/10.0f)
		skip(1)
	}	

	//
	// Wave flag
	//
	for(i in start_theta_factor until 360)
	{
		plane.setOffset(Math.round((scale_x.toFloat())*Math.sin((i.toDouble())/30.0f)).toInt(), Math.round(2.0f*(scale_y.toFloat())*Math.cos((i.toDouble())/15.0f)).toInt())
		skip(1)
	}

	//
	// Fade out
	//
	for(i in 0..10)
	{
		plane.setBrightness((10-i).toFloat()/10.0f)
		skip(1)
	}
	plane.cancel()
    }
}
