package org.apache.zookeeper;

public class Host {
	private String host;
	private int port;
	
	public Host(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public String getHostName() {
		return host;
	}

	public void setHostName(String hostName) {
		this.host = hostName;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
