package farm.httpserver;

import java.io.File;

import farm.Http;
import farm.Service;
import farm.UnknownServiceException;

public class Options {

	private Service service;
	private String dest = "-";
	private String uri;
	private boolean debug;
	private File documentRoot = new File("www");
	private File accessLog = new File("access.log");

	public static Options parse(String[] args) {
		Options opts = new Options();

		try {
			if (args.length > 1) {
				printUsage();
				System.exit(Http.EXIT_FAILURE);
			}

			if (args.length == 1)
				opts.service = new Service(args[0]);
			else
				opts.service = new Service(Http.HTTP_SERVICE);

		} catch (UnknownServiceException e) {
			System.err.print(e.getMessage());
			System.exit(Http.EXIT_FAILURE);
		}

		return opts;
	}

	private static void printUsage() {
		System.err.println("Usage: HttpServer [service]");
	}

	public String uri() {
		return uri;
	}

	public String dest() {
		return dest;
	}

	public boolean debug() {
		return debug;
	}

	public File documentRoot() {
		return documentRoot;
	}

	public File accessLog() {
		return accessLog;
	}

	public Service service() {
		return service;
	}
}
