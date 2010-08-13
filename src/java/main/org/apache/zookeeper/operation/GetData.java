package org.apache.zookeeper.operation;

import org.apache.jute.Record;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.proto.GetDataRequest;
import org.apache.zookeeper.proto.GetDataResponse;

public class GetData extends Operation {
	private Path path;
	private boolean watching;
	private Watcher watcher;
	private byte[] data;
	
	public GetData(Path path) {
		this(path, null);
	}
	
	public GetData(Path path, boolean watch) {
		this(path, null);
		this.watching = watch;
	}
	
	public GetData(Path path, Watcher watcher) {
		super(path);
		this.watcher = watcher;
	}

	@Override
	public Record createRequest(ChrootPathTranslator chroot) {
		final String serverPath = chroot.toServer(path).toString();
		
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
	public int getRequestOpCode() {
		return ZooDefs.OpCode.getData;
	}

	@Override
	public boolean isWatching() {
		return watching;
	}

	@Override
	public Watcher getWatcher() {
		return watcher;
	}

	public byte[] getData() {
		return data;
	}
}
