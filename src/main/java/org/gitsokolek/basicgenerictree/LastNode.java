package org.gitsokolek.basicgenerictree;

import org.gitsokolek.basicgenerictree.interfaces.MetaData;

public abstract class LastNode<T extends MetaData> {
	protected final T metaData;
	private Node<T> parent;

	protected LastNode(T metaData) {
		this.metaData = metaData;
	}

	protected void setParent(Node<T> parent) {
		this.parent = parent;
	}

	public Node<T> getParent() {
		return parent;
	}

	public T getMetaData() {
		return metaData;
	}
}
