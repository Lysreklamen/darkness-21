package darkness.generator.api;

import java.awt.*;

/**
 * An RGB bulb, whose color is defined by three channels.
 */
public class BulbRGB implements BulbSet {
    private final Channel channelRed;
    private final Channel channelGreen;
    private final Channel channelBlue;

    /**
     * Create a new bulb that is controlled by the given channels.
     */
    public BulbRGB(Channel channelRed, Channel channelGreen, Channel channelBlue) {
        this.channelRed = channelRed;
        this.channelGreen = channelGreen;
        this.channelBlue = channelBlue;
    }

    /**
     * @return The channel that controls the red component of this bulb.
     */
    public Channel getChannelRed() {
        return channelRed;
    }

    /**
     * @return The channel that controls the green component of this bulb.
     */
    public Channel getChannelGreen() {
        return channelGreen;
    }

    /**
     * @return The channel that controls the blue component of this bulb.
     */
    public Channel getChannelBlue() {
        return channelBlue;
    }

    /**
     * Set the RGB color of this bulb, by setting the individual channels to the given values.
     */
    @Override
    public void set(int red, int green, int blue) {
        channelRed.setValue(red);
        channelGreen.setValue(green);
        channelBlue.setValue(blue);
    }

    /**
     * Set the RGB color of this bulb, by setting the individual channels to the components of the given {@link Color}.
     */
    @Override
    public void set(Color color) {
        set(color.getRed(), color.getGreen(), color.getBlue());
    }

    /**
     * Set the RGB color of this bulb, by setting the individual channels to the components of the given hexadecimal RGB string.
     * {@code hexColor} must start with "0x" or "#".
     */
    public void set(String hexColor) {
        set(Color.decode(hexColor));
    }

    /**
     * Set a HSB color of this bulb, by setting the individual channels to the components of the RGB color that corresponds to the given HSB color.
     * @param hue The floor of this number is subtracted from it to create a fraction between 0 and 1. This fractional number is then multiplied by 360 to produce the hue angle in the HSB color model.
     * @param saturation In the range 0.0..1.0
     * @param brightness In the range 0.0..1.0
     */
    @Override
    public void setHSB(float hue, float saturation, float brightness) {
        set(Color.getHSBColor(hue,saturation, brightness));
    }

    /**
     * @return The RGB color that is indicated by the current values of this bulb's channels.
     */
    @Override
    public Color getColor() {
        return new Color(getRed(), getGreen(), getBlue());
    }

    /**
     * @return The red component that is indicated by the current values of this bulb's red channel.
     */
    @Override
    public int getRed() {
        return channelRed.getValue();
    }

    /**
     * @return The green component that is indicated by the current values of this bulb's green channel.
     */
    @Override
    public int getGreen() {
        return channelGreen.getValue();
    }

    /**
     * @return The blue component that is indicated by the current values of this bulb's blue channel.
     */
    @Override
    public int getBlue() {
        return channelBlue.getValue();
    }

    /**
     * @return A string representation of this bulb, indicating its current channel values.
     */
    @Override
    public String toString() {
        return "Bulb{R:"+getChannelRed()+",G:"+getChannelGreen()+"B:"+getChannelBlue()+"}";
    }
}
