import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class GenericsDemo {
	public static void main(String[] args) {
		Collection c1 = new ArrayList<String>();
		
		//The method add(Object) belongs to the raw type Collection
		//So any objects we add into ArrayList<String> will be converted to Object Type!
		c1.add(new String("Hello"));
		c1.add(new String("Hi"));
		//We may make a mistake as below, and the compiler cannot find it!
		//c1.add(new Integer(1));  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		
		Iterator iter1 = c1.iterator();
		while(iter1.hasNext()) {
			//The cast below is not safe! We think that all the elements
			//should be "String Type", but we are wrong!
			String s = (String) iter1.next(); 
			System.out.println(s.length());
		}
		
		//It is safer to use the wildcard '?'
		
		//We can only add the Type '?' or its subtype, 
		//but Type '?' is not sure, so we cannot add anything.
		ArrayList<String> a = new ArrayList<String>();
		a.add(new String("Wow"));
		Collection<?> c2 = a;
		//c2.add(new Integer(0)); //compile error!
		//c2.add(new String("Wow")); //compile error!
		
		for(Object e : c2) {
			System.out.println(((String)e).length());
		}	
	}
}
