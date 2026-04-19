package net.mega2223.bloginterpreter;

import net.mega2223.bloginterpreter.util.BlogEntry;
import net.mega2223.bloginterpreter.util.FileManager;
import net.mega2223.bloginterpreter.util.Templates;
import net.mega2223.bloginterpreter.util.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

public class BlogInterpreter {

    //TODO anotação de scripts

    public static final Properties PROPERTIES = new Properties();
    public static final ArrayList<BlogEntry> ENTRIES = new ArrayList<>(30);

    public static String FILE_SEPARATOR = File.separator;

    public static void main(String[] args) {
//        PROPERTIES.setProperty("src",src.getAbsolutePath());
//        PROPERTIES.setProperty("dest",dest.getAbsolutePath());
        argReader: for (int i = 0; i < args.length; i++) {
            String arg = args[i].strip();
            switch (arg){
                case "--html-template":
                    i++;
                    Templates.HTMLPageTemplate = args[i];
                    continue;
                case "--":
                default:
                    PROPERTIES.setProperty("src",args[i]);
                    PROPERTIES.setProperty("dest",args[i+1]);
                    break argReader;
            }
        }

        File src = new File(PROPERTIES.getProperty("src"));
        PROPERTIES.setProperty("src",src.getAbsolutePath());
        File dest = new File(PROPERTIES.getProperty("dest"));
        PROPERTIES.setProperty("dest",dest.getAbsolutePath());

        Templates.initTemplates(src.getAbsolutePath());
        Utils.DEBUG_LEVEL = Utils.DEBUG_VERBOSE;

        FileManager.compile(src,dest);
    }
}