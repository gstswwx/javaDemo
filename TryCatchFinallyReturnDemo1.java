public class TryCatchFinallyReturnDemo1 {
	public static void main(String[] args) {
		int i = func();
		System.out.println("i = " + i);
	}
	
	public static int func() { //ע�⣬�˴�����ֵΪjava������������
		int b = 10;
		try {
			int[] a = {1,2,3};
			for(int i = 0; i < a.length; i++) {
				System.out.println(a[i]);	
			}
			System.out.println("Last in try");
			
			b += 20;
			return b; //����java��ֵ���ݣ���ʱ�Ѿ�����һ��b��ֵ��ջ��ֻ�ǻ���ִ��finally���� 
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
			b += 50; //�˴��޸ĵĲ���ջ�еļ������ص�ֵ
			//return 1000;  //finally���ֵ�return�Ḳ��try��catch��return
		}
	}
}