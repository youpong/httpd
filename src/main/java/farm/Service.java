package farm;

import java.util.HashMap;

public class Service {
	private static HashMap<String, Integer> services;
	private String name;
	private int port;

	static {
		services = new HashMap<String, Integer>();
		services.put("daytime", 13);
		services.put("http", 80);
		services.put("http-alt", 8080);
	}

	public Service(String service) throws UnknownServiceException {
		name = service;
		port = 0;

		if (services.containsKey(service)) {
			port = services.get(service);
			return;
		}

		try {
			port = Integer.parseInt(service);
		} catch (Exception e) {
			throw new UnknownServiceException();
		}
	}

	public String getName() {
		return name;
	}

	public int getPort() {
		return port;
	}
}
