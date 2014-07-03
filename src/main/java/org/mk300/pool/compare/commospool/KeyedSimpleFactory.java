package org.mk300.pool.compare.commospool;


import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.pool.BaseKeyedPoolableObjectFactory;
import org.mk300.pool.compare.SampleCounter;

public class KeyedSimpleFactory extends BaseKeyedPoolableObjectFactory<Integer, SampleCounter> {

	static AtomicLong total = new AtomicLong(0);

	@Override
	public SampleCounter makeObject(Integer key) throws Exception {
		return new SampleCounter();
	}
	
	public void destroyObject(Integer key, SampleCounter arg0) throws Exception {
//		System.out.println(String.format("counter = %12s", String.format("%,d", arg0.get())));
		total.addAndGet(arg0.get());	
	}

	
	public long getTotal() {
		return total.get();
	}

}
