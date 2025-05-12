package net.mega2223.bloginterpreter;

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
                    compileContent(folder,dest);
                case "media":
                    break;
                case "templates":
                    compileTemplates(folder,dest);
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
        Utils.log("Compiling templates at " + tree + "|\\" + srcFolder.getName(),Utils.DEBUG_TASKS);
        File[] files = srcFolder.listFiles();
        Objects.requireNonNull(files);

        for (File act : files) {
            if (act.isDirectory()) {
                compileTemplates(act, destFolder, tree + "\\" + act.getName());
                continue;
            }
            String data = Utils.readFile(act).toString();
            data = Interpreter.solveHTML(data);
            Utils.saveFile(new File(destFolder.getAbsoluteFile() + tree), act.getName(), data);
        }
    }

    static void compileTemplates(File srcFolder, File destFolder){
        compileTemplates(srcFolder,destFolder,"");
    }

    static void compileContent(File srcFolder, File destFolder, String tree){
        Utils.log("Compiling content templates at " + tree + "|\\" + srcFolder.getName(),Utils.DEBUG_TASKS);
        File[] files = srcFolder.listFiles();
        Objects.requireNonNull(files);

        for (File act : files) {
            if (act.isDirectory()) {
                compileContent(act, destFolder, tree + "\\" + act.getName());
                continue;
            }
            String data = Utils.readFile(act).toString();
            data = MarkdownInterpreter.mdToHTML(data);
            String dest = act.getName();
            dest = Utils.changeExtension(dest,"html");
            Utils.saveFile(new File(destFolder.getAbsoluteFile() + tree), dest, data);
        }
    }

    static void compileContent(File srcFolder, File destFolder){
        compileContent(srcFolder, destFolder, "");
    }
}
