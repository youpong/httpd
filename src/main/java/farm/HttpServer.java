package farm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer implements Runnable {
	private Service service;
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

		try {
			HttpServer server = new HttpServer(service);
			server.run();
		} catch (UnknownServiceException e) {
			printUsage();
			System.exit(Http.EXIT_FAILURE);
		}
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
				new Thread(new Worker(sock)).start();
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

	public Worker(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			tcpPeerAddrPrint();
			//socket
			HttpRequest request = Parser.parseHttpRequest(
					new InputStreamReader(socket.getInputStream()));
			printRequest(request);
			
			reply();
			socket.close();
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	/**
	 * int sock, struct sockaddr *addr, socklen_t len);
	 */
	private void tcpPeerAddrPrint() {
		System.out.println("." + socket.getInetAddress() + ":" + socket.getPort());
	}

	// TODO: Support <Connection: keep-alive>
	private void printRequest(HttpRequest request) throws IOException {
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));

		String method = request.getMethod();
		
		out.write(method, 0, method.length());
		out.flush();
	}

	private void reply() throws IOException {
		PrintWriter out = new PrintWriter(socket.getOutputStream());
		out.println("HTTP/1.1 200 OK");
		out.println("Content-type: text/html");
		out.println("");
		readFile(out, "hello.html");
		out.flush();
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
