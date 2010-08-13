package org.apache.zookeeper;

import org.apache.jute.Record;
import org.apache.zookeeper.ZooKeeper.ExistsWatchRegistration;
import org.apache.zookeeper.ZooKeeper.WatchRegistration;
import org.apache.zookeeper.operation.ChrootPathTranslator;
import org.apache.zookeeper.operation.Operation;
import org.apache.zookeeper.operation.Path;
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
		String clientPath = op.getPath().toString();
		Record response = op.createResponse();
    	Record request = op.createRequest(chroot);
		RequestHeader header = new RequestHeader();
		WatchRegistration watchRegistration = getWatchRegistration(op,clientPath);

		header.setType(op.getRequestOpCode());
		ReplyHeader reply = connection.submitRequest(header, request , response, watchRegistration);
		op.checkReplyHeader(reply);
		op.receiveResponse(chroot, response);		
	}

	// function for asynchron operation
	public void send(Operation op, AsyncCallback cb, Object context) {
		Record response = op.createResponse();
		Record request = op.createRequest(chroot);
		RequestHeader header = new RequestHeader();
		ReplyHeader reply = new ReplyHeader();
		String clientPath = op.getPath().toString();
		String serverPath = chroot.toServer(clientPath).toString();
		WatchRegistration watchRegistration = getWatchRegistration(op, clientPath);
	
		header.setType(op.getRequestOpCode()); 
		connection.queuePacket(header, reply, request, response, cb, clientPath, serverPath, context, watchRegistration);
	}
    
	// Return a ExistsWatchRegistration object, if there is a order for watching
	private ExistsWatchRegistration getWatchRegistration(Operation op, String clientPath) {
		if(op.isWatching()) {
			return new ExistsWatchRegistration(op.getWatcher(), chroot.toServer(clientPath));
		}
		return null;	
	}
}
