package latch;

import com.learn.logging.optimised.Trie;

public class CustomLatch {
	int threadCount;
	Trie trie;

	public void init(Trie trie) {
		threadCount = 0;
		this.trie = trie;
	}
	public void up() {
		threadCount++;
	}
	public void down() {
		threadCount--;
	}
	public void readyToFinish() throws InterruptedException {
		while(threadCount!=0) {
			Thread.sleep(50);
			}
			executeOnFinish();
	}
	public void executeOnFinish() {
		trie.destroy();
	}
}
	 