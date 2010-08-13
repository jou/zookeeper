package org.apache.zookeeper;

/**
 * This class represents a host with its port to connect to.
 */
public class Host {
	private String host;
	private int port;
	
	public Host(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
