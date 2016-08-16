import java.io.*;
import java.util.*;
import java.util.regex.*;

public class IODemo {
	public static void main(String[] args) {
		StringBuffer bstr = new StringBuffer();
		String fileName = "./info.txt";
		String charSet = "utf8";
		IODemo m = new IODemo();
		m.getInfo(bstr);
		m.saveInfo(bstr, fileName, charSet);
	}

	public void getInfo(StringBuffer bstr) {
		Scanner scn = new Scanner(System.in);
		String nameStr = null;
		String numStr = null;

		while (true) {
			nameStr = getName(scn, bstr);
			numStr = getNumber(scn, bstr);
			printInfo(nameStr, numStr);
			if (getContinueInfo(scn) == true)
				continue;
			else
				break;
		}
	}

	protected String getName(Scanner scn, StringBuffer bstr) {
		while (true) {
			System.out.println("enter StudentName:");
			String tempName = scn.nextLine();
			if (tempName.length() < 2 || tempName.length() > 20) {
				System.out.println("The length of name should not be less than 2 or more than 20!");
				continue;
			}
			bstr.append(tempName + " ");
			return tempName;
		}
	}

	protected String getNumber(Scanner scn, StringBuffer bstr) {
		while (true) {
			System.out.println("enter StudentNo:");
			String tempNumber = scn.nextLine();
			String pattern = "[^0-9]";
			Pattern p = Pattern.compile(pattern);
			Matcher m = p.matcher(tempNumber);
			if (m.find()) {
				System.out.println("Student Number should consist of numbers only!");
				continue;
			}
			if (tempNumber.length() != 8) {
				System.out.println("The length of student number must be 8!");
				continue;
			}
			bstr.append(tempNumber + System.getProperty("line.separator"));
			return tempNumber;
		}

	}

	protected void printInfo(String nameStr, String numStr) {
		System.out.println("ok,info:" + nameStr + " " + numStr + " written successfully!");
	}

	protected boolean getContinueInfo(Scanner scn) {
		while (true) {
			System.out.println("continue?(y or n)");
			String reply = scn.nextLine();
			if (reply.equals("y")) {
				return true;
			} else if (reply.equals("n")) {
				return false;
			}
		}
	}

	public void saveInfo(StringBuffer bstr, String fileName, String charSet) {
		File file = new File("./info.txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println("Failed to creat" + fileName);
			}
		}
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), charSet));
			bw.write(bstr.toString());
		} catch (UnsupportedEncodingException e) {
			System.out.println("The character encoding is not supported.");
		} catch (FileNotFoundException e) {
			System.out.println("The file is not found!");
		} catch (IOException e) {
			System.out.println("IO Exception when writing " + fileName);
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					System.out.println("IO Exception when closing the file!");
				}
			}
		}
	}

}
