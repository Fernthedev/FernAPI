package com.github.fernthedev.fernapi.universal.util;

import com.github.fernthedev.fernapi.universal.api.URLGit;
import com.google.gson.Gson;
import lombok.Getter;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

public class VersionUtil {

    @Getter
    private static final VersionData versionData = new Gson().fromJson(getFile("fernapi_version.json"), VersionData.class);


    public static String getFile(String fileName) {

        ClassLoader classLoader = URLGit.class.getClassLoader();

        Source fileSource = Okio.source(Objects.requireNonNull(classLoader.getResourceAsStream(fileName)));

        return getFile(fileSource);
    }

    public static String getFile(File file) throws FileNotFoundException {
        return getFile(Okio.source(file));
    }

    public static String getFile(Source file) {

        StringBuilder result = new StringBuilder();

        //Get file from resources folder
        //ClassLoader classLoader = StaticHandler.class.getClassLoader();

        try (BufferedSource bufferedSource = Okio.buffer(file)) {

            while (true) {
                String line = bufferedSource.readUtf8Line();
                if (line == null) break;


                result.append(line).append("\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();

    }

}
