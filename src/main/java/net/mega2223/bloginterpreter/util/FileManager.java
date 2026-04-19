package net.mega2223.bloginterpreter.util;

import net.mega2223.bloginterpreter.BlogInterpreter;
import net.mega2223.bloginterpreter.dynamicinterpretation.HTMLInterpreter;
import net.mega2223.bloginterpreter.dynamicinterpretation.MarkdownInterpreter;
import net.mega2223.bloginterpreter.specialcases.SpecialCases;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class FileManager {
    private FileManager(){}
    //TODO suporte a recursão de arquivos
    public static void compile(File src, File dest){
        recursiveClean(dest);
        File[] folders = src.listFiles();
        Objects.requireNonNull(folders);

        String source = src.getAbsolutePath();

        File hypertextFolder = new File(source + BlogInterpreter.FILE_SEPARATOR +"hypertext");
        File markdownFolder = new File(source + BlogInterpreter.FILE_SEPARATOR + "markdown");
//        File mediaFolder = new File(source + BlogInterpreter.FILE_SEPARATOR +  "media");
        File styleFolder = new File(source + BlogInterpreter.FILE_SEPARATOR + "style");

        Utils.cloneFolder(styleFolder,dest);

        List<HTMLInterpreter.TagToReplace> tags = HTMLInterpreter.defaultTags();
        HTMLInterpreter.compileHypertextContent(hypertextFolder,dest, tags);
        MarkdownInterpreter.compileMdContent(markdownFolder,dest);

        SpecialCases.compileSpecialCases();
    }

    public static void recursiveClean(File dir){
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
                //FIXME harcoded src path :/
                recursiveClean(file);
            }
        }
    }

}
