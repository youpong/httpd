package farm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer implements Runnable {
	public static final String HTTP_VERSION = "HTTP/1.1";

	private Service service;

	private ServerSocket svSock;
	private File documentRoot = new File("./www");

	public static void main(String args[]) {
		String service = Http.HTTP_SERVICE;

		if (args.length >= 2) {
			printUsage();
			System.exit(Http.EXIT_FAILURE);
		}

		if (args.length == 1) {
			service = args[0];
		}

		try {
			HttpServer server = new HttpServer(service);
			server.run();
		} catch (UnknownServiceException e) {
			printUsage();
			System.exit(Http.EXIT_FAILURE);
		}
	}

	public File getDocumentRoot() {
		return documentRoot;
	}

	public HttpServer(String service) throws UnknownServiceException {
		this.service = new Service(service);
	}

	public HttpServer(Service service) {
		this.service = service;
	}

	private static void printUsage() {
		System.err.println("Usage: HttpServer [service]");
	}

	public void run() {
		try {
			svSock = new ServerSocket(service.getPort());
			printHostPort();
			while (true) {
				Socket sock = svSock.accept();
				HttpLog log = new HttpLog(new OutputStreamWriter(System.out));
				new Thread(new Worker(this, sock, log)).start();
			}
		} catch (Exception e) {
			System.err.println(e);
			System.exit(Http.EXIT_FAILURE);
		}
	}

	/**
	 * int sock, struct sockaddr *addr, socklen_t len);
	 */
	private void printHostPort() {
		System.out.println(svSock.getInetAddress().getHostName() + ":" + svSock.getLocalPort());
	}

}

class Worker implements Runnable {
	Socket socket;
	HttpServer server;
	HttpLog log;

	public Worker(HttpServer server, Socket socket, HttpLog log) {
		this.server = server;
		this.socket = socket;
		this.log = log;
	}

	public void run() {
		try {
			log.setPeerAddr(socket);

			HttpRequest request = Parser.parseHttpRequest(new InputStreamReader(socket.getInputStream()));
			log.setRequest(request);

			HttpResponse response = reply(request);
			log.setResponse(response);

			log.write();
			socket.close();
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	/**
	 * int sock, struct sockaddr *addr, socklen_t len);
	 *
	 * private void tcpPeerAddrPrint() { System.out.println("." +
	 * socket.getInetAddress() + ":" + socket.getPort()); }
	 */

	// TODO: Support <Connection: keep-alive>
	/*
	 * private void printRequest(HttpRequest request) throws IOException {
	 * BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
	 * 
	 * String method = request.getMethod();
	 * 
	 * out.write(method, 0, method.length()); out.flush(); }
	 */

	private HttpResponse reply(HttpRequest request) throws IOException {
		PrintWriter out = new PrintWriter(socket.getOutputStream());
		HttpResponse response = new HttpResponse();

		switch (request.getMethod()) {
		case "GET":
			File targetFile = new File(server.getDocumentRoot(), request.getRequestURI());
			if (!targetFile.canRead()) {
				response.setStatusCode("404");
				response.setContentType("text/html");
				out.print(response.gen());
				readFile(out, "www/error.html");
				out.flush();
				return response;
			}

			response.setStatusCode("200");
			response.setContentType("text/html");
			response.setContentLength(targetFile.length());

			out.print(response.gen());
			readFile(out, targetFile.getPath());
			out.flush();
			return response;
		case "POST":
		default:

		}
		return null;
	}

	private void readFile(PrintWriter out, String path) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
		char[] buf = new char[1024];

		int len;
		while ((len = in.read(buf, 0, 1024)) != -1) {
			out.write(buf, 0, len);
		}
		in.close();
	}
}
