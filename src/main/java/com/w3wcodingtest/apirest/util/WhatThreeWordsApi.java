package com.w3wcodingtest.apirest.util;

import com.what3words.javawrapper.What3WordsV3;
import lombok.extern.log4j.Log4j2;

import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Pattern;

@Log4j2
public final class WhatThreeWordsApi {

    private static final Properties applicationProperties;
    private static final Pattern threeWordsPattern = Pattern.compile("^/*(?:(?:\\p{L}\\p{M}*)+[.｡。･・︒។։။۔።।](?:\\p{L}\\p{M}*)+[.｡。･・︒។։။۔።।](?:\\p{L}\\p{M}*)+|(?:\\p{L}\\p{M}*)+([\u0020\u00A0](?:\\p{L}\\p{M}*)+){1,3}[.｡。･・︒។։။۔።।](?:\\p{L}\\p{M}*)+([\u0020\u00A0](?:\\p{L}\\p{M}*)+){1,3}[.｡。･・︒។։။۔።।](?:\\p{L}\\p{M}*)+([\u0020\u00A0](?:\\p{L}\\p{M}*)+){1,3})$");

    private WhatThreeWordsApi(){}

    static {
        applicationProperties = new Properties();

        try {
            ClassLoader classLoader = WhatThreeWordsApi.class.getClassLoader();
            InputStream applicationPropertiesStream = classLoader.getResourceAsStream("application.properties");
            applicationProperties.load(applicationPropertiesStream);
        } catch (Exception exception) {
            log.error("Error while loading application.properties file");
        }
    }

    public static What3WordsV3 getWrapper() {
        return new What3WordsV3(applicationProperties.getProperty("what3words.apiKey"));
    }

    public static boolean isValidThreeWordAddress(String threeWordAddress){
        return threeWordsPattern.matcher(threeWordAddress).find();
    }
}
