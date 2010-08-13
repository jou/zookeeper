package org.apache.zookeeper;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Register a watcher for a particular path.
 */
abstract class WatchRegistration {
    private Watcher watcher;
    private String serverPath;
    
    public WatchRegistration(Watcher watcher, String serverPath)
    {
        this.watcher = watcher;
        this.serverPath = serverPath;
    }

    abstract protected Map<String, Set<Watcher>> getWatches(ZKWatchManager watchManager, int rc);

    /**
     * Register the watcher with the set of watches on path.
     * @param rc the result code of the operation that attempted to
     * add the watch on the path.
     */
    public void register( ZKWatchManager watchManager, int rc) {
        if (shouldAddWatch(rc)) {
            Map<String, Set<Watcher>> watches = getWatches( watchManager, rc);
            synchronized(watches) {
                Set<Watcher> watchers = watches.get(serverPath);
                if (watchers == null) {
                    watchers = new HashSet<Watcher>();
                    watches.put(serverPath, watchers);
                }
                watchers.add(watcher != null ? watcher : watchManager.getDefaultWatcher());
            }
        }
    }
    
    /**
     * Determine whether the watch should be added based on return code.
     * @param rc the result code of the operation that attempted to add the
     * watch on the node
     * @return true if the watch should be added, otw false
     */
    protected boolean shouldAddWatch(int rc) {
        return rc == 0;
    }
}

/** 
 * Handle the special case of exists watches - they add a watcher
 * even in the case where NONODE result code is returned.
 */
class ExistsWatchRegistration extends WatchRegistration {
    public ExistsWatchRegistration(Watcher watcher, String serverPath) {
        super(watcher, serverPath);
    }

    @Override
    protected Map<String, Set<Watcher>> getWatches(ZKWatchManager watchManager, int rc) {
        return rc == 0 ?  watchManager.getWatches(ZKWatchManager.WATCHTYPE.DATA) : watchManager.getWatches(ZKWatchManager.WATCHTYPE.EXIST);
    }

    @Override
    protected boolean shouldAddWatch(int rc) {
        return rc == 0 || rc == KeeperException.Code.NONODE.intValue();
    }
}

class DataWatchRegistration extends WatchRegistration {
    public DataWatchRegistration(Watcher watcher, String serverPath) {
        super(watcher, serverPath);
    }

    @Override
    protected Map<String, Set<Watcher>> getWatches(ZKWatchManager watchManager, int rc) {
        return watchManager.getWatches(ZKWatchManager.WATCHTYPE.DATA);
    }
}

class ChildWatchRegistration extends WatchRegistration {
    public ChildWatchRegistration(ZKWatchManager watchManager, Watcher watcher, String serverPath) {
        super(watcher, serverPath);
    }

    @Override
    protected Map<String, Set<Watcher>> getWatches(ZKWatchManager watchManager, int rc) {
        return watchManager.getWatches(ZKWatchManager.WATCHTYPE.CHILD);
    }
}