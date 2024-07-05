package edu.gradinfo.dashboard.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class ImageUtils {
	public static BufferedImage resize(BufferedImage img, int newWidth, int newHeight) {
		Image tmp = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
		BufferedImage resized = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = resized.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return resized;
	}

}
