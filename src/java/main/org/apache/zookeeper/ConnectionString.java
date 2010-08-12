package org.apache.zookeeper;

import org.apache.zookeeper.operation.ChrootPathTranslator;
import org.apache.zookeeper.operation.Path;

public class ConnectionString {
	private HostList hosts;
	private ChrootPathTranslator chroot;

	public ConnectionString(String hosts) {
        // parse out chroot, if any
        int off = hosts.indexOf('/');
        if (off >= 0) {
            String parsedChrootPath = hosts.substring(off);
            // ignore "/" chroot spec, same as null
            if (parsedChrootPath.length() != 1) {
                this.chroot = new ChrootPathTranslator(new Path(parsedChrootPath));
            }
            hosts = hosts.substring(0, off);
        }

        String hostsList[] = hosts.split(",");
        
        for (String host : hostsList) {
            int port = 2181;
            int pidx = host.lastIndexOf(':');
            if (pidx >= 0) {
                // otherwise : is at the end of the string, ignore
                if (pidx < host.length() - 1) {
                    port = Integer.parseInt(host.substring(pidx + 1));
                }
                host = host.substring(0, pidx);
            }
            this.hosts.add(new Host(host, port));
        }
	}

	public ChrootPathTranslator getChroot() {
		return chroot;
	}

	public void setChroot(ChrootPathTranslator chroot) {
		this.chroot = chroot;
	}

	public HostList getHosts() {
		return hosts;
	}
	
	public void addHost(Host host) {
		this.hosts.add(host);
	}
}
