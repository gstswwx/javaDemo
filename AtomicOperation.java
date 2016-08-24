public class AtomicOperation implements Runnable{
	
	static volatile int i1 = 0; //��֤����i++���ϻ���������i++��Ϊԭ�Ӳ���
	static volatile int i2 = 0; //��֤i++����ԭ�Ӳ���
	static volatile int i3 = 0; //��֤++i����ԭ�Ӳ���
	
	@Override
	public void run() {
		for(int j = 0; j < 100; j++) {
			synchronized (this) { //�ӻ�����
				i1++;
			}
			i2++;
			++i3;
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		Runnable m = new AtomicOperation();
		Thread[] threads = new Thread[1000]; //��1000���߳�
		for(int k = 0; k < threads.length; k++) {
			threads[k] = new Thread(m);
			threads[k].start();
			
		}
		
		for(int k = 0;  k < threads.length; k++) { 
			//����Ϊ���������̣߳�ʹ�������߳����н�������������
			threads[k].join();
		}
		
		//��i++��++i��ԭ�Ӳ�������ô���յĽ��Ӧ����100000
		System.out.println("i1 = " + i1);
		System.out.println("i2 = " + i2);
		System.out.println("i3 = " + i3);
		
	}
}



