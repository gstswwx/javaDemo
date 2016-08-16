import java.io.*;

public class Main {
	public static void main(String[] args) {
		Main m = new Main();
		m.readFile("./data1.txt", "gb2312");
		System.out.println("---------------------");
		m.readFile("./data2.txt", "utf8");
	}

	public void readFile(String fileName, String charSet) {
		BufferedReader bReader = null;
		StringBuffer bstr = new StringBuffer();
		try {
			bReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), charSet));
			String s = null;
			while ((s = bReader.readLine()) != null) {
				bstr.append(s + '\n');
			}
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found!");
		} catch (IOException e) {
			System.out.println("IO Exception when reading the file!");
		} finally {
			if (bReader != null) {
				try {
					bReader.close();
				} catch (IOException e) {
					System.out.println("IO Exception when closing the file!");
				}
			}
		}

		System.out.println(bstr);
	}
}
