import java.io.*;

public class FileCopy {

	private Integer bytes = new Integer(0);
	private Integer sumBytes = new Integer(0);

	public double getRatio() {
		if (sumBytes == 0) {
			return 1.0;
		}
		return (double) bytes / sumBytes;
	}

	public int getSumBytes(String path) {
		int sum = 0;
		File root = new File(path);
		if (root.exists()) {
			if (root.isDirectory()) {
				File[] fileList = root.listFiles();
				for (int i = 0; i < fileList.length; i++) {
					if (fileList[i].isDirectory())
						sum += getSumBytes(fileList[i].getAbsolutePath());
					else
						sum += (int) fileList[i].length();
				}
			} else if (root.isFile()) {
				sum += root.length();
			}

		}
		return sum;
	}

	public void copy(String pathA, String pathB) throws IOException, InterruptedException {

		File srcFile = new File(pathA);
		File desFile = new File(pathB);
		boolean isSrcExist = srcFile.exists();

		if (isSrcExist) {
			sumBytes = getSumBytes(pathA);
			if (srcFile.isFile()) {
				copyFile(srcFile, desFile);
			} else if (srcFile.isDirectory()) {
				copyDirectory(srcFile, desFile);
			}
		} else {
			System.out.println("Source File cannot be found!");
		}

	}

	private void copyFile(File srcFile, File desFile) throws IOException, InterruptedException {
		boolean isDesExist = desFile.exists();
		if (isDesExist) {
			if (!desFile.isDirectory()) {
				System.out.println("Destination path is not a directory!");
				System.out.println("Maybe there is a file with the same name as the directory.");
				return;
			}
		} else {
			desFile.mkdirs();
			Thread.sleep(100);
		}

		String desFileName = desFile.getAbsolutePath();
		if (!desFileName.endsWith(File.separator)) {
			desFileName = desFileName + File.separator;
		}

		File targetFile = new File(desFileName + srcFile.getName());
		if (!targetFile.exists()) {
			targetFile.createNewFile();
		}
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(srcFile));
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(targetFile));
		} catch (Exception e) {
			System.out.println("An Error occur when copying " + targetFile.getAbsolutePath());
			System.out.println("Maybe there is a directory with the same name as the file");
			if (bis != null) {
				bis.close();
			}
			return;
		}

		byte[] b = new byte[4096];
		int byteNum = 0;
		while ((byteNum = bis.read(b)) != -1) {
			bytes += byteNum;
			bos.write(b, 0, byteNum);
		}
		// System.out.println("bytes = " + bytes);
		if (bis != null) {
			bis.close();
		}
		if (bos != null) {
			bos.close();
		}
		Thread.sleep(50);
	}

	private void copyDirectory(File srcFile, File desFile) throws IOException, InterruptedException {

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
