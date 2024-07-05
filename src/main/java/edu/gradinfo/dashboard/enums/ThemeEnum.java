package edu.gradinfo.dashboard.enums;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import lombok.Getter;

import javax.swing.plaf.basic.BasicLookAndFeel;

@Getter
public enum ThemeEnum implements IEnum<ThemeEnum> {
	LIGHT(new FlatLightLaf()),
	DARK(new FlatDarkLaf()),
	INTELLIJ(new FlatIntelliJLaf()),
	/* Used for using methods of IEnum */
	INSTANCE(null);

	private final BasicLookAndFeel lookAndFeel;

	ThemeEnum(BasicLookAndFeel lookAndFeel) {
		this.lookAndFeel = lookAndFeel;
	}
}
