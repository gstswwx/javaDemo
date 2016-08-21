import java.util.*;

class SynThread implements Runnable{
	
	private Stack<Integer> stack;
	
	public SynThread(Stack<Integer> stack) {
		this.stack = stack;
	}
	
	public void run() {
		for(int i = 0; i < 5; i++) {
			try{
				synchronized (stack) { //此处绑定的是stack，而不是this！！！
					stack.push(new Integer(i));
					System.out.println(Thread.currentThread().getName() + " push " + i);
					Thread.sleep((int)(Math.random() * 1000));
					Integer temp = stack.pop();
					System.out.println(Thread.currentThread().getName() + " pop " + temp);
					Thread.sleep((int)(Math.random() * 1000));
				}
				
			}
			catch(InterruptedException e) {
				System.out.println("Exception");
			}
		}
		
	}
	
}

public class SynchronizedTest {
	public static void main(String[] args) {
		Stack<Integer> stk = new Stack<Integer>();
		
		//为了说明互斥锁应该绑定stack，而不是this，一下构造了两个不同的SynThread对象，但是这两个对象的成员变量stack是同一个
		SynThread synthread = new SynThread(stk);
		SynThread synthread2 = new SynThread(stk);
		Thread t1 = new Thread(synthread, "t1");
		Thread t2 = new Thread(synthread2, "t2");
		t1.start();
		t2.start();
		
		//main函数也是一个线程
		/*for(int i = 0; i < 5; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("hello!");
		}*/
	}
}
