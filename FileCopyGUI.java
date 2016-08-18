import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;

import java.util.Random;

public class FileCopyGUI {

	public static void main(String[] args) {
		FileCopyGUI m = new FileCopyGUI();
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
				// askDialog.setUndecorated(true);
				// askDialog.getRootPane().setWindowDecorationStyle(JRootPane.ERROR_DIALOG);
				// askDialog.setLayout(new FlowLayout());
				JLabel warningLabel = new JLabel("操作有可能导致覆盖目标文件夹内已有的文件,是否继续？");
				warningLabel.setFont(new Font(null, Font.BOLD, 14));

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
						protected Void doInBackground() throws InterruptedException {
							Random random = new Random();
							int progress = 0;
							setProgress(0);
							while (progress < 100) {
								Thread.sleep(random.nextInt(500));
								progress += random.nextInt(10);
								setProgress(Math.min(progress, 100));
							}
							return null;
						}
					}

					@Override
					public void actionPerformed(ActionEvent e) {

						File srcFile = new File(srcField.getText());
						File desFile = new File(desField.getText());
						Boolean isSrcLegal = srcFile.exists();
						Boolean isDesLegal = desFile.exists();
						if (!(isSrcLegal && isDesLegal)) {
							askDialog.dispose();
							JDialog errorDialog = new JDialog();
							errorDialog.setTitle("Error");
							errorDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
							errorDialog.setLocation(810, 220);
							errorDialog.setSize(500, 200);
							errorDialog.setResizable(false);
							errorDialog.setModal(true);

							// GridLayout errorDialogLayout = new GridLayout(2,
							// 1);
							BorderLayout errorDialogLayout = new BorderLayout();
							// errorDialogLayout.setHgap(20);
							// errorDialogLayout.setVgap(50);
							errorDialog.setLayout(errorDialogLayout);
							String labelStr = "";
							if (!isSrcLegal && isDesLegal) {
								labelStr = "找不到您要移动的文件或文件夹:(";
							} else if (!isDesLegal && isSrcLegal) {
								labelStr = "找不到您所指定的目标移动位置:(";
							} else if (!isDesLegal && !isSrcLegal) {
								labelStr = "在您提供的两个路径下都找不到文件:(";
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
							// JPanel progressPanel = new JPanel();
							// progressPanel.setSize(150, 100);
							JProgressBar progressBar = new JProgressBar(0, 100);
							// progressBar.setSize(350, 30);
							// progressBar.setLocation(50, 50);
							progressBar.setValue(0);
							progressBar.setStringPainted(true);
							// progressPanel.add(progressBar);

							JLabel label = new JLabel("进度: ");
							label.setAlignmentX(30);
							// label.setLayout(null);
							label.setFont(new Font(null, Font.BOLD, 18));
							// label.setLocation(60, 50);
							progressDialog.getContentPane().add(label);
							progressDialog.getContentPane().add(progressBar);

							// progressDialog.pack();

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
									// System.out.println(evt.getPropertyName());
									if ("progress" == evt.getPropertyName()) {
										int progress = (Integer) evt.getNewValue();
										progressBar.setValue(progress);
									}
								}
							});
							task.execute();
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
