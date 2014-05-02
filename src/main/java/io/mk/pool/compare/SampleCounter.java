package io.mk.pool.compare;

public class SampleCounter {

	private volatile int count = 0;
	
	public void increment() {
		count++;
	}
	
	public int get() {
		return count;
	}
}
