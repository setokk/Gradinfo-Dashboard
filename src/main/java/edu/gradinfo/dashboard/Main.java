package edu.gradinfo.dashboard;

import edu.gradinfo.dashboard.gui.SplashScreen;
import edu.gradinfo.dashboard.util.ConfigUtils;
import edu.gradinfo.dashboard.git.GitController;
import edu.gradinfo.dashboard.util.UIUtils;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.io.IOException;

public class Main {
	public static void main(String[] args) throws UnsupportedLookAndFeelException, IOException, InterruptedException {
		ConfigUtils.loadConfig();
		GitController.INSTANCE.init();

		UIManager.setLookAndFeel(UIUtils.getLookAndFeelFromConfig());
		UIManager.put("TextField.font", UIUtils.DEFAULT_FONT);
		UIManager.put("Label.font", UIUtils.DEFAULT_FONT);
		UIManager.put("Button.font", UIUtils.DEFAULT_BUTTON_FONT);
		UIManager.put("CheckBox.font", UIUtils.DEFAULT_FONT);
		UIManager.put("CheckBoxMenuItem.font", UIUtils.DEFAULT_FONT);
		UIManager.put("TextArea.font", UIUtils.DEFAULT_TEXTAREA_FONT);

		new SplashScreen();
	}
}
