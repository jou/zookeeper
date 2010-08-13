package org.apache.zookeeper.operation;

import org.apache.jute.Record;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.proto.GetDataRequest;
import org.apache.zookeeper.proto.GetDataResponse;

public class GetData extends Operation {
	private Path path;
	private boolean watching = false;
	private Watcher watcher = null;
	private byte[] data;
	
	public GetData(Path path) {
		super(path);
	}
	
	public GetData(Path path, boolean watch) {
		this(path);
		this.watching = watch;
	}
	
	public GetData(Path path, Watcher watcher) {
		this(path);
		this.watcher = watcher;
		this.watching = true;
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

	public byte[] getData() {
		return data;
	}
	
	// Return a ExistsWatchRegistration object, if there is a order for watching
	@Override
	private WatchRegistration getWatchRegistration(serverPath) {
		if(watching) {
			return new DataWatchRegistration(watcher, serverPath);
		}
		return null;	
	}
}
