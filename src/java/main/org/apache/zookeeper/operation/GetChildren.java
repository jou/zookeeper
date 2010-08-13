package org.apache.zookeeper.operation;

import java.util.List;

import org.apache.jute.Record;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.proto.GetChildrenRequest;
import org.apache.zookeeper.proto.GetChildrenResponse;

public class GetChildren extends Operation {
	private Watcher watcher;
	private boolean watching;
	private List<String> children;
	
	public GetChildren(Path path) {
		this(path, null);
	}
	
	public GetChildren(Path path, boolean watch) {
		this(path, null);
		this.watching = watch;
	}
	
	public GetChildren(Path path, Watcher watcher) {
		super(path);
		this.watcher = watcher;
	}
	
	@Override
	public Record createRequest(ChrootPathTranslator chroot) {
		final String serverPath = chroot.toServer(path).toString();
	
		GetChildrenRequest request = new GetChildrenRequest();
		request.setPath(serverPath);
		request.setWatch(watcher != null);
		
		return request;
	}

	@Override
	public Record createResponse() {
		return new GetChildrenResponse();
	}

	@Override
	public void receiveResponse(ChrootPathTranslator chroot, Record response) {
		GetChildrenResponse getChildrenResponse = (GetChildrenResponse)response;
		this.children = getChildrenResponse.getChildren();
	}

	@Override
	public int getRequestOpCode() {
		return ZooDefs.OpCode.getChildren;
	}

	@Override
	public boolean isWatching() {
		return watching;
	}

	@Override
	public Watcher getWatcher() {
		return watcher;
	}

	public List<String> getChildren() {
		return children;
	}
}
