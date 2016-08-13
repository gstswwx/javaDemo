public class TryCatchFinallyReturnDemo2 {
	public static void main(String[] args) {
		Test tt = func();
		System.out.println("tt.val = " + tt.val);
	}
	
	public static Test func() { //注意，返回的是引用类型
		Test t = new Test(10);
		try {
			int[] a = {1,2,3};
			for(int i = 0; i < a.length; i++) {
				System.out.println(a[i]);	
			}
			System.out.println("Last in try");
			t.incl(20);
			return t; //此处将对象t的引用入栈以备返回
		}
		catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Array out of Bounds");
			System.out.println("Last in catch");
			t.incl(30);
			return t;
		}
		finally {
			System.out.println("Last in finally");
			t.incl(50); //由于t是引用类型，此处的引用t与入栈的引用t指向同一对象，所以也修改了入栈引用指向的对象
			//return t; //finally部分的return会覆盖try和catch的return
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