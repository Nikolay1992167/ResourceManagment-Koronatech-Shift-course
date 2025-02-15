package by.koronatechshiftcourse.util.writer;

import by.koronatechshiftcourse.exception.IOFileException;
import by.koronatechshiftcourse.exception.ParameterNotFoundException;
import by.koronatechshiftcourse.util.Constants;
import by.koronatechshiftcourse.util.Property;

import java.io.FileWriter;
import java.io.IOException;

public class ToFileWriter implements Writer {

    @Override
    public void write(String string) {
        String outputFilePath = Property.PROPERTIES.get(Constants.PATH);

        if (outputFilePath == null)

            throw new ParameterNotFoundException(Constants.PATH);

        try (FileWriter toFileWriter = new FileWriter(outputFilePath, false)) {

            toFileWriter.write(string);

        } catch (IOException e) {
            throw new IOFileException("Writing file error!");
        }
    }

}