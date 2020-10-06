package farm.httpserver;

import java.io.FileWriter;
import java.net.ServerSocket;

import farm.Http;

public class Server {
	//
	// server constants
	//
	public static final String HTTP_VERSION = "HTTP/1.1";
	public static final String SERVER_NAME = "bait/0.1";

	private Options options;

	public static void main(String args[]) {
		Options opts = Options.parse(args);
		new Server(opts).start();
	}

	public Server(Options options) {
		this.options = options;
	}

	public void start() {
		try {
			Logger log = new Logger(new FileWriter(options.accessLog(), true));

			ServerSocket svSock = new ServerSocket(options.service().getPort());
			printHostPort(svSock);

			while (true) {
				Runnable runnable = new Worker(svSock.accept(), log, options);
				new Thread(runnable).start();
			}
		} catch (Exception e) {
			System.err.println(e);
			System.exit(Http.EXIT_FAILURE);
		}
	}

	private void printHostPort(ServerSocket svSock) {
		System.out.println("Server listen " +
				svSock.getInetAddress().getHostName() + ":" + svSock.getLocalPort());
	}
}
