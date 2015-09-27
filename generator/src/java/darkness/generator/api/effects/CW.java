package darkness.generator.api.effects;

import darkness.generator.api.BulbSet;
import javafx.scene.effect.Effect;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by knutaldrin on 27.09.2015.
 */
public class CW extends EffectBase {

    /**
     * Map of CW codes for characters.
     * True = dit
     * False = dah
     */
    private static final HashMap<Character, Boolean[]> morseCode = new HashMap<>();

    private BulbSet bulbSet;
    private Color color;
    private int framesPerDit;
    private String str;
    private boolean cancelled;

    private int totalFrames;
    ArrayList<ArrayList<Boolean[]>> sequence = new ArrayList<>();

    static {
        morseCode.put('a', new Boolean[]{true, false});
        morseCode.put('b', new Boolean[]{false, true, true, true});
        morseCode.put('c', new Boolean[]{false, true, false, true});
        morseCode.put('d', new Boolean[]{false, true, true});
        morseCode.put('e', new Boolean[]{true});
        morseCode.put('f', new Boolean[]{true, true, false, true});
        morseCode.put('g', new Boolean[]{false, false, true});
        morseCode.put('h', new Boolean[]{true, true, true, true});
        morseCode.put('i', new Boolean[]{true, true});
        morseCode.put('j', new Boolean[]{true, false, false, false});
        morseCode.put('k', new Boolean[]{false, true, false});
        morseCode.put('l', new Boolean[]{true, false, true, true});
        morseCode.put('m', new Boolean[]{false, false});
        morseCode.put('n', new Boolean[]{false, true});
        morseCode.put('o', new Boolean[]{false, false, false});
        morseCode.put('p', new Boolean[]{true, false, false, true});
        morseCode.put('q', new Boolean[]{false, false, true, false});
        morseCode.put('r', new Boolean[]{true, false, true});
        morseCode.put('s', new Boolean[]{true, true, true});
        morseCode.put('t', new Boolean[]{false});
        morseCode.put('u', new Boolean[]{true, true, false});
        morseCode.put('v', new Boolean[]{true, true, true, false});
        morseCode.put('w', new Boolean[]{true, false, false});
        morseCode.put('x', new Boolean[]{false, true, true, false});
        morseCode.put('y', new Boolean[]{false, true, false, false});
        morseCode.put('z', new Boolean[]{false, false, true, true});

        morseCode.put('0', new Boolean[]{false, false, false, false, false});
        morseCode.put('1', new Boolean[]{true, false, false, false, false});
        morseCode.put('2', new Boolean[]{true, true, false, false, false});
        morseCode.put('3', new Boolean[]{true, true, true, false, false});
        morseCode.put('4', new Boolean[]{true, true, true, true, false});
        morseCode.put('5', new Boolean[]{true, true, true, true, true});
        morseCode.put('6', new Boolean[]{false, true, true, true, true});
        morseCode.put('7', new Boolean[]{false, false, true, true, true});
        morseCode.put('8', new Boolean[]{false, false, false, true, true});
        morseCode.put('9', new Boolean[]{false, false, false, false, true});

        morseCode.put('.', new Boolean[]{true, false, true, false, true, false});
        morseCode.put(',', new Boolean[]{false, false, true, true, false, false});
        morseCode.put('?', new Boolean[]{true, true, false, false, true, true});
        morseCode.put('\'', new Boolean[]{true, false, false, false, false, true});
        morseCode.put('!', new Boolean[]{false, true, false, true, false, false});
        morseCode.put('/', new Boolean[]{false, true, true, false, true});
        morseCode.put('(', new Boolean[]{false, true, false, false, true});
        morseCode.put(')', new Boolean[]{false, true, false, false, true, false});
        morseCode.put('&', new Boolean[]{true, false, true, true, true});
        morseCode.put(':', new Boolean[]{false, false, false, true, true, true});
        morseCode.put(';', new Boolean[]{false, true, false, true, false, true});
        morseCode.put('=', new Boolean[]{false, true, true, true, false});
        morseCode.put('+', new Boolean[]{true, false, true, false, true});
        morseCode.put('-', new Boolean[]{false, true, true, true, true, false});
        morseCode.put('_', new Boolean[]{true, true, false, false, true, false});
        morseCode.put('"', new Boolean[]{true, false, true, true, false, true});
        morseCode.put('$', new Boolean[]{true, true, true, false, true, true, false});
        morseCode.put('@', new Boolean[]{true, false, false, true, false, true});
    }

    public CW(BulbSet bulbSet, String str, Color color, int framesPerDit) {
        this.bulbSet = bulbSet;
        this.str = str;
        this.color = color;
        this.framesPerDit = framesPerDit;

        int totalFrames = 0;


        String[] words = str.split(" ");

        // Build an array of arrays (word) of arrays(letter in morse) of bools (dits and dahs)
        for (String word : words) {
            ArrayList<Boolean[]> morseWord = new ArrayList<>();

            for (Character c : word.toCharArray()) {
                morseWord.add(morseCode.get(c));
            }

            sequence.add(morseWord);
        }

        // TODO: Actually compute this
        totalFrames = 0; // Something
    }

    public CW(BulbSet bulbSet, String str) {
        this(bulbSet, str, Color.LIGHT_GRAY, 3);
    }

    @Override
    public void run() {
        // Pump out morse
        //
        // Dit = one period
        // Dah = threee periods
        // Space equal to one dit between dits and dahs
        // Space equal to one dah between characters
        // Space equal to seven dits between words

        Color color = new Color(200, 200, 200);

        for( ArrayList<Boolean[]> word : sequence) {
            for (Boolean[] c : word) {
                if (cancelled)
                    return;

                for( Boolean ditdah : c) {
                    set(bulbSet, color);
                    skip(ditdah ? framesPerDit : 3 * framesPerDit); // Skip 3xdit for a dah
                    relinquish(bulbSet);
                    skip(framesPerDit);
                }

                // Skip two more dits between characters
                skip(2 * framesPerDit);
            }

            // Skip 4 more dits to total 7 between words
            skip(4 * framesPerDit);
        }

    }

    @Override
    public void cancel() {
        cancelled = true;
    }

    @Override
    public String toString() {
        return "Effect CW on " + bulbSet + " for color " + color + " over " + totalFrames + " frames.";
    }
}
