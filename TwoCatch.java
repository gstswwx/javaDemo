public class TwoCatch {
	public static void main(String[] args) {
		func();
	}
	
	public static void func() {
		try {
			System.out.println("Start in try");
			
			
			
			int[] a = {1,2,3};
			for(int i = 0; i <= a.length; i++) {
				System.out.println(a[i]);	
			}
			
			String s = null;
			System.out.println(s.length());
			
			System.out.println("End in try");
		}
		catch(ArrayIndexOutOfBoundsException e) {
			System.out.println("Array out of Bounds");
		}
		catch(NullPointerException e) {
			System.out.println("Null pointer!");
		}
		finally {
			System.out.println("In finally");
		}
	}
}