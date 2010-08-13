package org.apache.zookeeper.operation;

import org.apache.jute.Record;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.common.PathUtils;
import org.apache.zookeeper.proto.GetDataRequest;
import org.apache.zookeeper.proto.GetDataResponse;
import org.apache.zookeeper.proto.ReplyHeader;

public class GetData extends Operation {
	private String path;
	private Watcher watcher;
	private byte[] data;
	
	public GetData(String path, Watcher watcher) {
		super();
		this.path = path;
		this.watcher = watcher;
	}

	@Override
	public String getPath() {
		return path;
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
		
		GetDataRequest request = new GetDataRequest();
		request.setPath(serverPath);
		request.setWatch(watcher != null);
		
		return request;
	}

	@Override
	public Record createResponse() {
		return new GetDataResponse();
	}

	@Override
	public void receiveResponse(ChrootPathTranslator chroot, Record response) {
		GetDataResponse getDataResponse = (GetDataResponse)response;
		
		this.data = getDataResponse.getData();
	}

	@Override
	public void checkReplyHeader(ReplyHeader header) throws KeeperException {
		if(header.getErr() != 0) {
			throw KeeperException.create(KeeperException.Code.get(header.getErr()), path);
		}
	}

	@Override
	public int getRequestOpCode() {
		return ZooDefs.OpCode.getData;
	}

	public byte[] getData() {
		return data;
	}
}
