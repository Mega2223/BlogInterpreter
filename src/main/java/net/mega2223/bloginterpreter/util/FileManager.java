package net.mega2223.bloginterpreter.util;

import net.mega2223.bloginterpreter.dynamicinterpretation.HTMLInterpreter;
import net.mega2223.bloginterpreter.dynamicinterpretation.MarkdownInterpreter;
import net.mega2223.bloginterpreter.specialcases.SpecialCases;

import java.io.File;
import java.util.Objects;

public class FileManager {
    private FileManager(){}
    //TODO suporte a recursão de arquivos
    public static void compile(File src, File dest){
        clean(dest);
        File[] folders = src.listFiles();
        Objects.requireNonNull(folders);

        String source = src.getAbsolutePath();

        File hypertextFolder = new File(source + "\\hypertext");
        File contentFolder = new File(source + "\\content");
        File mediaFolder = new File(source + "\\media");
        File styleFolder = new File(source + "\\style");

        //Utils.cloneFolder(mediaFolder,dest);
        Utils.cloneFolder(styleFolder,dest);

        HTMLInterpreter.compileHypertextContent(hypertextFolder,dest);
        MarkdownInterpreter.compileMdContent(contentFolder,dest);

        SpecialCases.compileSpecialCases();
    }

    public static void clean(File dir){
        File[] files = dir.listFiles();
        Objects.requireNonNull(files);
        Utils.log("Cleaning root directory",Utils.DEBUG_TASKS);
        for (File file : files) {
            if (file.isFile()) {
                if(file.delete()){
                    Utils.log("Deleted " + file.getName(),Utils.DEBUG_DETAIL);
                } else {
                    Utils.log("Could not delete " + file.getName(),Utils.DEBUG_IMPORTANT);
                }
            } else if (!file.getName().equalsIgnoreCase("src")){
                clean(file);
            }
        }
    }

}
