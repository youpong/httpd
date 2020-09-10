package farm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer implements Runnable {
	public static final boolean DEBUG_MODE = false;
	public static final String HTTP_VERSION = "HTTP/1.1";
	public static final String SERVER_NAME = "bait/0.1";

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
				new Thread(new Worker(this, sock)).start();
			}
		} catch (Exception e) {
			System.err.println(e);
			System.exit(Http.EXIT_FAILURE);
		}
	}

	private void printHostPort() {
		System.out.println(svSock.getInetAddress().getHostName() + ":" + svSock
				.getLocalPort());
	}

}

class Worker implements Runnable {
	Socket socket;
	HttpServer server;

	public Worker(HttpServer server, Socket socket) {
		this.server = server;
		this.socket = socket;
	}

	public void run() {
		try {
			Reader reader = new InputStreamReader(socket.getInputStream());
			HttpLog log;

			// while Connection is alive
			while (true) {
				log = new HttpLog(new OutputStreamWriter(System.out));

				log.setPeerAddr(socket);

				HttpRequest request = Parser.parseHttpRequest(reader,
						HttpServer.DEBUG_MODE);
				log.setRequest(request);
				/*
				 * if(request.isInvalid()) { break; }
				 */
				HttpResponse response = reply(request);
				log.setResponse(response);

				log.write();
				if ("close".equals(request.getHeader("Connection")))
					break;
			}

			socket.close();
		} catch (IOException e) {
			System.err.println(e);
		} catch (UnknownMethodException e) {
			if (HttpServer.DEBUG_MODE)
				System.err.println(e);
		} catch (UnexpectedChar e) {
			System.err.println(e);
		}
	}

	private HttpResponse reply(HttpRequest request) throws IOException,
			UnknownMethodException {
		PrintWriter out = new PrintWriter(socket.getOutputStream());
		HttpResponse response = new HttpResponse();

		switch (request.getMethod()) {
		case "GET":
		case "HEAD":
			// Server
			response.setServer(HttpServer.SERVER_NAME);

			// Status Code
			File targetFile = new File(server.getDocumentRoot(), request
					.getRequestURI());
			if (!targetFile.canRead() || targetFile.isDirectory()) {
				targetFile = new File(server.getDocumentRoot(), "error.html");
				response.setStatusCode("404");
			} else
				response.setStatusCode("200");

			// Content-Type
			response.setContentType("text/html");

			// Content-Length(same value GET/HEAD)
			response.setContentLength(targetFile.length());

			out.print(response.gen());
			if ("GET".equals(request.getMethod()))
				readFile(out, targetFile.getPath());
			out.flush();

			return response;
		default:
			throw new UnknownMethodException("Method: " + request.getMethod());
		}
	}

	private void readFile(PrintWriter out, String path) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(
				new FileInputStream(path)));
		char[] buf = new char[1024];

		int len;
		while ((len = in.read(buf, 0, 1024)) != -1) {
			out.write(buf, 0, len);
		}
		in.close();
	}
}
