public class AtomicOperation implements Runnable{
	
	static volatile int i1 = 0; //验证当给i++加上互斥锁后，则i++变为原子操作
	static volatile int i2 = 0; //验证i++不是原子操作
	static volatile int i3 = 0; //验证++i不是原子操作
	
	@Override
	public void run() {
		for(int j = 0; j < 100; j++) {
			synchronized (this) { //加互斥锁
				i1++;
			}
			i2++;
			++i3;
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		Runnable m = new AtomicOperation();
		Thread[] threads = new Thread[1000]; //开1000个线程
		for(int k = 0; k < threads.length; k++) {
			threads[k] = new Thread(m);
			threads[k].start();
			
		}
		
		for(int k = 0;  k < threads.length; k++) { 
			//这里为了阻塞主线程，使得所有线程运行结束后再输出结果
			threads[k].join();
		}
		
		//若i++或++i是原子操作，那么最终的结果应该是100000
		System.out.println("i1 = " + i1);
		System.out.println("i2 = " + i2);
		System.out.println("i3 = " + i3);
		
	}
}



