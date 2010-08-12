package org.apache.zookeeper.operation;

import org.apache.jute.Record;
import org.apache.zookeeper.ClientCnxn;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.proto.ReplyHeader;

public abstract class Operation {
  
	public abstract String getPath();
	
	public abstract Record createRequest(ChrootPathTranslator chroot);
	
	public abstract Record createResponse();
  
	public abstract void receiveResponse(ChrootPathTranslator chroot, Record response);
  
	public abstract void checkReplyHeader(ReplyHeader header) throws KeeperException;
  
	public abstract int getRequestOpCode();
   
}
