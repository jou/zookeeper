package org.apache.zookeeper.operation;

import org.apache.zookeeper.ClientCnxn;

public abstract class Operation {
  
  protected abstract Request createRequest( ChrootPathTranslator chroot );
  
  protected abstract void receiveResponse( ChrootPathTranslator chroot, Response )
  
}
