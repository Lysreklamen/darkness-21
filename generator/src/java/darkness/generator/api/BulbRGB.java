package darkness.generator.api;

import java.awt.*;

/**
 * An RGB bulb, whose color is defined by three channels.
 */
public class BulbRGB implements BulbSet {
    private final int id;
    private final Channel channelRed;
    private final Channel channelGreen;
    private final Channel channelBlue;
    private final float position[] = {-1.0f, -1.0f, 0.0f};

    /**
     * Create a new bulb that is controlled by the given channels.
     */
    public BulbRGB(int id, Channel channelRed, Channel channelGreen, Channel channelBlue, float posX, float posY) {
        this.id = id;
        this.channelRed = channelRed;
        this.channelGreen = channelGreen;
        this.channelBlue = channelBlue;
        this.position[0] = posX;
        this.position[1] = posY;
    }

    public int getId() {
        return id;
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
    public void set(int red, int green, int blue, ScriptBase setter) {
        channelRed.setValue(red, setter);
        channelGreen.setValue(green, setter);
        channelBlue.setValue(blue, setter);
    }

    /**
     * Set the RGB color of this bulb, by setting the individual channels to the components of the given {@link Color}.
     */
    @Override
    public void set(Color color, ScriptBase setter) {
        set(color.getRed(), color.getGreen(), color.getBlue(), setter);
    }

    /**
     * Set the RGB color of this bulb, by setting the individual channels to the components of the given hexadecimal RGB string.
     * {@code hexColor} must start with "0x" or "#".
     */
    public void set(String hexColor, ScriptBase setter) {
        set(Color.decode(hexColor), setter);
    }

    /**
     * Set a HSB color of this bulb, by setting the individual channels to the components of the RGB color that corresponds to the given HSB color.
     * @param hue The floor of this number is subtracted from it to create a fraction between 0 and 1. This fractional number is then multiplied by 360 to produce the hue angle in the HSB color model.
     * @param saturation In the range 0.0..1.0
     * @param brightness In the range 0.0..1.0
     */
    @Override
    public void setHSB(float hue, float saturation, float brightness, ScriptBase setter) {
        set(Color.getHSBColor(hue, saturation, brightness), setter);
    }

    public void relinquish(ScriptBase setter) {
        set(Channel.RELINQUISH, Channel.RELINQUISH, Channel.RELINQUISH, setter);
    }

    /**
     * @return The RGB color that is indicated by the current values of this bulb's channels.
     */
    @Override
    public Color getColor() {
        return new Color(getRed(), getGreen(), getBlue());
    }

    @Override
    public float[] getPosition() {
        return position;
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
