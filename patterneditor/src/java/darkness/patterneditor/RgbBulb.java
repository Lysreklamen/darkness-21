package darkness.patterneditor;

import java.util.List;

public class RgbBulb extends Bulb {
    private final int redChannel;
    private final int greenChannel;
    private final int blueChannel;
    
    public RgbBulb(int id, double x, double y, int redChannel, int greenChannel, int blueChannel, 
                   List<Double> xCoordinates, List<Double> yCoordinates) {
        super(id, x, y, xCoordinates, yCoordinates);
        this.redChannel = redChannel;
        this.greenChannel = greenChannel;
        this.blueChannel = blueChannel;
    }
    
    @Override
    public RgbColor getColor() {
        return new RgbColor(191, 255, 191);
    }
    
    @Override
    public String getBulbInfo() {
        return String.format("RGB bulb. Id: %d; Position: (%.2f, %.2f); Channels: %d, %d, %d",
                getId(), getX(), getY(),
                redChannel, greenChannel, blueChannel);
    }
    
    public String toString() {
        return getId() + " " + format(getX()) + " " + format(getY()) + " R " + redChannel + " " + greenChannel + " " + blueChannel + createPolygonString();
    }
}
