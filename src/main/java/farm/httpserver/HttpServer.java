package farm.httpserver;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;

import farm.Http;
import farm.Service;
import farm.UnknownServiceException;

public class HttpServer {
	//
	// static
	//
	public static final String HTTP_VERSION = "HTTP/1.1";
	public static final String SERVER_NAME = "bait/0.1";

	private Service service;
	private Options options;

	private ServerSocket svSock;

	public static void main(String args[]) {
		String service = Http.HTTP_SERVICE;

		if (args.length >= 2) {
			printUsage();
			System.exit(Http.EXIT_FAILURE);
		}

		if (args.length == 1) {
			service = args[0];
		}

		Options opts = new Options();
		
		try {
			HttpServer server = new HttpServer(service, opts);
			server.execute();
		} catch (UnknownServiceException e) {
			printUsage();
			System.exit(Http.EXIT_FAILURE);
		}
	}

	private static void printUsage() {
		System.err.println("Usage: HttpServer [service]");
	}

	public HttpServer(String service, Options options) throws UnknownServiceException {
		this.service = new Service(service);
		this.options = options;
	}

	public void execute() {
		try {
			svSock = new ServerSocket(service.getPort());
			printHostPort();
			while (true) {
				Socket sock = svSock.accept();
				new Thread(new HttpServerWorker(sock, options)).start();
			}
		} catch (Exception e) {
			System.err.println(e);
			System.exit(Http.EXIT_FAILURE);
		}
	}

	private void printHostPort() {
		System.out.println(
				svSock.getInetAddress().getHostName() + ":" + svSock.getLocalPort());
	}
}
