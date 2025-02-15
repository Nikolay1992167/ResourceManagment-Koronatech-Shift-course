package by.koronatechshiftcourse.util;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class Property {

    private static final String ASSIGNMENT = "=";

    public static final Map<String, String> PROPERTIES = new HashMap<>();

    public static void configProperties(String[] args) {
        PROPERTIES.put(Constants.PATH_TO_JAR_FILE, args[0]);
        Map<String, String> collect = Arrays.stream(args)
            .map(str -> str.split(ASSIGNMENT))
            .filter(strings -> strings.length == 2)
            .collect(Collectors.toMap(strings -> strings[0].trim(), strings -> strings[1].trim()));
        PROPERTIES.putAll(collect);
    }

}