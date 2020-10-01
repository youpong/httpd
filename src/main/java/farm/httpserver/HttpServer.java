package farm.httpserver;

import java.net.ServerSocket;
import java.net.Socket;

import farm.Http;

public class HttpServer {
	//
	// server constants
	//
	public static final String HTTP_VERSION = "HTTP/1.1";
	public static final String SERVER_NAME = "bait/0.1";

	private Options options;

	public static void main(String args[]) {
		Options opts = Options.parse(args);
		HttpServer server = new HttpServer(opts);
		server.execute();	
	}

	public HttpServer(Options options) {
		this.options = options;
	}

	public void execute() {
		try {
			ServerSocket svSock = new ServerSocket(options.service().getPort());
			printHostPort(svSock);
			while (true) {
				Socket sock = svSock.accept();
				new Thread(new HttpServerWorker(sock, options)).start();
			}
		} catch (Exception e) {
			System.err.println(e);
			System.exit(Http.EXIT_FAILURE);
		}
	}

	private void printHostPort(ServerSocket svSock) {
		System.out.println(
				svSock.getInetAddress().getHostName() + ":" + svSock.getLocalPort());
	}
}
