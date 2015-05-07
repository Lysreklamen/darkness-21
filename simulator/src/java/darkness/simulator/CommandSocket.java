package darkness.simulator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class CommandSocket extends Thread {
	private final PgmPlayer pgmPlayer;

	private static final String OVERLAY_PREFIX = "overlay:";

	public CommandSocket(PgmPlayer pgmPlayer) {
		this.pgmPlayer = pgmPlayer;
		setDaemon(true);
	}

	@Override
	public void run() {
		System.out.println("Socket running");
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(13370);
			System.out.println("Listening...");
			while (true) {
				Socket socket = null;
				BufferedReader reader = null;
				try {
					socket = serverSocket.accept();
					reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					System.out.println("Connected");
					String line;
					while ((line = reader.readLine()) != null) {
						System.out.println(line);
						if (line.startsWith(OVERLAY_PREFIX)) {
							String fileName = line.substring(OVERLAY_PREFIX.length());
							//TODO: Validate fileName against a whitelist to prevent reading of arbitrary files
							PgmReader pgmReader = new PgmReader(fileName);
							try {
								pgmReader.read();
								pgmPlayer.addOverlay(pgmReader);
							} catch (ParseException e) {
								System.out.println(String.format("Exception when parsing PGM file '%s': %s", fileName, e));
							} catch (IOException e) {
								System.out.println(String.format("Exception when reading PGM file '%s': %s", fileName, e));
							}
						}
					}
					System.out.println("Disconnected");
				} finally {
					if (reader != null) {
						reader.close();
					}
					if (socket != null) {
						socket.close();
					}
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					System.out.println("Exception when closing server socket: " + e);
				}
			}
		}
	}
}
