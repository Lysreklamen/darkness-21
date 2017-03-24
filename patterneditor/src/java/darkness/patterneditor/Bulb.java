package darkness.patterneditor;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class Bulb {
    private int id;
    private double x;
    private double y;
    private final List<Double> xCoordinates;
    private final List<Double> yCoordinates;
    
    private final static double DEFAULT_BULB_SIZE = 1;
    
    protected Bulb(int id, double x, double y, List<Double> xCoordinates, List<Double> yCoordinates) {
        if (xCoordinates == null || yCoordinates == null || xCoordinates.size() != yCoordinates.size())
            throw new IllegalArgumentException();
        this.id = id;
        this.x = x;
        this.y = y;
        if (xCoordinates.size() == 0) {
            this.xCoordinates = CreateDefaultXCoordinates();
            this.yCoordinates = CreateDefaultYCoordinates();
        }
        else {
            this.xCoordinates = new ArrayList<Double>(xCoordinates);
            this.yCoordinates = new ArrayList<Double>(yCoordinates);
        }
    }
    
    public abstract RgbColor getColor();
    public abstract String getBulbInfo();
    
    public int getId() {
        return id;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public void shift(double x, double y, double scale) {
        this.x += x / scale;
        this.y += y / scale;
    }

    public void shiftPoint(int index, double x, double y, double scale) {
        xCoordinates.set(index, xCoordinates.get(index) + x / scale);
        yCoordinates.set(index, yCoordinates.get(index) + y / scale);
    }
    
    public Polygon createPolygon(double scale, double xOffset, double yOffset) {
        int[] xPolyCoords = new int[xCoordinates.size()];
        int[] yPolyCoords = new int[yCoordinates.size()];
        for (int i = 0; i < xCoordinates.size(); ++i) {
            xPolyCoords[i] = (int)Math.round((x + xCoordinates.get(i)) * scale + xOffset);
            yPolyCoords[i] = (int)Math.round((y + yCoordinates.get(i)) * scale + yOffset);
        }
        return new Polygon(xPolyCoords, yPolyCoords, xPolyCoords.length);
    }
    
    protected String createPolygonString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < xCoordinates.size(); ++i) {
            sb.append(' ');
            sb.append(format(xCoordinates.get(i)));
            sb.append(' ');
            sb.append(format(yCoordinates.get(i)));
        }
        return sb.toString();
    }
    
    protected String format(double d) {
        return String.format(Locale.ENGLISH, "%1.2f", d);
    }
    
    private ArrayList<Double> CreateDefaultXCoordinates() {
        ArrayList<Double> coords = new ArrayList<Double>();
        double dist = DEFAULT_BULB_SIZE / 2;
        coords.add(-dist);
        coords.add(dist);
        coords.add(dist);
        coords.add(-dist);
        return coords;
    }
    
    private ArrayList<Double> CreateDefaultYCoordinates() {
        ArrayList<Double> coords = new ArrayList<Double>();
        double dist = DEFAULT_BULB_SIZE / 2;
        coords.add(-dist);
        coords.add(-dist);
        coords.add(dist);
        coords.add(dist);
        return coords;
    }
}
