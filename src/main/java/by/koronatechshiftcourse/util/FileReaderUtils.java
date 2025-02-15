package by.koronatechshiftcourse.util;

import by.koronatechshiftcourse.exception.IOFileException;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@UtilityClass
public class FileReaderUtils {

    public static List<String> readFromFile(String filePath) {
        try {
            return Files.readAllLines(Paths.get(filePath)).stream()
                    .toList();
        } catch (IOException e) {
            throw new IOFileException("Reading file error!");
        }
    }

}