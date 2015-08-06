package darkness.generator.output;

import darkness.generator.api.Channel;
import darkness.generator.api.ScriptBase;
import darkness.generator.api.ScriptManager;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Instances of subclasses of this class can be passed to {@link ScriptManager#start(ScriptBase, BaseOutput)},
 * and their methods will be invoked at various points in the generation of a sequence.
 * Thus, a generated sequence may be output in various manners.
 */
public abstract class BaseOutput {

    protected BufferedWriter writer;
    protected int frameNumber = 1;

    /**
     * Is called once per channel per frame; however, it might not be called in the sequence of the channel index.
     * TODO: This essentially means that {@link ConsoleOutput} and {@link FileOutput} are useless, and should be fixed.
     */
    public void writeChannelValue(Channel channel) {
        try {
            writer.write(Integer.toString(channel.getValue()));
            writer.write( " " );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeString( String str ) {
        try {
            writer.write( str );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeNewline() {
        writeString( "\n" );
    }

    public void writeComment( String comment ) {
        try {
            writer.write( "#" + comment + "\n" );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void beginScript( ) {
        System.out.println( "Script begin: " );
    }

    public void beginScript( String name ) {
        System.out.println( "Script \"" + name + "\" begin: " );
    }

    public void endScript() {
        System.out.println( "Script done" );
    }

    public void endScript( String name ) {
        System.out.println( "Script \"" + name + "\" done" );
    }

    public void beginFrame() {
        System.out.print( "Frame " + Integer.toString( frameNumber ) + ": " );
    }

    public void endFrame() {
       nextFrame();
    }

    public abstract void flush() throws IOException;

    public void nextFrame() {
        ++frameNumber;
        writeNewline();
    }

}
