package darkness.simulator;

public class ParseException extends Exception {
    public ParseException(String fileName, int lineNumber, String message) {
        super(String.format("Parse error in '%s', line %d: %s", fileName, lineNumber, message));
    }
}
