package org.apache.zookeeper.operation;

import java.util.List;

import org.apache.jute.Record;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.InvalidACLException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.proto.CreateRequest;
import org.apache.zookeeper.proto.CreateResponse;
import org.apache.zookeeper.proto.ReplyHeader;

public class Create extends Operation {

	private Path path;
	private byte data[];
	private List<ACL> acl;
	private CreateMode createMode;
	private Path responsePath;
	
	public Create(Path path, byte[] data, List<ACL> acl, CreateMode createMode) throws InvalidACLException {
		super();
		this.path = path;
		this.data = data;	
		this.createMode = createMode;
		this.responsePath = null;
		setAcl(acl);
	}
	
	@Override
	public Path getPath() {
		return path;
	}
	
	public void setPath(Path path) {
		this.path = path;
	}
	
	public byte[] getData() {
		return data;
	}
	
	public void setData(byte[] data) {
		this.data = data;
	}
	
	public List<ACL> getAcl() {
		return acl;
	}
	
	public void setAcl(List<ACL> acl) throws InvalidACLException {
		if (acl != null && acl.size() == 0) {
            throw new KeeperException.InvalidACLException();
        }
		this.acl = acl;
	}
	
	public CreateMode getCreateMode() {
		return createMode;
	}
	
	public void setCreateMode(CreateMode createMode) {
		this.createMode = createMode;
	}
	
	public Path getResponsePath() {
		return responsePath;
	}
	

	@Override
	public Record createRequest(ChrootPathTranslator chroot) {

		Path serverPath = chroot.toServer(path);
		CreateRequest request = new CreateRequest();
		
		request.setData(data);
		request.setFlags(createMode.toFlag());
		request.setPath(serverPath.toString());
		request.setAcl(acl);
		
		return request;
	}

	@Override
	public void receiveResponse(ChrootPathTranslator chroot, Record response) {	
		CreateResponse createResponse = (CreateResponse) response;
		this.responsePath = chroot.toServer(createResponse.getPath());
	}

	@Override
	public int getRequestOpCode() {
		return ZooDefs.OpCode.create;
	}

	@Override
	public void checkReplyHeader(ReplyHeader header) throws KeeperException {
		if(header.getErr() != 0) {
			throw KeeperException.create(KeeperException.Code.get(header.getErr()), path.toString());
		}
	}

	@Override
	public Record createResponse() {
		return new CreateResponse();
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
