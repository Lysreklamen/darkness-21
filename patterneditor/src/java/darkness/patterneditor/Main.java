package darkness.patterneditor;

import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Main implements WindowListener {
    private JFrame mainFrame;
    private BulbPanel bulbPanel;
    
    public static void main(String[] args) throws Exception {
        Main program = new Main();
        //program.run(args);
        program.run(new String[]{"E:\\programs\\darkness\\simulator\\patterns\\uka15.txt"});
    }
    
    private void run(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("You must pass one parameter: the pattern file");
            System.exit(1);
        }
        
        bulbPanel = new BulbPanel(args[0]);
        mainFrame = new JFrame("UKA17 - Lysreklamesimulator");
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainFrame.addWindowListener(this);
        mainFrame.setContentPane(bulbPanel);
        mainFrame.setSize(new Dimension(1800, 700));
        mainFrame.setVisible(true);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (JOptionPane.showConfirmDialog(mainFrame, "Are you sure you want to quit? Your unsaved changes, if any, will be lost.", "Confirm quit", JOptionPane.YES_NO_OPTION) == 0) {
            mainFrame.dispose();
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}
