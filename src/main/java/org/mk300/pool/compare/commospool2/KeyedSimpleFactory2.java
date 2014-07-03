package org.mk300.pool.compare.commospool2;


import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.mk300.pool.compare.SampleCounter;

public class KeyedSimpleFactory2 extends BaseKeyedPooledObjectFactory<Integer, SampleCounter> {

	static AtomicLong total = new AtomicLong(0);

	@Override
	public void destroyObject(Integer key, PooledObject<SampleCounter> p) throws Exception {
		total.addAndGet(p.getObject().get());	
	}

	
	public long getTotal() {
		return total.get();
	}

	@Override
	public SampleCounter create(Integer key) throws Exception {
		return new SampleCounter();
	}

	@Override
	public PooledObject<SampleCounter> wrap(SampleCounter value) {
		return new DefaultPooledObject<SampleCounter>(value);
	}

}
