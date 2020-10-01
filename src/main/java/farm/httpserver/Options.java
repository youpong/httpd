package farm.httpserver;

import java.io.File;

public class Options {

	private String dest = "-";
	private String uri;
	private boolean debug;
	private File documentRoot = new File("www");

	public static Options parse(String[] args) {
		Options opts = new Options();
		if (args.length == 0 || args.length > 2) {
			System.out.println("http-client uri [dest]");
			System.exit(1);
		}
		if (args.length == 2) {
			opts.dest = args[1];
		}
		opts.uri = args[0];

		return opts;
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
}
