package com.learn.logging.optimised;

import java.util.HashMap;
import java.util.Map;

class TrieNode {
	Map<Character, TrieNode> map = new HashMap<>();
	boolean isWordTerminated;
}

