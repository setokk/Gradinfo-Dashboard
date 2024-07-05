package edu.gradinfo.dashboard.util;

import edu.gradinfo.dashboard.Main;
import edu.gradinfo.dashboard.enums.ThemeEnum;

import javax.imageio.ImageIO;
import javax.swing.plaf.basic.BasicLookAndFeel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class UIUtils {
	public static final Dimension WINDOW_DIMENSION = new Dimension(1000, 650);
	public static final Color BACKGROUND_COLOR = new Color(255, 191, 81, 255);
	public static final Color BACKGROUND_SECONDARY_COLOR = new Color(237, 240, 245, 255);
	public static final Color FOREROUND_COLOR = new Color(220, 220, 220, 255);
	public static final Color GIT_MODIFIED_FILE_COLOR = new Color(35, 127, 155, 255);
	public static final Color GIT_NEW_FILE_COLOR = new Color(64, 155, 35, 255);
	public static final Color GIT_CREATED_FILE_COLOR = new Color(155, 35, 35, 255);
	public static final Font DEFAULT_FONT = new Font("Segoe UI", Font.PLAIN, 19);
	public static final Font DEFAULT_BUTTON_FONT = new Font("Segoe UI", Font.PLAIN, 16);
	public static final Font DEFAULT_TEXTAREA_FONT = new Font("Segoe UI", Font.PLAIN, 18);
	public static final BufferedImage APP_ICON;
	static {
		try {
			InputStream is = Main.class.getResourceAsStream("icon.png");
			APP_ICON = ImageIO.read(is);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static BasicLookAndFeel getLookAndFeelFromConfig() throws IOException {
		var config = ConfigUtils.getConfig();

		ThemeEnum theme = ThemeEnum.INSTANCE.fromOrdinal(Integer.parseInt(config.get(ConfigUtils.THEME_PROPERTY)));
		if (theme == null)
			return ThemeEnum.LIGHT.getLookAndFeel();
		else
			return theme.getLookAndFeel();
	}
}
