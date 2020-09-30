package farm;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

class HttpServerWorker implements Runnable {
	Socket socket;
	HttpServer server;

	public HttpServerWorker(HttpServer server, Socket socket) {
		this.server = server;
		this.socket = socket;
	}

	public void run() {
		try {
			HttpLog log;

			// while Connection is alive
			while (true) {
				log = new HttpLog(new OutputStreamWriter(System.out));

				log.setPeerAddr(socket);

				HttpRequest request = HttpRequestParser.parse(socket.getInputStream(),
						HttpServer.DEBUG_MODE);
				log.setRequest(request);
				/*
				 * if(request.isInvalid()) { break; }
				 */
				HttpResponse response = createHttpResponse(request);
				response.generate(socket.getOutputStream());
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
		} catch (UnexpectedCharException e) {
			System.err.println(e);
		}
	}

	private HttpResponse createHttpResponse(HttpRequest request)
			throws IOException, UnknownMethodException {
		HttpResponse response = new HttpResponse();

		switch (request.getMethod()) {
		case "GET" :
		case "HEAD" :
			// Server
			response.setHeader("Server", HttpServer.SERVER_NAME);

			// Status Code
			File targetFile = new File(server.getDocumentRoot(),
					request.getRequestURI());
			if (!targetFile.canRead() || targetFile.isDirectory()) {
				targetFile = new File(server.getDocumentRoot(), "error.html");
				response.setStatusCode("404");
			} else
				response.setStatusCode("200");

			// Content-Type
			response.setHeader("Content-Type", "text/html");

			// Content-Length(set same value with GET/HEAD)
			response.setHeader("Content-Length", Long.toString(targetFile.length()));

			// Body
			if ("GET".equals(request.getMethod()))
				readFile(response, targetFile.getPath());

			return response;
		default :
			throw new UnknownMethodException("Method: " + request.getMethod());
		}
	}

	private void readFile(HttpResponse response, String path) throws IOException {
		InputStream is = new FileInputStream(path);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];

		int len;
		while ((len = is.read(buf, 0, 1024)) != -1) {
			os.write(buf, 0, len);
		}
		response.setBody(os.toByteArray());
		is.close();
	}
}