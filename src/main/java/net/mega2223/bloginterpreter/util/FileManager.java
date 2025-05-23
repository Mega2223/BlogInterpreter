package net.mega2223.bloginterpreter.util;

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

        compileTemplates(new File(src.getAbsolutePath() + "\\templates"),dest);
        MarkdownInterpreter.compileMdContent(new File(src.getAbsolutePath() + "\\content"),dest);

//        for(File folder : folders){
//            if(folder.isFile()){continue;}
//            String name = folder.getName();
//            switch (name){
//                // FIXME pq isso funciona assim? só tem uma pasta pra cada categoria
//                // além disso tipicamente você quer que os stylesheets e as mídias passem
//                // antes do content e dos templates
//                case "content":
//                    MarkdownInterpreter.compileMdContent(folder,dest);
//                case "media":
//                    // TODO copiar pra cima pra não quebrar a leitura?
//                    break;
//                case "templates":
//                    compileTemplates(folder,dest);
//                    break;
//                case "style":
//                    // os stylesheets me parece que é melhor deixar aqui
//                    // em vez de copiar para cima
//                    break;
//            }
//        }
        SpecialCases.compileSpecialCases();
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
            Utils.saveFile(new File(destFolder.getAbsoluteFile() + tree), act.getName(), data);
        }
    }

    static void compileTemplates(File srcFolder, File destFolder){
        compileTemplates(srcFolder,destFolder,"");
    }

}
