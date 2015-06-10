package darkness.generator.api;

import java.awt.*;

public class BulbRGB implements BulbSet {
    private final Channel channelRed;
    private final Channel channelGreen;
    private final Channel channelBlue;

    BulbRGB(Channel channelRed, Channel channelGreen, Channel channelBlue) {
        this.channelRed = channelRed;
        this.channelGreen = channelGreen;
        this.channelBlue = channelBlue;
    }

    public Channel getChannelRed() {
        return channelRed;
    }

    public Channel getChannelGreen() {
        return channelGreen;
    }

    public Channel getChannelBlue() {
        return channelBlue;
    }

    @Override
    public void set(int red, int green, int blue) {
        channelRed.setValue(red);
        channelGreen.setValue(green);
        channelBlue.setValue(blue);
    }

    @Override
    public void set(Color color) {
        set(color.getRed(), color.getGreen(), color.getBlue());
    }

    public void set(String hexColor) {
        set(Color.decode(hexColor));
    }

    /**
     * Set a HSB color to this bulb
     * @param hue The floor of this number is subtracted from it to create a fraction between 0 and 1. This fractional number is then multiplied by 360 to produce the hue angle in the HSB color model.
     * @param saturation In the range 0.0..1.0
     * @param brightness In the range 0.0..1.0
     */
    @Override
    public void setHSB(float hue, float saturation, float brightness) {
        set(Color.getHSBColor(hue,saturation, brightness));
    }

    @Override
    public Color getColor() {
        return new Color(getRed(), getGreen(), getBlue());
    }

    @Override
    public int getRed() {
        return channelRed.getValue();
    }

    @Override
    public int getGreen() {
        return channelGreen.getValue();
    }

    @Override
    public int getBlue() {
        return channelBlue.getValue();
    }

    @Override
    public String toString() {
        return "Bulb{R:"+getChannelRed()+",G:"+getChannelGreen()+"B:"+getChannelBlue()+"}";
    }
}
