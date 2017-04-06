package darkness.patterneditor;

import java.util.List;

public class FixedColorBulb extends Bulb {
    private final int channel;
    private final RgbColor color;
    
    public FixedColorBulb(int id, double x, double y, int channel, RgbColor color, 
                          List<Double> xCoordinates, List<Double> yCoordinates) {
        super(id, x, y, xCoordinates, yCoordinates);
        this.channel = channel;
        this.color = color;
    }
    
    @Override
    public RgbColor getColor() {
        return color;
    }

    @Override
    public String getBulbInfo() {
        return String.format("Fixed color bulb. Id: %d; Position: (%.2f, %.2f); Channel: %d; Color: (%d, %d, %d)",
                getId(), getX(), getY(), channel,
                color.getRed(), color.getGreen(), color.getBlue());
    }
    
    public String toString() {
        return getId() + " " + format(getX()) + " " + format(getY()) + " F " + channel + " " + color.getRed() + " " + color.getGreen() + " " + color.getBlue() + createPolygonString();
    }
}
