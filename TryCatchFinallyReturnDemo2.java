public class TryCatchFinallyReturnDemo2 {
	public static void main(String[] args) {
		Test tt = func();
		System.out.println("tt.val = " + tt.val);
	}
	
	public static Test func() { //ע�⣬���ص�����������
		Test t = new Test(10);
		try {
			int[] a = {1,2,3};
			for(int i = 0; i < a.length; i++) {
				System.out.println(a[i]);	
			}
			System.out.println("Last in try");
			t.incl(20);
			return t; //�˴�������t��������ջ�Ա�����
		}
		catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Array out of Bounds");
			System.out.println("Last in catch");
			t.incl(30);
			return t;
		}
		finally {
			System.out.println("Last in finally");
			t.incl(50); //����t���������ͣ��˴�������t����ջ������tָ��ͬһ��������Ҳ�޸�����ջ����ָ��Ķ���
			//return t; //finally���ֵ�return�Ḳ��try��catch��return
		}
	}
}

class Test {
	int val;
	Test(int val) {
		this.val = val;
	}
	void incl(int i) {
		val += i;
	}
}