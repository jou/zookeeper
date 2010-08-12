package org.apache.zookeeper.operation;

import org.apache.jute.Record;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.common.PathUtils;
import org.apache.zookeeper.proto.DeleteRequest;
import org.apache.zookeeper.proto.ReplyHeader;

public class Delete extends Operation {
	
	private String path;
	private int version;
	
	public Delete(String path, int version) {
		super();
		this.path = path;
		this.version = version;
	}

	@Override
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
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
		
		DeleteRequest request = new DeleteRequest();
		request.setPath(serverPath);
		request.setVersion(version);
		
		return request;
	}

	@Override
	public void receiveResponse(ChrootPathTranslator chroot, Record response) {
		// Nothing to do		
	}

	@Override
	public int getRequestOpCode() {
		return ZooDefs.OpCode.delete;
	}

	@Override
	public void checkReplyHeader(ReplyHeader header) throws KeeperException {
		if(header.getErr() != 0) {
			throw KeeperException.create(KeeperException.Code.get(header.getErr()), path);
		}		
	}

	@Override
	public Record createResponse() {
		return null;
	}

}
