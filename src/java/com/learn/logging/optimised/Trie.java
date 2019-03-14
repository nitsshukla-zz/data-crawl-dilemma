package com.learn.logging.optimised;

public class Trie {
	private TrieNode root;

	public Trie() {
		root = new TrieNode();
	}

	public void insert(String word) {
		TrieNode root = this.root;
		for (int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			if (root.map.containsKey(c)) {
				root = root.map.get(c);
			} else {
				TrieNode n = new TrieNode();
				root.map.put(c, n);
				root = n;
			}
		}
		root.isWordTerminated = true;
	}

	public boolean search(String word) {
		TrieNode root = this.root;
		for (int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			if (root.map.containsKey(c)) {
				root = root.map.get(c);
			} else
				return false;
		}
		return root.isWordTerminated;
	}

	public void destroy() {
		root.map.clear();
		root = null;
	}
}
