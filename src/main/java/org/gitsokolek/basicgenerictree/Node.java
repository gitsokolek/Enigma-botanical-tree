package org.gitsokolek.basicgenerictree;

import org.gitsokolek.basicgenerictree.interfaces.MetaData;

import java.util.*;

public class Node<T extends MetaData> extends LastNode<T> {

	private final List<LastNode<T>> nodes = new ArrayList<>();

	public Node(T metaData) {
		super(metaData);
	}

	public List<LastNode<T>> childrenView() {
		return Collections.unmodifiableList(nodes);
	}

	public boolean attach(LastNode<T> child) {
		Objects.requireNonNull(child, "Node cannot be null");
		if (child == this) return false;
		if (child instanceof Node) {
			@SuppressWarnings("unchecked")
			Node<T> childNode = (Node<T>) child;
			if (contains(childNode, this)) return false; // anti-cycle
		}
		child.setParent(this);
		nodes.add(child);
		return true;
	}


	private static <T extends MetaData> boolean contains(Node<T> root, Node<T> target) {
		Set<Node<T>> visited = Collections.newSetFromMap(new IdentityHashMap<>());
		ArrayDeque<Node<T>> stack = new ArrayDeque<>();

		if (enqueueChildrenAndCheck(root, target, visited, stack)) return true;

		while (!stack.isEmpty()) {
			Node<T> cur = stack.pop();
			if (enqueueChildrenAndCheck(cur, target, visited, stack)) return true;
		}
		return false;
	}

	private static <T extends MetaData> boolean enqueueChildrenAndCheck(
			Node<T> parent,
			Node<T> target,
			Set<Node<T>> visited,
			ArrayDeque<Node<T>> stack
																	   ) {
		for (LastNode<T> ch : parent.nodes) {
			if (isTarget(ch, target)) return true;
			Node<T> n = asNode(ch);
			if (n != null && visited.add(n)) {
				stack.push(n);
			}
		}
		return false;
	}

	private static <T extends MetaData> boolean isTarget(LastNode<T> child, Node<T> target) {
		return child == target;
	}

	private static <T extends MetaData> Node<T> asNode(LastNode<T> child) {
		if (child instanceof Node) {
			@SuppressWarnings("unchecked")
			Node<T> n = (Node<T>) child;
			return n;
		}
		return null;
	}
}
