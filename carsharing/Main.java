package carsharing;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String... args) throws SQLException, ClassNotFoundException {
        // write your code here
        String filename = "";
        try{
            filename = args[1];
        } catch (Exception e){}

        new app(filename);
    }
}