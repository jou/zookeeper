package org.apache.zookeeper.operation;

import org.apache.zookeeper.ClientCnxn;

public abstract class Operation {
	
  public void execute( ClientCnxn cnxn ){
	  
  }
  
  protected abstract Request createRequest();
}
