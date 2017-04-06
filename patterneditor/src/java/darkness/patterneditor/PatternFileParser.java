package darkness.patterneditor;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PatternFileParser {
    public PatternParseResult parse(Reader sourceReader) {
        List<Bulb> bulbs = new ArrayList<Bulb>();
        List<String> errors = new ArrayList<String>();
        HashSet<Integer> ids = new HashSet<Integer>();
        double offsetX = 0;
        double offsetY = 0;
        double scaleX = 1;
        double scaleY = 1;
        int lineNumber = 0;
        try {
            BufferedReader reader = new BufferedReader(sourceReader);
            String line;
            while ((line = reader.readLine()) != null) {
                ++lineNumber;
                line = removeComment(line);
                List<String> items = tokenizeLine(line);
                if (items.size() == 0)
                    continue;
                String maybeInstruction = items.get(0).toUpperCase();
                if (maybeInstruction.equals("OFFSET")) {
                    offsetX = Double.parseDouble(items.get(1));
                    offsetY = Double.parseDouble(items.get(2));
                    continue;
                }
                if (maybeInstruction.equals("SCALE")) {
                    scaleX = Double.parseDouble(items.get(1));
                    scaleY = Double.parseDouble(items.get(2));
                    continue;
                }
                if (items.size() < 4) {
                    errors.add(lineNumber + ": No bulb type (line contains less than three elements)");
                    continue;
                }
                String bulbType = items.get(3).toUpperCase();
                try {
                    int id = Integer.parseInt(items.get(0));
                    if (ids.contains(id)) {
                        errors.add(lineNumber + ": Duplicate bulb id: " + id);
                        continue;
                    }
                    ids.add(id);
                    double x = (Double.parseDouble(items.get(1)) - offsetX) * scaleX;
                    double y = (Double.parseDouble(items.get(2)) - offsetY) * scaleY;
                    if (bulbType.equals("F")) {
                        bulbs.add(new FixedColorBulb(
                                id, x, y,
                                Integer.parseInt(items.get(4)),
                                new RgbColor(
                                        Integer.parseInt(items.get(5)),
                                        Integer.parseInt(items.get(6)),
                                        Integer.parseInt(items.get(7))),
                                getCoordinates(items, 8, scaleX),
                                getCoordinates(items, 9, scaleY)));
                    }
                    else if (bulbType.equals("R")) {
                        bulbs.add(new RgbBulb(
                                id, x, y,
                                Integer.parseInt(items.get(4)),
                                Integer.parseInt(items.get(5)),
                                Integer.parseInt(items.get(6)),
                                getCoordinates(items, 7, scaleX),
                                getCoordinates(items, 8, scaleY)));
                    }
                    else {
                        errors.add(lineNumber + ": Unknown bulb type '" + items.get(3) + "' (must be 'F' or 'R')");
                        continue;
                    }
                }
                catch (NumberFormatException e) {
                    errors.add(lineNumber + ": Invalid bulb id or position, or incorrect number of parameters for bulb, or invalid parameters");
                    continue;
                }
            }
            if (errors.size() == 0) {
                System.out.println("Pattern file parsed");
                return PatternParseResult.success(bulbs);
            }
            else {
                System.out.println("Pattern file parsing failed");
                return PatternParseResult.failure(errors);
            }
        }
        catch (Exception e) {
            errors.add(e.getMessage());
            System.out.println("Pattern file parsing failed");
            return PatternParseResult.failure(errors);
        }
    }
    
    private String removeComment(String line) {
        int index = line.indexOf('#');
        return index == -1 ? line : line.substring(0, index);
    }
    
    private List<String> tokenizeLine(String line) {
        List<String> result = new ArrayList<String>();
        for (String item : line.split("[ \\t]+")) {
            if (!item.isEmpty())
                result.add(item);
        }
        return result;
    }
    
    private List<Double> getCoordinates(List<String> items, int startIndex, double scale) {
        List<Double> result = new ArrayList<Double>();
        for (int i = startIndex; i < items.size(); i += 2) {
            result.add(Double.parseDouble(items.get(i)) * scale);
        }
        return result;
    }
}
