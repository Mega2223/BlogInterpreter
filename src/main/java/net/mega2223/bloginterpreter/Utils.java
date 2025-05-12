package net.mega2223.bloginterpreter;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Utils {
    private Utils(){}

    public static final String DELIMITER_B = "::";
    public static final String DELIMITER_E = "::";

    public static int DEBUG_LEVEL = 0;
    public static boolean saveLog = false;
    private static final StringBuilder log = new StringBuilder();

    public static final int DEBUG_IMPORTANT = 0;
    public static final int DEBUG_TASKS = 2;
    public static final int DEBUG_VERBOSE = 4;

    public static void log(String dat, int level){
        if(level <= DEBUG_LEVEL){
            System.out.println(dat);
            if(saveLog){
                log.append(dat);
            }
        }
    }

    public static void saveLog(){
        saveFile(new File(System.getProperty("User.dir")), "log.txt", log);
    }
    public static void saveFile(File directory, String fileName, StringBuilder data){
        saveFile(directory,fileName,data.toString());
    }
    public static void saveFile(File directory, String fileName, String data){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(directory.getAbsolutePath() + "\\" + fileName));
            writer.write(data);
            writer.close();
            log("Saved file " + fileName, DEBUG_TASKS);
        } catch (IOException e) {
            log("WARNING: COULD NOT SAVE FILE " + fileName, DEBUG_IMPORTANT);
            log(e.toString(), DEBUG_IMPORTANT);
//            throw new RuntimeException(e);
        }
    }

    public static StringBuilder readFile(File file){
        try {
            BufferedReader r = new BufferedReader(new FileReader(file));
            StringBuilder data = new StringBuilder();
            while(r.ready()){
                data.append(r.readLine());
                data.append("\n");
            }
            return data;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static File getFile(String name){
        String pathname = Objects.requireNonNull(Utils.class.getClassLoader().getResource(name)).toString();
        pathname = pathname.substring(5); //whar
        return new File(URLDecoder.decode(pathname, StandardCharsets.UTF_8));
    }

    public static String changeExtension(String name, String newExtension){
        String[] d = name.split("\\.");
        String extension = d[d.length-1];
        return name.substring(0,name.length()-extension.length()) + newExtension;
    }

    public static String solveReplace(String dat, String tag, String sub){
        return dat.replace(DELIMITER_B+tag+DELIMITER_E,sub);
    }

}
