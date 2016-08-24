import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;

public class Main {

	public static void main(String[] args) {
		Main m = new Main();
		m.createGUI();

	}

	public void createGUI() {

		JFrame mainDialog = new JFrame("File Copy");
		mainDialog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainDialog.setLocation(700, 250);
		mainDialog.setSize(500, 190);
		mainDialog.setResizable(false);

		mainDialog.setLayout(new BorderLayout());

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

		JTextField srcField = new JTextField(30);
		srcField.setFont(new Font(null, 0, 15));
		JTextField desField = new JTextField(30);
		desField.setFont(new Font(null, 0, 15));

		JButton startBtn = new JButton("开始复制");
		startBtn.setEnabled(false);

		Document srcDoc = srcField.getDocument();
		Document desDoc = desField.getDocument();
		srcDoc.addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				if (srcDoc.getLength() == 0) {
					startBtn.setEnabled(false);
				}
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				if (desDoc.getLength() > 0) {
					startBtn.setEnabled(true);
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
			}
		});

		desDoc.addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				if (desDoc.getLength() == 0) {
					startBtn.setEnabled(false);
				}
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				if (srcDoc.getLength() > 0) {
					startBtn.setEnabled(true);
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
			}
		});

		JButton chooseSrcBtn = new JButton("...");
		chooseSrcBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				int selected = fc.showOpenDialog(null);
				if (selected == JFileChooser.APPROVE_OPTION) {
					File srcFile = fc.getSelectedFile();
					srcField.setText(srcFile.getAbsolutePath());
				}

			}
		});
		JButton chooseDesBtn = new JButton("...");
		chooseDesBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int selected = fc.showOpenDialog(null);
				if (selected == JFileChooser.APPROVE_OPTION) {
					File desFile = fc.getSelectedFile();
					desField.setText(desFile.getAbsolutePath());
				}

			}
		});

		centerPanel.add(srcField);
		centerPanel.add(chooseSrcBtn);
		centerPanel.add(desField);
		centerPanel.add(chooseDesBtn);

		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 20));

		startBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				JDialog askDialog = new JDialog();
				askDialog.setTitle("Warning");
				askDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				askDialog.setLocation(760, 230);
				askDialog.setSize(420, 200);
				askDialog.setResizable(false);
				FlowLayout warningLayout = new FlowLayout();
				warningLayout.setHgap(35);
				warningLayout.setVgap(30);
				askDialog.setLayout(warningLayout);
				JLabel warningLabel = new JLabel("     确定要进行复制吗？       ");
				warningLabel.setFont(new Font(null, Font.BOLD, 25));

				JButton denyButton = new JButton("否");
				denyButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						askDialog.dispose();
					}
				});

				JButton okButton = new JButton("是");
				okButton.addActionListener(new ActionListener() {

					class Task extends SwingWorker<Void, Void> {

						@Override
						protected Void doInBackground() throws InterruptedException, IOException {

							class copyThread extends Thread {
								private FileCopy fileCopy;
								private String src;
								private String des;

								public copyThread(String src, String des) {
									super();
									this.src = src;
									this.des = des;
									fileCopy = new FileCopy();
								}

								public double getRatio() {
									return fileCopy.getRatio();
								}

								public void run() {
									try {
										fileCopy.copy(src, des);
									} catch (IOException | InterruptedException e) {
										System.out.println("Exception when copyThread run");
									}
								}
							}

							int progress = 0;
							setProgress(0);
							copyThread thread = new copyThread(srcField.getText(), desField.getText());
							thread.start();

							while (progress < 100) {
								Thread.sleep(10);
								progress = (int) (thread.getRatio() * 100);
								setProgress(progress);
							}

							return null;
						}
					}

					@Override
					public void actionPerformed(ActionEvent e) {

						File srcFile = new File(srcField.getText());
						File desFile = new File(desField.getText());
						boolean isSrcLegal = srcFile.exists();
						boolean isDesLegal = desFile.exists();
						boolean isDesDir = false;
						if (isDesLegal) {
							if (desFile.isDirectory()) {
								isDesDir = true;
							}
						}
						boolean isNested = false;
						if (desFile.getAbsolutePath().equals(srcFile.getAbsolutePath())) {
							isNested = true;
						} else if (desFile.getAbsolutePath().indexOf(srcFile.getAbsolutePath()) == 0
								&& desFile.getAbsolutePath().charAt(srcFile.getAbsolutePath().length()) == File.separatorChar) {
							isNested = true;
						}
						
						class continueFlag {
							boolean isContinue = true;
						}
						continueFlag cfg = new continueFlag();
						
						if (!(isSrcLegal && isDesLegal && isDesDir && !isNested)) {
							askDialog.dispose();
							JDialog errorDialog = new JDialog();
							errorDialog.setTitle("Error");
							errorDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
							errorDialog.setLocation(810, 220);
							errorDialog.setSize(500, 200);
							errorDialog.setResizable(false);
							errorDialog.setModal(true);

							BorderLayout errorDialogLayout = new BorderLayout();
							errorDialog.setLayout(errorDialogLayout);
							String labelStr = "";
							if (!isSrcLegal && isDesLegal) {
								labelStr = "找不到您要移动的文件或文件夹:(";
							} else if (!isDesLegal && isSrcLegal) {
								labelStr = "找不到您所指定的目标移动位置:(";
							} else if (!isDesLegal && !isSrcLegal) {
								labelStr = "在您提供的两个路径下都找不到文件:(";
							} else {
								if (!isDesDir) {
									labelStr = "您所制定的目标移动位置不是一个目录:(";
								} else if (isNested) {
									labelStr = "目标文件夹是源文件夹的子文件夹:(";
								}
							}
							JLabel errorInfo = new JLabel(labelStr, SwingConstants.CENTER);
							errorInfo.setFont(new Font(null, Font.BOLD, 20));
							errorDialog.add("Center", errorInfo);

							JButton backButton = new JButton("返回");
							backButton.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									errorDialog.dispose();
								}
							});
							JPanel backButtonPanel = new JPanel();
							backButtonPanel.add(backButton);
							errorDialog.add("South", backButtonPanel);

							errorDialog.setVisible(true);
						} else {
							askDialog.dispose();

							int exceptionFlag = -1;

							

							File[] fileList1 = desFile.listFiles();

							for (int i = 0; i < fileList1.length; i++) {
								if (fileList1[i].getName().equals(srcFile.getName())) {
									if (fileList1[i].isFile() && srcFile.isFile()) {
										exceptionFlag = 0;
									} else if (fileList1[i].isDirectory() && srcFile.isDirectory()) {
										exceptionFlag = 1;
									} else if (fileList1[i].isFile() && srcFile.isDirectory()) {
										exceptionFlag = 2;
									} else if (fileList1[i].isDirectory() && srcFile.isFile()) {
										exceptionFlag = 3;
									}
								}
							}

							if (exceptionFlag != -1) {
								JDialog sameNameWarningDialog = new JDialog();
								sameNameWarningDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
								sameNameWarningDialog.setLocation(810, 220);
								sameNameWarningDialog.setSize(500, 200);
								sameNameWarningDialog.setResizable(false);
								sameNameWarningDialog.setModal(true);
								sameNameWarningDialog.setLayout(new BorderLayout());

								JLabel sameNameInfoLabel = new JLabel();
								sameNameInfoLabel.setFont(new Font(null, Font.BOLD, 20));

								sameNameInfoLabel.setHorizontalAlignment(JLabel.CENTER);
								String sameNameInfo = "";
								if (exceptionFlag == 0 || exceptionFlag == 1) {
									sameNameWarningDialog.setTitle("Warning");
									if (exceptionFlag == 0) {
										sameNameInfo = "目标目录下有重名文件，是否覆盖该文件？";
									} else if (exceptionFlag == 1) {
										sameNameInfo = "目标目录下有重名文件夹，是否继续？";
									}
									sameNameInfoLabel.setText(sameNameInfo);
									JButton continueButton = new JButton("继续");
									JButton giveUpButton = new JButton("返回");
									sameNameWarningDialog.add("Center", sameNameInfoLabel);
									JPanel btnPanel = new JPanel();
									btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
									btnPanel.add(continueButton);
									btnPanel.add(giveUpButton);
									sameNameWarningDialog.add("South", btnPanel);
									continueButton.addActionListener(new ActionListener() {
										@Override
										public void actionPerformed(ActionEvent e) {
											sameNameWarningDialog.dispose();
										}
									});
									giveUpButton.addActionListener(new ActionListener() {
										@Override
										public void actionPerformed(ActionEvent e) {
											sameNameWarningDialog.dispose();
											cfg.isContinue = false;
										}
									});
								} else if (exceptionFlag == 2 || exceptionFlag == 3) {
									sameNameWarningDialog.setTitle("Error");
									if (exceptionFlag == 2) {
										sameNameInfo = "目标目录下有重名文件，禁止复制！";
										cfg.isContinue = false;
									} else if (exceptionFlag == 3) {
										sameNameInfo = "目标目录下有重名文件夹，禁止复制！";
										cfg.isContinue = false;
									}
									sameNameInfoLabel.setText(sameNameInfo);
								}
								sameNameWarningDialog.add(sameNameInfoLabel);

								sameNameWarningDialog.setVisible(true);
							}

							if (cfg.isContinue == true) {
								startBtn.setEnabled(false);
								srcField.setEnabled(false);
								desField.setEnabled(false);
								chooseSrcBtn.setEnabled(false);
								chooseDesBtn.setEnabled(false);
								JDialog progressDialog = new JDialog();
								progressDialog.setTitle("Progress Bar");
								progressDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
								progressDialog.setLocation(810, 220);
								progressDialog.setSize(350, 100);
								progressDialog.setResizable(false);
								FlowLayout progressDialogLayout = new FlowLayout();
								progressDialogLayout.setHgap(30);
								progressDialogLayout.setVgap(20);
								progressDialog.setLayout(progressDialogLayout);
								JProgressBar progressBar = new JProgressBar(0, 100);
								progressBar.setValue(0);
								progressBar.setStringPainted(true);
								JLabel label = new JLabel("进度: ");
								label.setAlignmentX(30);
								label.setFont(new Font(null, Font.BOLD, 18));
								progressDialog.getContentPane().add(label);
								progressDialog.getContentPane().add(progressBar);
								progressDialog.addWindowListener(new WindowAdapter() {
									@Override
									public void windowClosed(WindowEvent e) {
										startBtn.setEnabled(true);
										srcField.setEnabled(true);
										desField.setEnabled(true);
										chooseSrcBtn.setEnabled(true);
										chooseDesBtn.setEnabled(true);
									}
								});

								progressDialog.setVisible(true);

								Task task = new Task();
								task.addPropertyChangeListener(new PropertyChangeListener() {
									@Override
									public void propertyChange(PropertyChangeEvent evt) {
										if ("progress" == evt.getPropertyName()) {
											int progress = (Integer) evt.getNewValue();
											progressBar.setValue(progress);
										}
									}
								});
								task.execute();

							}

						}
					}
				});

				askDialog.getContentPane().add(warningLabel);
				askDialog.getContentPane().add(okButton);
				askDialog.getContentPane().add(denyButton);
				askDialog.setModal(true);
				askDialog.setVisible(true);

			}
		});

		startBtn.setFont(new Font(null, Font.BOLD, 15));
		bottomPanel.add(startBtn);

		mainDialog.getContentPane().add(centerPanel, BorderLayout.CENTER);
		mainDialog.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		mainDialog.setVisible(true);

	}
}
