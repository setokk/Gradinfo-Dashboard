package edu.gradinfo.dashboard.gui;

import edu.gradinfo.dashboard.enums.ThemeEnum;
import edu.gradinfo.dashboard.git.GitController;
import edu.gradinfo.dashboard.util.ConfigUtils;
import edu.gradinfo.dashboard.util.FileUtils;
import edu.gradinfo.dashboard.util.UIUtils;
import edu.gradinfo.dashboard.util.ImageUtils;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.io.IOException;

public class HomePage extends JFrame {
	private final JPanel aggregatePanel;

	/* HEADER */
	private	final JPanel headerPanel;
	private final JLabel headerLogoLabel;
	private final JLabel headerAppLabel;
	private final JComboBox<String> headerThemeDropdown;

	/* MAIN */
	private final JPanel mainPanel;
	private final JPanel editorPanel;
	private final JTextArea editorTextArea;
	private final JPanel fileTreePanel;
	private final JScrollPane fileTreeScrollPane;
	private final JScrollPane editorScrollPane;
	private final JPanel gitOperationsPanel;
	private final JButton newFileBtn;
	private final JButton updateBtn;
	private final JButton pushBtn;
	private final JLabel commitMsgLabel;
	private final JTextField commitMsgTextField;

	private static String openedFilename;

	public HomePage() {
		this.setMinimumSize(UIUtils.WINDOW_DIMENSION);
		this.setMaximumSize(UIUtils.WINDOW_DIMENSION);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Gradinfo Dashboard");
		this.setIconImage(UIUtils.APP_ICON);

		// Defaults for re-setting
		GridBagConstraints c = new GridBagConstraints();
		int DEFAULT_FILL = c.fill;
		Insets DEFAULT_INSETS = c.insets;

		/*
		 *
		 *  Header panel
		 *
		 */
		headerPanel = new JPanel(new GridBagLayout());

		/* Logo Label */
		headerLogoLabel = new JLabel("");
		headerLogoLabel.setIcon(new ImageIcon(ImageUtils.resize(UIUtils.APP_ICON, 80, 80)));
		setConstraints(c, 0, 0, 0.02, 0.0, GridBagConstraints.WEST, DEFAULT_INSETS);
		headerPanel.add(headerLogoLabel, c);

		/* App Label */
		headerAppLabel = new JLabel("Gradinfo");
		setConstraints(c, 1, 0, 0.98, 0.0, GridBagConstraints.WEST, DEFAULT_INSETS);
		headerPanel.add(headerAppLabel, c);

		/* Theme Dropdown (ignore INSTANCE enum value) */
		ThemeEnum[] themeEnums = ThemeEnum.INSTANCE.getValues();
		String[] items = new String[themeEnums.length - 1];
		for (var themeEnum : themeEnums) {
			if (themeEnum.equals(ThemeEnum.INSTANCE))
				continue;
			items[themeEnum.ordinal()] = themeEnum.name();
		}
		headerThemeDropdown = new JComboBox<>(items);
		headerThemeDropdown.setSelectedIndex(Integer.parseInt(ConfigUtils.getConfig().get(ConfigUtils.THEME_PROPERTY)));
		setConstraints(c, 2, 0, 0.02, 0.0, GridBagConstraints.EAST, new Insets(0, 0, 0, 20));
		headerPanel.add(headerThemeDropdown, c);

		/*
		 *
		 *  Main panel
		 *
		 */
		mainPanel = new JPanel(new GridBagLayout());
		mainPanel.setBackground(UIUtils.BACKGROUND_COLOR);

		/* Editor */
		editorPanel = new JPanel(new GridBagLayout());
		editorTextArea = new JTextArea();
		editorTextArea.setTabSize(1);
		editorScrollPane = new JScrollPane(editorTextArea);
		setConstraints(c, 0, 0, 1, 1, GridBagConstraints.CENTER, DEFAULT_INSETS, GridBagConstraints.BOTH);
		editorPanel.add(editorScrollPane, c); // Add the scroll pane instead of the text area
		setConstraints(c, 1, 0, 0.45, 1, GridBagConstraints.CENTER, new Insets(0, 0, 0, 5), GridBagConstraints.BOTH);
		mainPanel.add(editorPanel, c);

		/* File tree view */
		fileTreePanel = new JPanel();
		fileTreePanel.setLayout(new BoxLayout(fileTreePanel, BoxLayout.Y_AXIS));
		fileTreeScrollPane = new JScrollPane(fileTreePanel);
		fileTreeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		fileTreeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		fileTreeScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		updateFileTree();

		setConstraints(c, 0, 0, 0.45, 1, GridBagConstraints.WEST, new Insets(0, 0, 0, 0), GridBagConstraints.BOTH);
		mainPanel.add(fileTreeScrollPane, c);

		/* Git operations */
		gitOperationsPanel = new JPanel();
		gitOperationsPanel.setBackground(UIUtils.BACKGROUND_COLOR);
		gitOperationsPanel.setLayout(new GridBagLayout());
		GridBagConstraints gitC = new GridBagConstraints();
		gitC.gridx = 0;
		gitC.gridy = GridBagConstraints.RELATIVE;
		gitC.gridwidth = 1;
		gitC.weightx = 1.0;
		gitC.fill = GridBagConstraints.HORIZONTAL;
		gitC.insets = new Insets(5, 0, 5, 0);

		newFileBtn = new JButton("New file");
		updateBtn = new JButton("Update");
		pushBtn = new JButton("Push");
		commitMsgLabel = new JLabel("Commit message:");
		commitMsgLabel.setHorizontalAlignment(SwingConstants.CENTER);
		commitMsgLabel.setForeground(UIUtils.FOREROUND_COLOR);
		commitMsgTextField = new JTextField();
		newFileBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, newFileBtn.getPreferredSize().height));
		updateBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, updateBtn.getPreferredSize().height));
		pushBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, pushBtn.getPreferredSize().height));
		commitMsgLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, commitMsgLabel.getPreferredSize().height));
		commitMsgTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, commitMsgTextField.getPreferredSize().height));
		gitOperationsPanel.add(newFileBtn, gitC);
		gitOperationsPanel.add(updateBtn, gitC);
		gitOperationsPanel.add(pushBtn, gitC);
		gitOperationsPanel.add(commitMsgLabel, gitC);
		gitOperationsPanel.add(commitMsgTextField, gitC);
		setConstraints(c, 2, 0, 0.1, 1, GridBagConstraints.EAST, DEFAULT_INSETS, GridBagConstraints.BOTH);
		mainPanel.add(gitOperationsPanel, c);

		/*
		 *
		 *  Aggregate panel
		 *
		 */
		aggregatePanel = new JPanel(new GridBagLayout());
		aggregatePanel.setBackground(UIUtils.BACKGROUND_COLOR);

		// Populate aggregate panel
		setConstraints(c, 0, 0, 1, 0.1, GridBagConstraints.NORTHWEST, DEFAULT_INSETS, GridBagConstraints.HORIZONTAL);
		aggregatePanel.add(headerPanel, c);
		setConstraints(c, 0, 1, 1, 0.9, GridBagConstraints.CENTER, new Insets(10, 20, 20, 20), GridBagConstraints.BOTH);
		aggregatePanel.add(mainPanel, c);

		configureListeners();
		configureKeystrokes();

		this.setContentPane(aggregatePanel);

		this.pack();
		this.setVisible(true);
	}

	/**
	 * This method configures and adds the listeners for all components that need one
	 */
	private void configureListeners() {
		headerThemeDropdown.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				// Selected index corresponds to enum ordinal
				int selectedIndex = headerThemeDropdown.getSelectedIndex();

				// Update theme config
				ThemeEnum themeEnum = ThemeEnum.INSTANCE.fromOrdinal(selectedIndex);
				ConfigUtils.updateConfig(ConfigUtils.THEME_PROPERTY, String.valueOf(themeEnum.ordinal()));

				// Update the UI
				try {
					UIManager.setLookAndFeel(themeEnum.getLookAndFeel());
					SwingUtilities.updateComponentTreeUI(this);
					this.pack();
				} catch (UnsupportedLookAndFeelException ignored) {}

				// Save theme to config file
				try {
					ConfigUtils.saveConfig();
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(this,
						"Could not save theme to configuration file.",
						"Config file error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		newFileBtn.addActionListener(e -> {
			String filename = JOptionPane.showInputDialog(this,
				"Enter file name:",
				"New file", JOptionPane.INFORMATION_MESSAGE).trim();

			if (filename.isEmpty())
				return;

			if (FileUtils.exists(filename)) {
				JOptionPane.showMessageDialog(this,
					"File with name: \"" + filename + "\" already exists.",
					"File creation error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				FileUtils.saveDbFile(filename, "");
				GitController.INSTANCE.add();

				JButton newFile = new JButton(filename);
				newFile.setMaximumSize(new Dimension(Integer.MAX_VALUE, newFile.getPreferredSize().height));
				newFile.setForeground(UIUtils.GIT_CREATED_FILE_COLOR);
				newFile.addActionListener(getFileTreeBtnListener(filename));
				fileTreePanel.add(newFile);
				fileTreePanel.revalidate();

				openedFilename = filename;
				newFile.doClick();
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(this,
					"Could not create file.",
					"File creation error", JOptionPane.ERROR_MESSAGE);
			} catch (InterruptedException ex) {
				JOptionPane.showMessageDialog(this,
					"Problem adding files to local git repo.",
					"Git add error", JOptionPane.ERROR_MESSAGE);
			}
		});

		updateBtn.addActionListener(e -> {
			try {
				boolean isUpdateSuccess = GitController.INSTANCE.update();
				if (!isUpdateSuccess) {
					JOptionPane.showMessageDialog(this,
						"Update was not successful.",
						"Update error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				updateFileTree();
				JOptionPane.showMessageDialog(this,
					"Update was successful!",
					"Update success", JOptionPane.INFORMATION_MESSAGE);
			} catch (IOException | InterruptedException ex) {
				JOptionPane.showMessageDialog(this,
					"Problem pulling files from remote git repo.",
					"Git pull error", JOptionPane.ERROR_MESSAGE);
			}
		});

		pushBtn.addActionListener(e -> {
			String commitMessage = commitMsgTextField.getText().trim();
			if (commitMessage.isEmpty()) {
				JOptionPane.showMessageDialog(this,
					"Please provide a commit message.",
					"Git commit error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				boolean isPushSuccess = GitController.INSTANCE.commitChangesOnRemote(commitMessage);
				if (!isPushSuccess) {
					JOptionPane.showMessageDialog(this,
						"Problem pushing changes to remote git repo.",
						"Git push error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				JOptionPane.showMessageDialog(this,
					"Changes were successfully pushed to remote git repo.",
					"Git push success", JOptionPane.INFORMATION_MESSAGE);
			} catch (IOException | InterruptedException ex) {
				JOptionPane.showMessageDialog(this,
					"Problem pushing changes from local repo.",
					"Git push error (local)", JOptionPane.ERROR_MESSAGE);
			}
		});
	}

	/**
	 * Sets up basic keystrokes (like Ctrl + S etc.)
	 */
	private void configureKeystrokes() {
		Component mainWindow = this;
		Action saveAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					boolean isExists = FileUtils.saveDbFile(openedFilename, editorTextArea.getText());

					Component[] components = fileTreePanel.getComponents();
					for (int i = 0; i < components.length; i++) {
						if (components[i] instanceof JButton fileBtn
							&& openedFilename.equals(fileBtn.getText())) {
							fileBtn.setForeground(isExists ? UIUtils.GIT_MODIFIED_FILE_COLOR : UIUtils.GIT_NEW_FILE_COLOR);
						}
					}
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(mainWindow, "Could not save file.");
				}
			}
		};

		InputMap inputMap = aggregatePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = aggregatePanel.getActionMap();

		// Define key stroke for "Ctrl + S"
		KeyStroke ctrlSKeyStroke = KeyStroke.getKeyStroke("control S");
		inputMap.put(ctrlSKeyStroke, "saveAction");
		actionMap.put("saveAction", saveAction);
	}

	private void updateFileTree() {
		fileTreePanel.removeAll();

		for (String filename : FileUtils.getAllDbFiles()) {
			JButton fileBtn = new JButton(filename);
			fileBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, fileBtn.getPreferredSize().height));
			fileBtn.addActionListener(getFileTreeBtnListener(filename));
			fileTreePanel.add(fileBtn);
		}

		fileTreePanel.revalidate();
	}

	private ActionListener getFileTreeBtnListener(String filename) {
		return e -> {
			// Open file content on editor tab
			try {
				String fileContent = FileUtils.getDbFile(filename);
				if (fileContent == null) {
					JOptionPane.showMessageDialog(this, "DB file does not exist.");
					return;
				}
				editorTextArea.setText(fileContent);
				openedFilename = filename;
			} catch (IOException ignored) {}
		};
	}

	private void setConstraints(GridBagConstraints c, int gridx, int gridy, double weightx, double weighty, int anchor, Insets insets) {
		c.gridx = gridx;
		c.gridy = gridy;
		c.weightx = weightx;
		c.weighty = weighty;
		c.anchor = anchor;
		c.insets = insets;
	}

	private void setConstraints(GridBagConstraints c, int gridx, int gridy, double weightx, double weighty, int anchor, Insets insets, int fill) {
		c.gridx = gridx;
		c.gridy = gridy;
		c.weightx = weightx;
		c.weighty = weighty;
		c.anchor = anchor;
		c.insets = insets;
		c.fill = fill;
	}
}
