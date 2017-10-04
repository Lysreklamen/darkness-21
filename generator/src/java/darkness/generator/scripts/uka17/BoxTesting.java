package darkness.generator.scripts.uka17;

import darkness.generator.api.BulbRGB;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public abstract class BoxTesting extends BaseScript {
    @Override
    public void run() {
        int boxId = Integer.parseInt(getClass().getSimpleName().replaceAll("[a-zA-Z.]", ""));
        File boxFile = new File("../simulator/patterns/UKA-17/boxes.txt");
        List<Connector> connectors = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File("../simulator/patterns/UKA-17/boxes.txt"));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] items = line.trim().split(" ");
                String[] boxItems = items[0].split("\\.");
                int box = Integer.parseInt(boxItems[0]);
                if (box != boxId) {
                    continue;
                }
                int connector = Integer.parseInt(boxItems[1]);
                int bulbId = Integer.parseInt(items[1]);
                connectors.add(new Connector(connector, bulbId));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Collections.sort(connectors);
        Color[] colors = {Color.RED, Color.GREEN, Color.BLUE};
        for (Connector connector : connectors) {
            BulbRGB bulb = bulb(connector.bulbId);
            for (Color color : colors) {
                rgbFade(bulb, color, 10);
                skip(20);
                set(bulb, 0, 0, 0);
            }
        }
    }

    public static class BoxTesting01 extends BoxTesting { }
    public static class BoxTesting02 extends BoxTesting { }
    public static class BoxTesting03 extends BoxTesting { }
    public static class BoxTesting04 extends BoxTesting { }
    public static class BoxTesting05 extends BoxTesting { }
    public static class BoxTesting06 extends BoxTesting { }
    public static class BoxTesting07 extends BoxTesting { }
    public static class BoxTesting08 extends BoxTesting { }
    public static class BoxTesting09 extends BoxTesting { }
    public static class BoxTesting10 extends BoxTesting { }
    public static class BoxTesting11 extends BoxTesting { }
    public static class BoxTesting12 extends BoxTesting { }
    public static class BoxTesting13 extends BoxTesting { }
    public static class BoxTesting14 extends BoxTesting { }

    private static class Connector implements Comparable<Connector> {
        private final int connector;
        private final int bulbId;

        private Connector(int connector, int bulbId) {
            this.connector = connector;
            this.bulbId = bulbId;
        }

        @Override
        public int compareTo(Connector o) {
            return Integer.compare(connector, o.connector);
        }
    }
}
