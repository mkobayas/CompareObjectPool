package org.mk300.pool.compare.lockfree;

import org.mk300.pool.compare.SampleCounter;

import org.mk300.pool.KeyedObjectControllerFactory;
import org.mk300.pool.ObjectController;

public class KeyedSampleCounterControllerFactory implements KeyedObjectControllerFactory<Integer, SampleCounter> {

	
	@Override
	public ObjectController<SampleCounter> createObjectController(Integer key) {
		
		KeyedSampleCounterController controller = new KeyedSampleCounterController(key); 
		return controller;
	}

}
