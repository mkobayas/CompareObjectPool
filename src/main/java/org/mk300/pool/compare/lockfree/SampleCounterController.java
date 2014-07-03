package org.mk300.pool.compare.lockfree;

import org.mk300.pool.ObjectController;

import java.util.concurrent.atomic.AtomicLong;

import org.mk300.pool.compare.SampleCounter;


public class SampleCounterController implements ObjectController<SampleCounter> {

	static AtomicLong total = new AtomicLong(0);
	
	@Override
	public SampleCounter create() {
		return new SampleCounter();
	}

	@Override
	public void destory(SampleCounter obj) {
//		System.out.println(String.format("counter = %12s", String.format("%,d", obj.get())));
		total.addAndGet(obj.get());
	}

	public long getTotal() {
		return total.get();
	}
}
