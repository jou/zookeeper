package org.apache.zookeeper.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.apache.zookeeper.ZKTestCase;
import org.apache.zookeeper.operation.*;

public class OperationsTest extends ZKTestCase {
	@Test
	public void testPath() {
		String pathSeparator = Path.ZNODE_PATH_SEPARATOR;
		Path p;
		
		// Empty constructor
		p = new Path();
		assertEquals(pathSeparator, p.toString());
		
		// Invalid path
		try {
			new Path("/foo//bar");
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			
		}
		
		// Constructor with string
		p = new Path("/foo");
		assertEquals("/foo", p.toString());
		
		p = new Path("foo");
		assertEquals("/foo", p.toString());
		
		Path fooPath = new Path("foo");
		
		// Append path
		p = fooPath.append("bar");
		assertEquals("/foo/bar", p.toString());
		
		p = fooPath.append(new Path("bar"));
		assertEquals("/foo/bar", p.toString());
		
		// Prepend path
		p = fooPath.prepend("bar");
		assertEquals("/bar/foo", p.toString());
		
		p = fooPath.prepend(new Path("bar"));
		assertEquals("/bar/foo", p.toString());
		
		// Remove from beginning 
		Path longPath = new Path("/foo/bar/baz");
		
		p = longPath.removePrepend("/foo");
		assertEquals("/bar/baz", p.toString());
		
		p = longPath.removePrepend(fooPath);
		assertEquals("/bar/baz", p.toString());
		
		p = longPath.removePrepend("/foo/bar");
		assertEquals("/baz", p.toString());
		
		try {
			longPath.removePrepend("/kazonk");
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			
		}
	}
}
