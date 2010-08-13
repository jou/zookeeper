package org.apache.zookeeper.operation;

import org.apache.jute.Record;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.proto.ReplyHeader;

public abstract class Operation {
	protected Path path;
	
	protected Operation(Path path) {
		this.path = path;
	}
	
	public Path getPath() {
		return path;
	}
	
	public abstract Record createRequest(ChrootPathTranslator chroot);
	
	public abstract Record createResponse();
  
	public abstract void receiveResponse(ChrootPathTranslator chroot, Record response);
  
	public abstract int getRequestOpCode();

	public void checkReplyHeader(ReplyHeader header) throws KeeperException {
		if(header.getErr() != 0) {
			throw KeeperException.create(KeeperException.Code.get(header.getErr()), path.toString());
		}
	}
}
