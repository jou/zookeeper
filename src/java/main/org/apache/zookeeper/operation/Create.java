package org.apache.zookeeper.operation;

import org.apache.zookeeper.CreateMode;
import java.util.List;
import org.apache.zookeeper.data.ACL;

public class Create extends Operation{

	private String path;
	private byte data[];
	private List<ACL> acl;
	private CreateMode createMode;
	
	public Create(String path, byte[] data, List<ACL> acl, CreateMode createMode) {
		super();
		this.path = path;
		this.data = data;
		this.acl = acl;
		this.createMode = createMode;
	}
	
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
}
