import java.util.*;
import java.io.*;


public class ListFile {

	public static void main(String[] args) throws IOException {
		ListFile m = new ListFile();
		m.listFile();
	}
	
	public void listFile() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		ArrayList<String> aList = new ArrayList<String>();
		while(true) {
			String keyword = getKeyword(br);
			search(aList, "." + File.separator, keyword);
			System.out.println("ok");
			for(int i = 0; i < aList.size(); i += 2) {
				System.out.println("  " + aList.get(i) + "\t" + aList.get(i + 1));
			}
			if(getContinueInfo(br) == true) {
				aList.clear();
				continue;
			}
			else {
				break;
			}
		}
		System.out.println("exit");
		br.close();
	}
	
	private String getKeyword(BufferedReader br) throws IOException {
		String keyword = null;
		while(true) {
			System.out.print("search content:");
			keyword = br.readLine();
			if(keyword.length() == 0) {
				System.out.println("Please enter a keyword!");
				continue;
			}
			else {
				break;
			}
		}
		return keyword;
	}
	
	private void search(ArrayList<String> res, String currentDir, String keyword) {
		File file = new File(currentDir);
		if(file.exists()) {
			if(!file.isDirectory()) {
				if(file.getName().toLowerCase().contains(keyword.toLowerCase())) {
					res.add(file.getName());
					res.add(currentDir.substring(0, currentDir.length() - 1));
					return;
				}
			}
			else {
				String[] fileList = file.list();
				if(fileList != null) {
					for(String s : fileList) {
						s = currentDir + s + File.separator;
						search(res, s, keyword);
					}
				}
				
			}
		}
		
	}
	
	private boolean getContinueInfo(BufferedReader br) throws IOException {
		while (true) {
			System.out.println("continue?(yes or no)");
			String reply = br.readLine();
			if (reply.equals("yes")) {
				return true;
			} else if (reply.equals("no")) {
				return false;
			}
		}
	}
	
	
}
