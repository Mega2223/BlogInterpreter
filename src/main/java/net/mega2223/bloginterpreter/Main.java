package net.mega2223.bloginterpreter;

import java.io.File;
import java.util.Properties;

public class Main {

    public static final Properties PROPERTIES = new Properties();

    public static void main(String[] args) {
        File src = new File(args[0]);
        File dest = new File(args[1]);

        PROPERTIES.setProperty("src",src.getAbsolutePath());
        PROPERTIES.setProperty("dest",dest.getAbsolutePath());

        Utils.DEBUG_LEVEL = Utils.DEBUG_VERBOSE;

        FileManager.compile(src,dest);
    }
}