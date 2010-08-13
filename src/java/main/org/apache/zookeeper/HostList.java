package org.apache.zookeeper;

import java.util.ArrayList;
import java.util.List;

/**
 * A list of hosts to connect to as {@link Host} objects.
 */
public class HostList extends ArrayList<Host> {
	public HostList() {
		super();
	}
	
	public HostList(List<Host> hosts) {
		super(hosts);
	}

	private static final long serialVersionUID = 1L;
}
