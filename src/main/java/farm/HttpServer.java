package farm;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
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
			server.execute();
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

	public void execute() {
		try {
			svSock = new ServerSocket(service.getPort());
			printHostPort();
			while (true) {
				Socket sock = svSock.accept();
				new Thread(new HttpServerWorker(this, sock)).start();
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
