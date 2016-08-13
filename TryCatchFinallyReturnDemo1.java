public class TryCatchFinallyReturnDemo1 {
	public static void main(String[] args) {
		int i = func();
		System.out.println("i = " + i);
	}
	
	public static int func() { //注意，此处返回值为java基本数据类型
		int b = 10;
		try {
			int[] a = {1,2,3};
			for(int i = 0; i < a.length; i++) {
				System.out.println(a[i]);	
			}
			System.out.println("Last in try");
			
			b += 20;
			return b; //由于java是值传递，此时已经拷贝一份b的值入栈，只是还得执行finally部分 
			//return b += 20;
		}
		catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Array out of Bounds");
			System.out.println("Last in catch");
			b += 30;
			return b;
		}
		finally {
			System.out.println("Last in finally");
			b += 50; //此处修改的并非栈中的即将返回的值
			//return 1000;  //finally部分的return会覆盖try和catch的return
		}
	}
}