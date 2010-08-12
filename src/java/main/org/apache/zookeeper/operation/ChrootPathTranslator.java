package org.apache.zookeeper.operation;

public class ChrootPathTranslator {
	private Path basePath;

	public ChrootPathTranslator() {
		this(new Path());
	}

	public ChrootPathTranslator(Path basePath) {
		this.basePath = basePath;
	}

	public Path toServer(String path) {
		return toServer(new Path(path));
	}

	public Path toServer(Path path) {
		return path.prepend(basePath);
	}

	public Path fromServer(String path) {
		return fromServer(new Path(path));
	}

	public Path fromServer(Path path) {
		return path.removePrepend(basePath);
	}
}
