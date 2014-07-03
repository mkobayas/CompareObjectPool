package org.mk300.pool.sample.simple.test;

import org.mk300.pool.BorrowSt;
import org.mk300.pool.SimpleLockFreeObjectPool;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.mk300.pool.compare.SampleCounter;
import org.mk300.pool.compare.commospool.SimpleFactory;
import org.mk300.pool.compare.commospool2.SimpleFactory2;
import org.mk300.pool.compare.lockfree.SampleCounterController;


public class ComparePoolPefromanceTest {

	@Option(name="-h", usage="help")
	public static boolean help;

	@Option(name="-t", metaVar="th_num", usage="threads count")
	public static int th_num = 200;

	
	@Option(name="-p", metaVar="poolSize", usage="pool Size")
	public static int poolSize = 1000;
	

	@Option(name="-d", metaVar="duration", usage="duration(ms)")
	public static long time = 3000;
	
	@Option(name="-s", metaVar="BorrowSt", usage="FIRST | RANDOM | RANDOM_FIRST | THREAD")
	public static BorrowSt st = BorrowSt.RANDOM_FIRST;
	

	@Option(name="-w", metaVar="wait", usage="wait")
	public static long wait = 0;
	
	public static void main(String[] args) throws Exception {
		
		// parse argument
		ComparePoolPefromanceTest app = new ComparePoolPefromanceTest();
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
		
		final SimpleFactory factory = new SimpleFactory();
		final ObjectPool<SampleCounter> pool = new GenericObjectPool<SampleCounter>(factory, poolSize, 
				GenericObjectPool.WHEN_EXHAUSTED_BLOCK,
				GenericObjectPool.DEFAULT_MAX_WAIT,
				poolSize,
				GenericObjectPool.DEFAULT_MIN_IDLE,
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

							SampleCounter counter = null;
							try {
								counter = pool.borrowObject();
								
								counter.increment();
								
								if(wait > 0) {
									Thread.sleep(wait);
								}
								
							} finally {
								if(counter != null) {
									pool.returnObject(counter);
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
		
		GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		config.setMaxTotal(poolSize);
		config.setMaxIdle(-1);
		config.setBlockWhenExhausted(true);
		
		final SimpleFactory2 factory = new SimpleFactory2();
		final org.apache.commons.pool2.ObjectPool<SampleCounter> pool = new org.apache.commons.pool2.impl.GenericObjectPool<SampleCounter>(factory, config);
		
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

							SampleCounter counter = null;
							try {
								counter = pool.borrowObject();
								
								counter.increment();
								
								if(wait > 0) {
									Thread.sleep(wait);
								}
								
							} finally {
								if(counter != null) {
									pool.returnObject(counter);
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
		
		final SampleCounterController controller = new SampleCounterController();
		final SimpleLockFreeObjectPool<SampleCounter> pool = new SimpleLockFreeObjectPool<SampleCounter>(poolSize, controller, st);		
		
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

							SampleCounter counter = null;
							try {
								counter = pool.borrow();
								
								counter.increment();
								
								if(wait > 0) {
									Thread.sleep(wait);
								}
								
							} finally {
								if(counter != null) {
									pool.release(counter);
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
		
		System.out.println("pool.getCreateCount          = " + pool.getCreateCount());
		System.out.println("pool.getObjectCount()        = " + pool.getObjectCount());
		System.out.println("pool.getDestroyCount()       = " + pool.getDestroyCount());
		System.out.println("pool.getCreationErrorCount() = " + pool.getCreationErrorCount());
		
		pool.destroy();
		
		System.out.println(String.format("Exec time = %s ms" , String.format("%,d", ela )));
		System.out.println(String.format("%4s Threads : throghput = %12s", th_num, String.format("%,d",  (long)(((double)count.get())*1000d/ela) )));
		System.out.println(String.format("Exec sum    = %12s", String.format("%,d",  count.get() )));
		System.out.println(String.format("Counter sum = %12s", String.format("%,d", controller.getTotal())));
		

	}
}
