package org.apache.zookeeper.operation;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.zookeeper.common.PathUtils;

public class Path implements Cloneable {
	/**
	 * Path separator for ZNode paths.
	 */
	public static final String ZNODE_PATH_SEPARATOR = "/";
	
	/**
	 * Parts the path is composed of.
	 */
    private LinkedList<String> pathParts;
    
    /**
     * Join the single parts of the paths with ZNODE_PATH_SEPARATOR.
     * 
     * @param parts
     * @return
     */
    private static String join(List<String> parts) {
    	int capacity = 1;
    	int delimLength = 1;
    	String delimiter = ZNODE_PATH_SEPARATOR;
    	
    	// Determine result string length
    	Iterator<String> iter = parts.iterator();
    	if (iter.hasNext()) {
    	    capacity += iter.next().length() + delimLength;
    	}

    	StringBuilder buffer = new StringBuilder(capacity);
    	
    	// Paths are always absolute
    	buffer.append(delimiter);
    	
    	// Build joined string
    	iter = parts.iterator();
    	if (iter.hasNext()) {
    	    buffer.append(iter.next());
    	    while (iter.hasNext()) {
    		buffer.append(delimiter);
    		buffer.append(iter.next());
    	    }
    	}
    	return buffer.toString();
    }
    
    /**
     * Split a path by ZNODE_PATH_SEPARATOR.
     * 
     * @param path
     * @return
     */
    private static LinkedList<String> split(String path) {
    	String[] splitPath = path.split(ZNODE_PATH_SEPARATOR);
    	return new LinkedList<String>(Arrays.asList(splitPath));
    }
	
    /**
     * Constructs a path to the root.
     */
	public Path() {
		this(new LinkedList<String>());
	}

	/**
	 * Constructs a path using a string.
	 * 
	 * @param path
	 */
	public Path(String path) {
		this(split(path));
	}
	
	/**
	 * Constructs a path from the path of another Path object.
	 * 
	 * @param path
	 */
	public Path(Path path) {
		this(path.pathParts);
	}
	
	/**
	 * Constructs a path from a list of path parts.
	 * 
	 * @param linkedList
	 */
	protected Path(List<String> pathParts) {
		LinkedList<String> linkedList = new LinkedList<String>(pathParts);
		
		if(linkedList.size() > 0) {
			while(linkedList.getFirst().isEmpty()) {
				linkedList.removeFirst();
			}
		}
		
		PathUtils.validatePath(join(linkedList));
		
		this.pathParts = linkedList;
	}
	
	/**
	 * Append a path to the current path. 
	 * 
	 * @param path
	 * @return
	 */
	public Path append(String path) {
		return append(new Path(path));
	}
	
	/**
	 * Prepend a path to the current path.
	 * 
	 * @param path
	 * @return
	 */
	public Path prepend(String path) {
		return prepend(new Path(path));
	}
	
	/**
	 * Remove prepended path
	 * 
	 * @param prepend
	 * @return
	 */
	public Path removePrepend(String prepend) {
		return removePrepend(new Path(prepend));
	}

	/**
	 * Append a path to the current path. 
	 * 
	 * @param path
	 * @return
	 */
	public Path append(Path path) {	
		Path clone = new Path(this);
		Iterator<String> newParts = path.pathParts.iterator();
		
		while(newParts.hasNext()) {
			clone.pathParts.addLast(newParts.next());
		}
		
		return clone;
	}
	
	/**
	 * Prepend a path to the current path.
	 * 
	 * @param path
	 * @return
	 */
	public Path prepend(Path path) {
		Path clone = new Path(this);
		Iterator<String> newParts = path.pathParts.descendingIterator();
		
		while(newParts.hasNext()) {
			clone.pathParts.addFirst(newParts.next());
		}
		
		return clone;
	}
	
	/**
	 * Remove prepended path
	 * 
	 * @param prepend
	 * @return
	 */
	public Path removePrepend(Path prepend) {
		Path clone = new Path(this);
		
		LinkedList<String> pathParts = clone.pathParts;
		Iterator<String> prependIterator = prepend.pathParts.iterator();
		
		while(prependIterator.hasNext()) {
			String prependPart = prependIterator.next();
			String myPart = pathParts.removeFirst();
			
			if(!prependPart.equals(myPart)) {
				throw new IllegalArgumentException(
					this.toString() + " is not child of " + prepend.toString()
				);
			}
		}
		
		return clone;
	}

	/**
	 * Parent path.
	 * 
	 * @return
	 */
	public Path parent() {
		Path clone = new Path(this);
		clone.pathParts.remove(clone.pathParts.size() - 1);
		return clone;
	}
	
	public String toString() {
		return join(this.pathParts);
	}
	
	public Object clone() {
		return new Path(this);
	}
}
