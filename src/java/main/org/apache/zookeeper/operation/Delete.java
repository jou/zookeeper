package org.apache.zookeeper.operation;

import org.apache.jute.Record;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.proto.DeleteRequest;
import org.apache.zookeeper.proto.ReplyHeader;

public class Delete extends Operation {
	
	private Path path;
	private int version;
	
	public Delete(Path path, int version) {
		super();
		this.path = path;
		this.version = version;
	}

	public void setPath(Path path) {
		this.path = path;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
	@Override
	public Path getPath() {
		return path;
	}

	@Override
	public Record createRequest(ChrootPathTranslator chroot) {
		final String serverPath = chroot.toServer(path).toString();

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
			throw KeeperException.create(KeeperException.Code.get(header.getErr()), path.toString());
		}		
	}

	@Override
	public Record createResponse() {
		return null;
	}

	@Override
	public boolean isWatching() {
		return false;
	}

	@Override
	public Watcher getWatcher() {
		return null;
	}

}
