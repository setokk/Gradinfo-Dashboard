package edu.gradinfo.dashboard.git;

import edu.gradinfo.dashboard.enums.GitStatusEnum;
import edu.gradinfo.dashboard.util.ConfigUtils;
import edu.gradinfo.dashboard.util.FileUtils;

import java.io.IOException;
import java.util.Observable;

import static edu.gradinfo.dashboard.enums.GitStatusEnum.*;

@SuppressWarnings("deprecation")
public final class GitController extends Observable {
    private GitStatusEnum gitStatus;
    public static final GitController INSTANCE = new GitController();

    public void init() {
        var config = ConfigUtils.getConfig();
        GitStatusEnum status = GitStatusEnum.INSTANCE.fromOrdinal(Integer.parseInt(config.get(ConfigUtils.GIT_PROPERTY)));
        setGitStatus((status == null) ? NOT_CLONED : status, false);
        INSTANCE.addObserver(new GitStatusObserver());
		/*int globalStatus = Runtime.getRuntime().exec(new String[]{ "git", "config", "--global", "--add",  "safe.directory", "*" }).waitFor();
		int emailStatus = Runtime.getRuntime().exec(new String[]{ "git", "config", "--global", "user.email", email }).waitFor();
		int nameStatus = Runtime.getRuntime().exec(new String[]{ "git", "config", "--global", "user.name", name }).waitFor();
		(globalStatus == 0 && emailStatus == 0 && nameStatus == 0)*/
    }

    public boolean update() throws IOException, InterruptedException {
        boolean isAddSuccess = add();
        boolean isStashSuccess = stash();
        boolean isPullSuccess = pull();
        boolean isPopSuccess = pop();
        boolean isAddSecondSuccess = add();

        return (isAddSuccess && isStashSuccess && isPullSuccess && isPopSuccess && isAddSecondSuccess);
    }

    public boolean commitChangesOnRemote(String commitMessage) throws IOException, InterruptedException {
        boolean isUpdateSuccess = update();
        if (!isUpdateSuccess)
            return false;

        boolean isCommitSuccess = commit("DB: " + commitMessage);
        boolean isPushSuccess = push();

        return (isCommitSuccess && isPushSuccess);
    }

    public void cloneRepo(String url) throws IOException, InterruptedException {
        boolean isSuccess = (Runtime.getRuntime().exec(new String[]{"git", "clone", url, FileUtils.PROJECT_NAME}).waitFor() == 0);
        if (isSuccess)
            setGitStatus(NONE, true);
    }

    public boolean pull() throws IOException, InterruptedException {
        return (Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "cd", FileUtils.PROJECT_ROOT, "&&", "git", "pull"}).waitFor() == 0);
    }

    public boolean stash() throws IOException, InterruptedException {
        boolean isSuccess = (Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "cd", FileUtils.PROJECT_ROOT, "&&", "git", "stash"}).waitFor() == 0);
        if (isSuccess)
            setGitStatus(STASHED, true);

        return isSuccess;
    }

    public boolean push() throws IOException, InterruptedException {
        boolean isSuccess = (Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "cd", FileUtils.PROJECT_ROOT, "&&", "git", "push", "origin", "main"}).waitFor() == 0);
        if (isSuccess)
            setGitStatus(NONE, true);

        return isSuccess;
    }

    public boolean commit(String message) throws IOException, InterruptedException {
        boolean isSuccess = (Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "cd", FileUtils.PROJECT_ROOT, "&&", "git", "commit", "-m", message}).waitFor() == 0);
        if (isSuccess)
            setGitStatus(COMMITTED, true);

        return isSuccess;
    }

    public boolean add() throws IOException, InterruptedException {
        boolean isSuccess = (Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "cd", FileUtils.PROJECT_ROOT, "&&", "git", "add", FileUtils.PROJECT_DB}).waitFor() == 0);
        if (isSuccess)
            setGitStatus(STAGED, true);

        return isSuccess;
    }

    public boolean pop() throws IOException, InterruptedException {
        boolean isSuccess = (Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "cd", FileUtils.PROJECT_ROOT, "&&", "git", "stash", "pop"}).waitFor() == 0);
        if (isSuccess)
            setGitStatus(STAGED, true);

        return isSuccess;
    }

    public void setGitStatus(GitStatusEnum gitStatus, boolean notifyObservers) {
        this.gitStatus = gitStatus;
        if (notifyObservers) {
            setChanged();
            notifyObservers(this.gitStatus);
        }
    }
}
