import java.io.*;
import java.util.Scanner;

public class FileCopyDemo {

	public static void main(String[] args) throws IOException {
		Scanner scn = new Scanner(System.in);
		System.out.println("Input the source file path:");
		String src = scn.nextLine();
		System.out.println("Input the destination directory path:");
		String des = scn.nextLine();
		scn.close();
		copy(src, des);
	}

	public static void copy(String pathA, String pathB) throws IOException {
		File srcFile = new File(pathA);
		File desFile = new File(pathB);
		boolean isSrcExist = srcFile.exists();

		if (isSrcExist) {
			if (srcFile.isFile()) {
				copyFile(srcFile, desFile);
			} else if (srcFile.isDirectory()) {
				copyDirectory(srcFile, desFile);
			}
		} else {
			System.out.println("Source File cannot be found!");
		}

	}

	private static void copyFile(File srcFile, File desFile) throws IOException {
		boolean isDesExist = desFile.exists();
		if (isDesExist) {
			if (!desFile.isDirectory()) {
				System.out.println("Destination path is not a directory!");
				System.out.println("Maybe there is a file with the same name as the directory.");
				return;
			}
		} else {
			desFile.mkdirs();
		}

		String desFileName = desFile.getAbsolutePath();
		if (!desFileName.endsWith(File.separator)) {
			desFileName = desFileName + File.separator;
		}

		File targetFile = new File(desFileName + srcFile.getName());
		// System.out.println(targetFile.getAbsolutePath());
		if (!targetFile.exists()) {
			targetFile.createNewFile();
		}
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(srcFile));
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(targetFile));
		}
		catch(Exception e) {
			System.out.println("An Error occur when copying " + targetFile.getAbsolutePath());
			System.out.println("Maybe there is a directory with the same name as the file");
			if(bis != null) {
				bis.close();
			}
			return;
		}
		
		byte[] b = new byte[1024];
		int byteNum = 0;
		while ((byteNum = bis.read(b)) != -1) {
			bos.write(b, 0, byteNum);
		}
		if (bis != null) {
			bis.close();
		}
		if (bos != null) {
			bos.close();
		}
	}

	private static void copyDirectory(File srcFile, File desFile) throws IOException {

		File[] fileList = srcFile.listFiles();
		if (fileList != null) {
			if (fileList.length == 0) {
				String targetDir = desFile.getAbsolutePath() + File.separator + srcFile.getName();
				new File(targetDir).mkdirs();
			} else {
				for (File file : fileList) {
					String targetDir = desFile.getAbsolutePath() + File.separator + srcFile.getName();
					if (file.isFile()) {
						copyFile(file, new File(targetDir));
					} else if (file.isDirectory()) {
						copyDirectory(file, new File(targetDir));
					}
				}
			}
		}
	}
}
