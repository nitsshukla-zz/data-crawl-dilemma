package latch;

import java.util.concurrent.atomic.*;

import com.learn.logging.optimised.Trie;

public class CustomLatch {
	AtomicInteger threadCount;
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
			Thread.sleep(5000);
			}
			executeOnFinish();
	}
	public void executeOnFinish() {
		trie.destroy();
	}
}
	 
