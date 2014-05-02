package io.mk.pool.sample.simple.test;

import io.mk.pool.BorrowSt;
import io.mk.pool.KeyedSimpleLockFreeObjectPool;
import io.mk.pool.compare.SampleCounter;
import io.mk.pool.compare.commospool.KeyedSimpleFactory;
import io.mk.pool.compare.commospool2.KeyedSimpleFactory2;
import io.mk.pool.compare.lockfree.KeyedSampleCounterControllerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.pool.impl.GenericKeyedObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;


public class CompareKeyedPoolPefromanceTest {

	@Option(name="-h", usage="help")
	public static boolean help;

	@Option(name="-t", metaVar="th_num", usage="threads count")
	public static int th_num = 100;

	
	@Option(name="-p", metaVar="poolSize", usage="pool Size")
	public static int poolSize = 1;
	

	@Option(name="-d", metaVar="duration", usage="duration(ms)")
	public static long time = 3000;
	
	@Option(name="-s", metaVar="BorrowSt", usage="FIRST | RANDOM | RANDOM_FIRST | THREAD")
	public static BorrowSt st = BorrowSt.RANDOM_FIRST;
	

	@Option(name="-w", metaVar="wait", usage="wait")
	public static long wait = 0;
	
	public static void main(String[] args) throws Exception {
		
		// parse argument
		CompareKeyedPoolPefromanceTest app = new CompareKeyedPoolPefromanceTest();
        CmdLineParser parser = new CmdLineParser(app);
        try {
            parser.parseArgument(args);    
        } catch (CmdLineException e) {
            System.err.println(e);
            parser.printUsage(System.err);
            System.exit(1);
        }
		
        if(help) {
            parser.printUsage(System.err);
            System.exit(1);
        }
		
        testCommonsPool();
        testCommonsPool2();
        testLockFree();
		
    }
	
	private static void testCommonsPool() throws Exception {
		
		final KeyedSimpleFactory factory = new KeyedSimpleFactory();
		final GenericKeyedObjectPool<Integer, SampleCounter> pool = new GenericKeyedObjectPool<Integer, SampleCounter>(factory, poolSize, 
				GenericObjectPool.WHEN_EXHAUSTED_BLOCK,
				GenericObjectPool.DEFAULT_MAX_WAIT,
				poolSize,
				GenericObjectPool.DEFAULT_TEST_ON_BORROW,
				GenericObjectPool.DEFAULT_TEST_ON_RETURN,
				GenericObjectPool.DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS,
				GenericObjectPool.DEFAULT_NUM_TESTS_PER_EVICTION_RUN, 
				GenericObjectPool.DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS,
				GenericObjectPool.DEFAULT_TEST_WHILE_IDLE);
        
        
		System.out.println("START");

		
		final CountDownLatch startLatch = new CountDownLatch(1);
		final CountDownLatch endLatch = new CountDownLatch(th_num);
		final AtomicLong count = new AtomicLong(0);
		for(int i=0; i<th_num ; i++) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {

					long localCount = 0;
					try {
						startLatch.await();
						long start = System.currentTimeMillis();
						long end = start + time;
						while(System.currentTimeMillis() < end) {

							int key = (int)localCount % 10;
							SampleCounter counter = null;
							try {
								counter = pool.borrowObject(key);
								
								counter.increment();
								
								if(wait > 0) {
									Thread.sleep(wait);
								}
								
							} finally {
								if(counter != null) {
									pool.returnObject(key, counter);
								}
							}
							
							localCount++;
						}
						
						end = System.currentTimeMillis();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						count.addAndGet(localCount);
						endLatch.countDown();
					}
				}
				
			});
			t.start();
		}

		long start = System.currentTimeMillis();
		startLatch.countDown();
		endLatch.await();
		long end = System.currentTimeMillis();
		
		long ela = end - start;
		

		System.out.println("pool.getNumActive          = " + pool.getNumActive());
		System.out.println("pool.getNumIdle()          = " + pool.getNumIdle());
		
		pool.close();
		
		
		System.out.println(String.format("Exec time = %s ms" , String.format("%,d", ela )));
		System.out.println(String.format("%4s Threads : throghput = %12s", th_num, String.format("%,d",  (long)(((double)count.get())*1000d/ela) )));
		System.out.println(String.format("Exec sum    = %12s", String.format("%,d",  count.get() )));
		System.out.println(String.format("Counter sum = %12s", String.format("%,d", factory.getTotal())));
		
	}
	

	private static void testCommonsPool2() throws Exception {
		
		org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig config = new org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig();
		config.setMaxTotal(-1);
		config.setMaxIdlePerKey(-1);
		config.setMaxTotalPerKey(poolSize);
		config.setBlockWhenExhausted(true);
		
		final KeyedSimpleFactory2 factory = new KeyedSimpleFactory2();
		final org.apache.commons.pool2.impl.GenericKeyedObjectPool<Integer, SampleCounter> pool = new org.apache.commons.pool2.impl.GenericKeyedObjectPool<Integer, SampleCounter>(factory, config);
		
		System.out.println("START");

		
		final CountDownLatch startLatch = new CountDownLatch(1);
		final CountDownLatch endLatch = new CountDownLatch(th_num);
		final AtomicLong count = new AtomicLong(0);
		for(int i=0; i<th_num ; i++) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {

					long localCount = 0;
					try {
						startLatch.await();
						long start = System.currentTimeMillis();
						long end = start + time;
						while(System.currentTimeMillis() < end) {

							int key = (int)localCount % 10;
							SampleCounter counter = null;
							try {
								counter = pool.borrowObject(key);
								
								counter.increment();
								
								if(wait > 0) {
									Thread.sleep(wait);
								}
								
							} finally {
								if(counter != null) {
									pool.returnObject(key, counter);
								}
							}
							
							localCount++;
						}
						
						end = System.currentTimeMillis();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						count.addAndGet(localCount);
						endLatch.countDown();
					}
				}
				
			});
			t.start();
		}

		long start = System.currentTimeMillis();
		startLatch.countDown();
		endLatch.await();
		long end = System.currentTimeMillis();
		
		long ela = end - start;
		

		System.out.println("pool.getNumActive          = " + pool.getNumActive());
		System.out.println("pool.getNumIdle()          = " + pool.getNumIdle());
		
		pool.close();
		
		
		System.out.println(String.format("Exec time = %s ms" , String.format("%,d", ela )));
		System.out.println(String.format("%4s Threads : throghput = %12s", th_num, String.format("%,d",  (long)(((double)count.get())*1000d/ela) )));
		System.out.println(String.format("Exec sum    = %12s", String.format("%,d",  count.get() )));
		System.out.println(String.format("Counter sum = %12s", String.format("%,d", factory.getTotal())));
		
	}
	

	private static void testLockFree() throws Exception {
		
		final KeyedSampleCounterControllerFactory controller = new KeyedSampleCounterControllerFactory();
		final KeyedSimpleLockFreeObjectPool<Integer, SampleCounter> pool = new KeyedSimpleLockFreeObjectPool<Integer, SampleCounter>(poolSize, controller, st);		
		
		System.out.println("START");

		
		final CountDownLatch startLatch = new CountDownLatch(1);
		final CountDownLatch endLatch = new CountDownLatch(th_num);
		final AtomicLong count = new AtomicLong(0);
		for(int i=0; i<th_num ; i++) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {

					long localCount = 0;
					try {
						startLatch.await();
						long start = System.currentTimeMillis();
						long end = start + time;
						while(System.currentTimeMillis() < end) {

							int key = (int)localCount % 10;
							SampleCounter counter = null;
							try {
								counter = pool.borrow(key);
								
								counter.increment();
								
								if(wait > 0) {
									Thread.sleep(wait);
								}
								
							} finally {
								if(counter != null) {
									pool.release(key, counter);
								}
							}
							
							localCount++;
						}
						
						end = System.currentTimeMillis();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						count.addAndGet(localCount);
						endLatch.countDown();
					}
				}
				
			});
			t.start();
		}

		long start = System.currentTimeMillis();
		startLatch.countDown();
		endLatch.await();
		long end = System.currentTimeMillis();
		
		long ela = end - start;
		
		
		pool.destroy();
		
		System.out.println(String.format("Exec time = %s ms" , String.format("%,d", ela )));
		System.out.println(String.format("%4s Threads : throghput = %12s", th_num, String.format("%,d",  (long)(((double)count.get())*1000d/ela) )));
		System.out.println(String.format("Exec sum    = %12s", String.format("%,d",  count.get() )));
		

	}
}
