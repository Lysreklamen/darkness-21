package darkness.web;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Scanner;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class WebServer {
	public static void main(String[] args) throws Exception {
		HttpServer server = HttpServer.create(new InetSocketAddress(13371), 0);
		server.createContext("/form.html", new FormHandler());
		server.createContext("/send.html", new SendHandler());
		server.setExecutor(null);
		server.start();
	}

	private static class FormHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			byte[] response = (
					"<!DOCTYPE html>" +
					"<html>" +
					"	<head><title>Lysreklamen 2015</title></head>" +
					"	<body>" +
					"		<form action='send.html' method='GET'>" +
					"			<label>File name:</label>" +
					"			<input type='text' name='fileName' />" +
					"			<input type='submit' value='Overlay' />" +
					"		</form>" +
					"	</body>" +
					"</html>").getBytes(Charset.forName("UTF-8"));
			exchange.sendResponseHeaders(200, response.length);
			OutputStream out = exchange.getResponseBody();
			out.write(response);
			out.close();
		}
	}

	private static class SendHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			Socket socket = null;
			PrintWriter writer = null;
			try {
				socket = new Socket("localhost", 13370);
				writer = new PrintWriter(socket.getOutputStream());
				//TODO: Rewrite to use POST (need to find a good way to extract the form parameters)
				String uri = exchange.getRequestURI().toString();
				String prefix = "?fileName=";
				int index = uri.indexOf(prefix);
				String fileName = uri.substring(index + prefix.length()).replace("%2F", "/");
				//TODO: Validate that fileName doesn't contain newlines
				writer.write("overlay:" + fileName + "\n");
				writer.flush();
			} finally {
				if (writer != null) {
					writer.close();
				}
				if (socket != null) {
					socket.close();
				}
			}
			exchange.getResponseHeaders().add("Location", "form.html");
			exchange.sendResponseHeaders(302, 0);
			exchange.getResponseBody().close();
		}
	}
}
