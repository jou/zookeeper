package org.apache.zookeeper.operation;

import java.util.List;

import org.apache.jute.Record;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.common.PathUtils;
import org.apache.zookeeper.proto.GetChildrenRequest;
import org.apache.zookeeper.proto.GetChildrenResponse;
import org.apache.zookeeper.proto.ReplyHeader;

public class GetChildren extends Operation {
	private String path;
	private Watcher watcher;
	private List<String> children;
	
	public GetChildren(String path) {
		this(path, null);
	}
	
	public GetChildren(String path, Watcher watcher) {
		this.path = path;
		this.watcher = watcher;
	}
	
	@Override
	public String getPath() {
		return this.path;
	}

	@Override
	public Record createRequest(ChrootPathTranslator chroot) {
		final String clientPath = path;
		PathUtils.validatePath(clientPath);
		final String serverPath;
		
		if(clientPath.equals("/")) {
			serverPath = clientPath;
		} else {
			serverPath = chroot.toServer(clientPath).toString();
		}
		
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
	public void checkReplyHeader(ReplyHeader header) throws KeeperException {
		if(header.getErr() != 0) {
			throw KeeperException.create(KeeperException.Code.get(header.getErr()), path);
		}
	}

	@Override
	public int getRequestOpCode() {
		return ZooDefs.OpCode.getChildren;
	}

	public List<String> getChildren() {
		return children;
	}
}
