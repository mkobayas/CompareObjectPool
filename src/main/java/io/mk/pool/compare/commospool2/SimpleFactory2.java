package io.mk.pool.compare.commospool2;

import java.util.concurrent.atomic.AtomicLong;

import io.mk.pool.compare.SampleCounter;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class SimpleFactory2 extends BasePooledObjectFactory<SampleCounter> {

	static AtomicLong total = new AtomicLong(0);
	
	@Override
	public SampleCounter create() throws Exception {
		return new SampleCounter();
	}

	@Override
	public PooledObject<SampleCounter> wrap(SampleCounter arg0) {
		return new DefaultPooledObject<SampleCounter>(arg0);
	}

	@Override
	public void destroyObject(PooledObject<SampleCounter> p) throws Exception {
//		System.out.println(String.format("counter = %12s", String.format("%,d", p.getObject().get())));
		total.addAndGet(p.getObject().get());	
		super.destroyObject(p);
	}

	public long getTotal() {
		return total.get();
	}
	
}
