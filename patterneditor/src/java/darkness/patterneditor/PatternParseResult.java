package darkness.patterneditor;

import java.util.ArrayList;
import java.util.List;

public class PatternParseResult {
    private boolean success;
    private List<Bulb> bulbs = new ArrayList<Bulb>();
    private List<String> errors = new ArrayList<String>();
    
    // We need a private constructor and static creation methods because 
    // the compiler can't tell the difference between the generic List types 
    private PatternParseResult(boolean success) {
        this.success = success;
    }
    
    public static PatternParseResult success(List<Bulb> bulbs) {
        PatternParseResult result = new PatternParseResult(true);
        result.bulbs.addAll(bulbs);
        return result;
    }

    public static PatternParseResult failure(List<String> errors) {
        PatternParseResult result = new PatternParseResult(false);
        result.errors.addAll(errors);
        return result;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public List<Bulb> getBulbs() {
        return new ArrayList<Bulb>(bulbs);
    }
    
    public List<String> getErrors() {
        return new ArrayList<String>(errors);
    }
}
