package darkness.generator.output;

import darkness.generator.api.Channel;

import java.io.BufferedWriter;
import java.io.IOException;

public abstract class BaseOutput {

    protected BufferedWriter writer;
    protected int frameNumber = 1;

    /**
     * Writes a frame to whatever output we want.
     */
    //public abstract void write();

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
