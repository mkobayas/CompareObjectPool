package org.mk300.pool.compare.commospool;


import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.mk300.pool.compare.SampleCounter;

public class SimpleFactory extends BasePoolableObjectFactory<SampleCounter> {

	static AtomicLong total = new AtomicLong(0);

	@Override
	public SampleCounter makeObject() throws Exception {
		return new SampleCounter();
	}
	
	public void destroyObject(SampleCounter arg0) throws Exception {
//		System.out.println(String.format("counter = %12s", String.format("%,d", arg0.get())));
		total.addAndGet(arg0.get());	
	}

	
	public long getTotal() {
		return total.get();
	}

}
