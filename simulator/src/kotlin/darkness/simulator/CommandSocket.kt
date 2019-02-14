package darkness.simulator

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.ServerSocket
import java.net.Socket

class CommandSocket(private val pgmPlayer: PgmPlayer) : Thread() {
    private val overlayPrefix = "overlay:"

    init {
        isDaemon = true
    }

    override fun run() {
        println("Socket running")
        var serverSocket: ServerSocket? = null
        try {
            serverSocket = ServerSocket(13370)
            println("Listening...")
            while (true) {
                var socket: Socket? = null
                var reader: BufferedReader? = null
                try {
                    socket = serverSocket.accept()!!
                    reader = BufferedReader(InputStreamReader(socket.getInputStream()))
                    println("Connected")
                    reader.forEachLine {line ->
                        println(line)
                        if (line.startsWith(overlayPrefix)) {
                            val fileName = line.substring(overlayPrefix.length)
                            //TODO: Validate fileName against a whitelist to prevent reading of arbitrary files
                            val pgmReader = PgmReader(fileName)
                            try {
                                pgmReader.read()
                                pgmPlayer.addOverlay(pgmReader)
                            } catch (e: ParseException) {
                                println(String.format("Exception when parsing PGM file '%s': %s", fileName, e))
                            } catch (e: IOException) {
                                println(String.format("Exception when reading PGM file '%s': %s", fileName, e))
                            }

                        }
                    }
                    println("Disconnected")
                } finally {
                    reader?.close()
                    socket?.close()
                }
            }
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close()
                } catch (e: IOException) {
                    println("Exception when closing server socket: $e")
                }
            }
        }
    }
}
