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

	public static Options parse(String[] args) {
		Options opts = new Options();

		try {
			if (args.length == 0 || args.length > 1) {
				printUsage();
				System.exit(Http.EXIT_FAILURE);
			}

			if (args.length == 1) {
				opts.service = new Service(args[0]);
			}
		} catch (UnknownServiceException e) {
			// TODO
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
	
	public Service service() {
		return service;
	}
}
