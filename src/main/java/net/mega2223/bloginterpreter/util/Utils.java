package net.mega2223.bloginterpreter.util;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Utils {
    private Utils(){}

    public static int DEBUG_LEVEL = 0;
    public static boolean saveLog = false;
    private static final StringBuilder log = new StringBuilder();

    public static final int DEBUG_IMPORTANT = 0;
    public static final int DEBUG_TASKS = 2;
    public static final int DEBUG_DETAIL = 3;
    public static final int DEBUG_VERBOSE = 4;
    public static final int DEBUG_SPAM = 5;

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

    public static File recursiveSearch(File root, String name){
        File[] files = root.listFiles();
        for (File file : files){
            if(file.isDirectory()){
                File f = recursiveSearch(file, name);
                if(f != null){ return f; }
            } else {
                if(file.getName().equals(name)){
                    return file;
                }
            }
        }
        return null;
    }

    public static String getRelativePath(File root, File file){
        String rootPath = root.getAbsolutePath();
        String filePath = file.getAbsolutePath();
        if(!filePath.contains(rootPath)){
            Utils.log("WARNING: FILE PATH IS NOT SUBDIRECTORY FROM ROOT",Utils.DEBUG_IMPORTANT);
            return null;
        }
        return filePath.replace(rootPath, "");
    }

    public static void cloneFolder(File folderToClone, File destinationRoot){
        File dest = new File(destinationRoot.getAbsolutePath()+"\\"+folderToClone.getName());
        dest.mkdirs();
        File[] files = folderToClone.listFiles();
        for (File f : files){
            if(f.isDirectory()){
                Utils.log("Cloning folder " + f.getName() + " into " + dest.getName(),Utils.DEBUG_TASKS);
                cloneFolder(f,dest);
            } else {
                File w = new File(dest.getAbsolutePath()+"\\"+f.getName());
                try {
                    Utils.log("Cloning file " + f.getName() + " into " + dest.getName(),Utils.DEBUG_TASKS);
                    FileReader reader = new FileReader(f);
                    FileWriter writer = new FileWriter(w);
                    while(reader.ready()){
                        writer.write(reader.read());
                    }
                    writer.close(); reader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
