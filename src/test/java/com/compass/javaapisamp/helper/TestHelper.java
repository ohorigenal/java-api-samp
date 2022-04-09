package com.compass.javaapisamp.helper;

import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.file.Files;

public class TestHelper {

    public static String readClassPathResource(String path) throws IOException {
        return Files.readString(
            ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + path).toPath()
        );
    }
}
