package edu.gradinfo.dashboard.gui;

import edu.gradinfo.dashboard.enums.GitStatusEnum;
import edu.gradinfo.dashboard.git.GitController;
import edu.gradinfo.dashboard.util.ConfigUtils;
import edu.gradinfo.dashboard.util.ImageUtils;
import edu.gradinfo.dashboard.util.UIUtils;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Dimension;
import java.io.IOException;

public class SplashScreen extends JFrame {

    private JLabel imgLabel;

    public SplashScreen() throws InterruptedException, IOException {
        Dimension windowSize = new Dimension(256, 256);

        this.setMinimumSize(windowSize);
        this.setResizable(false);
        this.setUndecorated(true);
        this.setLocationRelativeTo(null);

        imgLabel = new JLabel("");
        imgLabel.setMinimumSize(windowSize);
        imgLabel.setIcon(new ImageIcon(ImageUtils.resize(UIUtils.APP_ICON, 256, 256)));
        this.add(imgLabel);

        this.pack();
        this.setVisible(true);

        Thread.sleep(800);
        this.dispose();

        // Show dialogue
        var config = ConfigUtils.getConfig();
        GitStatusEnum gitStatus = GitStatusEnum.INSTANCE.fromOrdinal(Integer.parseInt(config.get(ConfigUtils.GIT_PROPERTY)));
        if (gitStatus.equals(GitStatusEnum.NOT_CLONED)) {
            int option = JOptionPane.showConfirmDialog(null,
                "Would you like to clone the project?\n(If this is not the first time running the app, ignore this message)",
                "Clone project", JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.CLOSED_OPTION)
                System.exit(0);
            else if (option == JOptionPane.YES_OPTION)
                GitController.INSTANCE.cloneRepo("https://github.com/setokk/Gradinfo.git");
        }

        new HomePage();
    }
}
