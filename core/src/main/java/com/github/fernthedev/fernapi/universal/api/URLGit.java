package com.github.fernthedev.fernapi.universal.api;

import com.github.fernthedev.fernapi.universal.util.VersionUtil;
import lombok.Getter;

/**
 * Holds URLs for github files that may change in the future.
 */
@Getter
public class URLGit {

    private static final String tag = VersionUtil.getVersionData().getFernapi_version();

//    static {
//        Package pack = URLGit.class.getPackage();
//        String checkTag = pack.getSpecificationVersion();
//
//        if (checkTag == null) checkTag = pack.getImplementationVersion();
//        if (checkTag == null) throw new NullPointerException("Version is somehow null. Checked specification and implementation");
//
//        tag = checkTag;
//    }

    public static String formatString(String string) {
        return string
                .replaceAll("%tag%", tag);
    }

    public static final String MAIN_PATH = "https://github.com/Fernthedev/FernAPI/";
    public static final String FILE_PATH = MAIN_PATH + "blob/%tag%/";
    public static final String DOCS = FILE_PATH + "docs/";

    public static final String INCORRECT_SETUP = formatString(DOCS + "error/incorrectsetup.md");



}
