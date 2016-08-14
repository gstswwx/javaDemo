public class Singleton {
	private static Singleton s = null;
	private Singleton() {}
	public static synchronized Singleton getInstance() {
		if(s == null)
			s = new Singleton();
		return s;
	}
}
