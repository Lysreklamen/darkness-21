package darkness.patterneditor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputListener;

@SuppressWarnings("serial")
public class BulbPanel extends JPanel implements ActionListener, ChangeListener, MouseInputListener, KeyEventDispatcher {
    private String patternFileName;
    private List<Bulb> bulbs;
    private Bulb hoveredBulb;
    private Bulb selectedBulb;
    private Integer hoveredPointIndex;
    private Integer selectedPointIndex;
    private Point previousDragPos;
    private double gridResolution = 0.2;
    private boolean isGridVisible;
    
    private final JLabel frameLabel;
    private final JTextArea errorMessageArea;
    private final JScrollPane errorMessageScrollPane;
    private final JButton reloadButton;
    private final JButton saveButton;
    private final JToggleButton gridToggleButton;
    private final JLabel bulbInfoLabel;

    private final static double RENDER_OFFSET_X = 50; // Pixels
    private final static double RENDER_OFFSET_Y = 100; // Pixels
    private final static double RENDER_SCALE = 150; // Pixels per meter
    private final static double HOVER_POINT_SIZE = 6;
    private final static double HOVER_POINT_DISTANCE = 10;
    private final static int MAJOR_GRID_MULTIPLE = 5;
    // Cornice == gesims
    private final static double CORNICE_X = 0;
    private final static double CORNICE_Y = 0;
    private final static double CORNICE_WIDTH = 10;
    private final static double CORNICE_HEIGHT = 2;


    public BulbPanel(String patternFileName) throws Exception {
        this.patternFileName = patternFileName;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setSize(new Dimension(1280, 480));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        addMouseListener(this);
        addMouseMotionListener(this);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
        
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        add(topPanel);
        
        reloadButton = new JButton("Reload");
        reloadButton.addActionListener(this);
        reloadButton.setAlignmentY(TOP_ALIGNMENT);
        reloadButton.setPreferredSize(new Dimension(100, 25));
        reloadButton.setMnemonic(KeyEvent.VK_R);
        topPanel.add(reloadButton);
        
        topPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        
        saveButton = new JButton("Save");
        saveButton.addActionListener(this);
        saveButton.setAlignmentY(TOP_ALIGNMENT);
        saveButton.setPreferredSize(new Dimension(100, 25));
        saveButton.setMnemonic(KeyEvent.VK_S);
        topPanel.add(saveButton);
        
        topPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        
        topPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        
        frameLabel = new JLabel("", JLabel.RIGHT);
        frameLabel.setAlignmentY(TOP_ALIGNMENT);
        frameLabel.setPreferredSize(new Dimension(100, 25));
        topPanel.add(frameLabel);
        
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.X_AXIS));
        add(middlePanel);
        
        middlePanel.add(Box.createRigidArea(new Dimension(10, 0)));
        
        gridToggleButton = new JToggleButton("Grid");
        gridToggleButton.addActionListener(this);
        gridToggleButton.setAlignmentY(TOP_ALIGNMENT);
        gridToggleButton.setPreferredSize(new Dimension(100, 25));
        gridToggleButton.setMnemonic(KeyEvent.VK_G);
        middlePanel.add(gridToggleButton);
        
        middlePanel.add(Box.createRigidArea(new Dimension(10, 0)));
        
        bulbInfoLabel = new JLabel("");
        bulbInfoLabel.setAlignmentY(TOP_ALIGNMENT);
        bulbInfoLabel.setPreferredSize(new Dimension(800, 25));
        middlePanel.add(bulbInfoLabel);
        
        middlePanel.add(Box.createRigidArea(new Dimension(Integer.MAX_VALUE, 0)));
        
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        errorMessageArea = new JTextArea();
        errorMessageArea.setEditable(false);
        errorMessageArea.addMouseListener(this);
        errorMessageScrollPane = new JScrollPane(errorMessageArea);
        errorMessageScrollPane.setVisible(false);
        add(errorMessageScrollPane);
        
        reload();
    }
    
    public void reload() throws Exception {
        PatternFileParser patternParser = new PatternFileParser();
        System.out.println("Parsing pattern file '" + patternFileName + "'...");
        PatternParseResult patternResult = patternParser.parse(new FileReader(patternFileName));
        bulbs = patternResult.getBulbs();
        
        if (patternResult.isSuccess()) {
            errorMessageScrollPane.setVisible(false);
        }
        else {
            bulbs = new ArrayList<Bulb>();
            StringBuffer sb = new StringBuffer();
            
            if (patternResult.getErrors().size() > 0)
                sb.append(patternResult.getErrors().size() + " errors found in the pattern file:");
            for (String error : patternResult.getErrors()) {
                sb.append('\n');
                sb.append(error);
            }
            
            if (patternResult.getErrors().size() > 0)
                sb.append("\n\n");
            
            errorMessageArea.setText(sb.toString());
            errorMessageArea.scrollRectToVisible(new Rectangle(0, 0, 0, 0));
            errorMessageScrollPane.setVisible(true);
        }

        repaint();
    }
    
    private void updateBulbInfoLabel() {
        bulbInfoLabel.setText(hoveredBulb == null ? "" : hoveredBulb.getBulbInfo());
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawGrid(g);
        drawCornice(g);
        for (Bulb bulb : bulbs) {
            drawBulb(g, bulb);
        }
        drawHoveredPoint(g);
    }
    
    private void drawGrid(Graphics g) {
        if (!isGridVisible)
            return;
        
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.LIGHT_GRAY);
        
        int count = 0;
        for (double x = RENDER_OFFSET_X; x < getWidth(); x += gridResolution * RENDER_SCALE) {
            g2.setStroke(new BasicStroke(count % MAJOR_GRID_MULTIPLE == 0 ? 2 : 1));
            g.drawLine((int) x, (int) RENDER_OFFSET_Y, (int) x, (int) getHeight());
            ++count;
        }
        
        count = 0;
        for (double y = RENDER_OFFSET_Y; y < getHeight(); y += gridResolution * RENDER_SCALE) {
            g2.setStroke(new BasicStroke(count % MAJOR_GRID_MULTIPLE == 0 ? 2 : 1));
            g.drawLine((int) RENDER_OFFSET_X, (int) y, (int) getWidth(), (int) y);
            ++count;
        }
    }

    private void drawCornice(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.drawRect(scaleX(CORNICE_X), scaleY(CORNICE_Y), scaleX(CORNICE_WIDTH), scaleY(CORNICE_HEIGHT));
    }
    
    private void drawBulb(Graphics g, Bulb bulb) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(bulb == hoveredBulb ? 3 : 1));
        Color fillColor = createColor(bulb.getColor());
        Color borderColor = (bulb == hoveredBulb ? Color.YELLOW : Color.BLACK);
        Polygon polygon = bulb.createPolygon(RENDER_SCALE, RENDER_OFFSET_X, RENDER_OFFSET_Y);
        g.setColor(fillColor);
        g.fillPolygon(polygon);
        g.setColor(borderColor);
        g.drawPolygon(polygon);
    }
    
    private void drawHoveredPoint(Graphics g) {
        if (hoveredPointIndex == null)
            return;
        Polygon p = hoveredBulb.createPolygon(RENDER_SCALE, RENDER_OFFSET_X, RENDER_OFFSET_Y);
        g.setColor(Color.WHITE);
        int x = (int) (p.xpoints[hoveredPointIndex] - HOVER_POINT_SIZE / 2);
        int y = (int) (p.ypoints[hoveredPointIndex] - HOVER_POINT_SIZE / 2);
        g.fillOval(x, y, (int) HOVER_POINT_SIZE, (int) HOVER_POINT_SIZE);
        g.setColor(Color.BLACK);
        g.drawOval(x, y, (int) HOVER_POINT_SIZE, (int) HOVER_POINT_SIZE);
    }

    private int scaleX(double value) {
        return (int) (value * RENDER_SCALE + RENDER_OFFSET_X);
    }

    private int scaleY(double value) {
        return (int) (value * RENDER_SCALE + RENDER_OFFSET_Y);
    }
    
    private static Color createColor(RgbColor source) {
        return new Color(source.getRed(), source.getGreen(), source.getBlue());
    }
    
    private void findHoveredBulb(Point mousePos) {
        hoveredBulb = null;
        hoveredPointIndex = null;
        for (int b = bulbs.size() - 1; b >= 0; --b) {
            Bulb bulb = bulbs.get(b);
            Polygon p = bulb.createPolygon(RENDER_SCALE, RENDER_OFFSET_X, RENDER_OFFSET_Y);
            if (p.contains(mousePos)) {
                hoveredBulb = bulb;
                hoveredPointIndex = null;
                Integer bestPointIndex = null;
                double bestPointDistance = Double.MAX_VALUE;
                for (int i = 0; i < p.npoints; ++i) {
                    double distance = new Point(p.xpoints[i], p.ypoints[i]).distance(mousePos);
                    if (distance <= HOVER_POINT_DISTANCE && distance < bestPointDistance) {
                        bestPointDistance = distance;
                        bestPointIndex = i;
                    }
                }
                hoveredPointIndex = bestPointIndex;
                return;
            }
        }
    }
    
    private void save() {
        if (JOptionPane.showConfirmDialog(this, "Are you sure you want to save? The original file you loaded will be overwritten.", "Confirm save", JOptionPane.YES_NO_OPTION) != 0)
            return;
        
        StringBuilder sb = new StringBuilder();
        for (Bulb bulb : bulbs) {
            sb.append(bulb);
            sb.append('\n');
        }
        
        FileOutputStream stream = null;
        OutputStreamWriter writer = null;
        try {
            stream = new FileOutputStream(patternFileName);
            writer = new OutputStreamWriter(stream);
            writer.write(sb.toString());
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(this, "An error occurred while saving.");
        }
        finally {
            try {
                if (writer != null)
                    writer.close();
                if (stream != null)
                    stream.close();
            }
            catch (IOException e) {
                JOptionPane.showMessageDialog(this, "An error occurred while saving.");
            }
        }
    }
    
    private void toggleGrid() {
        isGridVisible = !isGridVisible;
        gridToggleButton.setSelected(isGridVisible);
        repaint();
    }
    
    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            if (event.getSource() == reloadButton)
                reload();
            else if (event.getSource() == saveButton)
                save();
            else if (event.getSource() == gridToggleButton)
                toggleGrid();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
    
    @Override
    public void stateChanged(ChangeEvent event) {
        updateBulbInfoLabel();
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        findHoveredBulb(e.getPoint());
        selectedBulb = hoveredBulb;
        selectedPointIndex = hoveredPointIndex;
        if (hoveredBulb != null)
            previousDragPos = e.getPoint();
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        selectedBulb = null;
        selectedPointIndex = null;
        previousDragPos = null;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (selectedBulb == null)
            return;
        int xDelta = e.getPoint().x - previousDragPos.x;
        int yDelta = e.getPoint().y - previousDragPos.y;
        if (selectedPointIndex != null && e.isAltDown()) {
            selectedBulb.shiftPoint(selectedPointIndex, xDelta, yDelta, RENDER_SCALE);
        }
        else {
            selectedBulb.shift(xDelta, yDelta, RENDER_SCALE);
        }
        previousDragPos = e.getPoint();
        repaint();
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
        Bulb previousHoveredBulb = hoveredBulb;
        Integer previousHoveredPointIndex = hoveredPointIndex;
        findHoveredBulb(e.getPoint());
        if (hoveredBulb != previousHoveredBulb || hoveredPointIndex != previousHoveredPointIndex) {
            updateBulbInfoLabel();
            repaint();
        }
    }
    
    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        try {
            if (e.getKeyChar() == 'r')
                reload();
            else if (e.getKeyChar() == 's')
                save();
            else if (e.getKeyChar() == 'g')
                toggleGrid();
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }
}
