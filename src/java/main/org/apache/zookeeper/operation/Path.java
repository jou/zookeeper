package org.apache.zookeeper.operation;

import org.apache.zookeeper.common.PathUtils;

public class Path {
    private String path;

	public Path(String path) {
		super();
		PathUtils.validatePath(path);
		this.path = path;
	}
}
