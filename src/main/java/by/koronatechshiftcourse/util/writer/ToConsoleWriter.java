package by.koronatechshiftcourse.util.writer;

public class ToConsoleWriter implements Writer {

    @Override
    public void write(String string) {
        System.out.println(string);
    }

}