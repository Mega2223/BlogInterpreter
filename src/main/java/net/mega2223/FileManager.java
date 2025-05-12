package net.mega2223;

import java.io.File;
import java.util.Objects;

public class FileManager {
    private FileManager(){}

    public static void compile(File src, File dest){
        clean(dest);
        File[] folders = src.listFiles();
        Objects.requireNonNull(folders);
        for(File folder : folders){
            if(folder.isFile()){continue;}
            String name = folder.getName();
            switch (name){
                case "content":
                    break;
                case "media":
                    break;
                case "templates":
                    compileTemplates(src,dest);
                    break;
                case "style":
                    break;
            }
        }
    }

    public static void clean(File dir){
        File[] files = dir.listFiles();
        Objects.requireNonNull(files);
        Utils.log("Cleaning root directory",Utils.DEBUG_TASKS);
        for (File file : files) {
            if (file.isFile()) {
                if(file.delete()){
                    Utils.log("Deleted " + file.getName(),Utils.DEBUG_VERBOSE);
                } else {
                    Utils.log("Could not delete " + file.getName(),Utils.DEBUG_VERBOSE);
                }
            } else if (!file.getName().equalsIgnoreCase("src")){
                clean(file);
            }
        }
    }

    static void compileTemplates(File srcFolder, File destFolder, String tree){
        Utils.log("Compiling templates at " + tree + "\\" + srcFolder.getName(),Utils.DEBUG_TASKS);
        File[] files = srcFolder.listFiles();
        Objects.requireNonNull(files);

        for (int i = 0; i < files.length; i++) {
            File act = files[i];
            if(act.isDirectory()){
                compileTemplates(act,destFolder,tree + "\\" + act.getName());
                continue;
            }
            Interpreter.solveHTML();
        }
    }

    static void compileTemplates(File srcFolder, File destFolder){
        compileTemplates(srcFolder,destFolder,"");
    }
}
