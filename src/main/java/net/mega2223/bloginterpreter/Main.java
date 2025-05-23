package net.mega2223.bloginterpreter;

import net.mega2223.bloginterpreter.util.BlogEntry;
import net.mega2223.bloginterpreter.util.FileManager;
import net.mega2223.bloginterpreter.util.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

public class Main {

    public static final Properties PROPERTIES = new Properties();
    public static final ArrayList<BlogEntry> entries = new ArrayList<>(30);

    public static void main(String[] args) {
        File src = new File(args[0]);
        File dest = new File(args[1]);

        PROPERTIES.setProperty("src",src.getAbsolutePath());
        PROPERTIES.setProperty("dest",dest.getAbsolutePath());

        Utils.DEBUG_LEVEL = Utils.DEBUG_VERBOSE;

        FileManager.compile(src,dest);
    }
}