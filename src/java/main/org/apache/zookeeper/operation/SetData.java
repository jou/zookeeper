package org.apache.zookeeper.operation;

import org.apache.jute.Record;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.common.PathUtils;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.proto.ReplyHeader;
import org.apache.zookeeper.proto.SetDataRequest;
import org.apache.zookeeper.proto.SetDataResponse;

public class SetData extends Operation {
	private String path;
	private int version;
	private byte[] data;
	private Stat stat;
	
	public SetData(String path, int version, byte[] data) {
		super();
		this.path = path;
		this.version = version;
		this.data = data;
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
		
		SetDataRequest request = new SetDataRequest();
		request.setPath(serverPath);
		request.setData(data);
		request.setVersion(version);
		
		return request;
	}

	@Override
	public Record createResponse() {
		return new SetDataResponse();
	}

	@Override
	public void receiveResponse(ChrootPathTranslator chroot, Record response) {
		SetDataResponse setDataResponse = (SetDataResponse)response;
		stat = setDataResponse.getStat();
	}

	@Override
	public void checkReplyHeader(ReplyHeader header) throws KeeperException {
		if(header.getErr() != 0) {
			throw KeeperException.create(KeeperException.Code.get(header.getErr()), path);
		}
	}

	@Override
	public int getRequestOpCode() {
		return ZooDefs.OpCode.setData;
	}

}
