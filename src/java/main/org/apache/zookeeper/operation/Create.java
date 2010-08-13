package org.apache.zookeeper.operation;

import org.apache.jute.Record;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;

import java.util.List;

import org.apache.zookeeper.common.PathUtils;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.proto.CreateRequest;
import org.apache.zookeeper.proto.CreateResponse;

import org.apache.zookeeper.proto.ReplyHeader;

public class Create extends Operation {

	private String path;
	private byte data[];
	private List<ACL> acl;
	private CreateMode createMode;
	private Path responsePath;
	
	public Create(String path, byte[] data, List<ACL> acl, CreateMode createMode) {
		super();
		this.path = path;
		this.data = data;
		this.acl = acl;
		this.createMode = createMode;
		this.responsePath = null;
	}
	
	@Override
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
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
	
	public void setAcl(List<ACL> acl) {
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
		final String clientPath = path;
		final String serverPath = chroot.toServer(path).toString();
		
		PathUtils.validatePath(clientPath, createMode.isSequential());
		CreateRequest request = new CreateRequest();
		request.setData(data);
		request.setFlags(createMode.toFlag());
		request.setPath(serverPath);
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
			throw KeeperException.create(KeeperException.Code.get(header.getErr()), path);
		}
	}

	@Override
	public Record createResponse() {
		return new CreateResponse();
	}
}
