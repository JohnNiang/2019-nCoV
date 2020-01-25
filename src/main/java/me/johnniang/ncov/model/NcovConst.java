package me.johnniang.ncov.model;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author johnniang
 */
public class NcovConst {

    public static final String WORKSPACE_FOLDER = ".ncov";

    public static final String USER_HOME = System.getProperty("user.home");

    public static final Path LATEST_TIMESTAMP_PATH = Paths.get(USER_HOME, WORKSPACE_FOLDER + "/timestamp");

    private NcovConst() {
    }

}
