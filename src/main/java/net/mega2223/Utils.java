package net.mega2223;

import java.io.*;

public class Utils {
    private Utils(){}

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
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(directory.getAbsolutePath() + "\\" + fileName)));
            writer.write(data.toString());
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
            }
            return data;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
