package org.mk300.pool.compare.lockfree;

import org.mk300.pool.ObjectController;
import org.mk300.pool.compare.SampleCounter;

public class KeyedSampleCounterController implements ObjectController<SampleCounter> {
	
	@SuppressWarnings("unused")
	private final int key;
	
	public KeyedSampleCounterController(int key) {
		this.key = key;
	}
	
	@Override
	public SampleCounter create() {
		return new SampleCounter();
	}

	@Override
	public void destory(SampleCounter obj) {
//		System.out.println(String.format("Key= %3s, Count : %12s", key, String.format("%,d", obj.get() )));
	}

}
