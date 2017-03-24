package darkness.patterneditor;

public class RgbColor {
    private final int red;
    private final int green;
    private final int blue;
    
    public RgbColor(int red, int green, int blue) {
        this.red = coerce(red);
        this.green = coerce(green);
        this.blue = coerce(blue);
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }
    
    private static int coerce(int value) {
        if (value < 0)
            return 0;
        if (value > 255)
            return 255;
        return value;
    }
}
