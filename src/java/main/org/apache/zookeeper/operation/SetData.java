package org.apache.zookeeper.operation;

import org.apache.jute.Record;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.proto.SetDataRequest;
import org.apache.zookeeper.proto.SetDataResponse;

public class SetData extends Operation {
	private int version;
	private byte[] data;
	private Stat stat;
	
	public SetData(Path path, byte[] data, int version) {
		super(path);
		this.data = data;
		this.version = version;
	}

	@Override
	public Record createRequest(ChrootPathTranslator chroot) {
		final String serverPath = chroot.toServer(path).toString();
		
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
	public int getRequestOpCode() {
		return ZooDefs.OpCode.setData;
	}

	@Override
	public boolean isWatching() {
		return false;
	}

	@Override
	public Watcher getWatcher() {
		return null;
	}

	public Stat getStat() {
		return stat;
	}
}
