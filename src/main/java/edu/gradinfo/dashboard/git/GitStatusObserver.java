package edu.gradinfo.dashboard.git;

import edu.gradinfo.dashboard.enums.GitStatusEnum;
import edu.gradinfo.dashboard.util.ConfigUtils;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

@SuppressWarnings("deprecation")
public class GitStatusObserver implements Observer {
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof GitController && arg instanceof GitStatusEnum newGitStatus) {

			ConfigUtils.updateConfig(ConfigUtils.GIT_PROPERTY, String.valueOf(newGitStatus.ordinal()));
			try {
				ConfigUtils.saveConfig();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}
}