package org.apache.zookeeper;

import org.apache.jute.Record;
import org.apache.zookeeper.operation.ChrootPathTranslator;
import org.apache.zookeeper.operation.Operation;
import org.apache.zookeeper.proto.ReplyHeader;
import org.apache.zookeeper.proto.RequestHeader;

public class Executor {
 	
	private ClientCnxn connection;
	private ChrootPathTranslator chroot;
	
	public Executor(ClientCnxn connection, ChrootPathTranslator chroot) {
		this.connection = connection;
		this.chroot = chroot;		
	}
	
	public ClientCnxn getConnection() {
		return connection;
	}
	
	public ChrootPathTranslator getChroot() {
		return chroot;
	}
	
	// function for synchron operation
	public void execute(Operation op) throws InterruptedException, KeeperException {
		Record response = op.createResponse();
		Record request = op.createRequest(chroot);
		RequestHeader header = new RequestHeader();

		header.setType(op.getRequestOpCode());
		ReplyHeader reply = connection.submitRequest(header, request , response, null);
		op.checkReplyHeader(reply);
		op.receiveResponse(chroot, response);		
	}
	
	// function for asynchron operation
	public void send(Operation op, AsyncCallback cb, Object context) {
		Record response = op.createResponse();
		Record request = op.createRequest(chroot);
		RequestHeader header = new RequestHeader();
		ReplyHeader reply = new ReplyHeader();
		String clientPath = op.getPath();
		String serverPath = chroot.toServer(clientPath).toString();
	
		header.setType(op.getRequestOpCode()); 
		connection.queuePacket(header, reply, request, response, cb, clientPath, serverPath, context, null);
	}

}
