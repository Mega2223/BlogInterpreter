package net.mega2223.bloginterpreter.dynamicinterpretation;

import net.mega2223.bloginterpreter.Main;
import net.mega2223.bloginterpreter.util.BlogEntry;
import net.mega2223.bloginterpreter.util.Templates;
import net.mega2223.bloginterpreter.util.Utils;

import java.io.File;
import java.sql.Date;
import java.util.Objects;
import java.util.Properties;

public class MarkdownInterpreter {

    private MarkdownInterpreter(){}

    public static final String PROPERTIES_PREFIX = "%-";

    static String mdToHTML(String data, String link){
        StringBuilder html = new StringBuilder();
        Properties properties = new Properties();
        properties.setProperty("link",link);
        String[] lines = data.split("\n");

        for (String line : lines) {
            html.append(lineToHTML(line, properties));
        }

        String htmlBody = html.toString();
        File templateFile = new File(Templates.HTMLPageTemplate);
        String htmlText = templateFile.exists() ?
                Utils.readFile(templateFile).toString() :
                Utils.readFile(Utils.getFile("TEMPLATE.html")).toString();

        htmlText = HTMLInterpreter.replacePatternByElement(htmlText,"body",htmlBody);
        htmlText = HTMLInterpreter.replacePatternByElement(htmlText,"title",properties.getProperty("title"));
        htmlText = HTMLInterpreter.replacePatternByElement(htmlText,"head",headProperties(properties));

        compileEntry(properties);

        return htmlText;
    }

    static String lineToHTML(String line, Properties properties){
        StringBuilder b = new StringBuilder();

        line = line.strip();
        if(line.isEmpty()){return line;}

        if(line.startsWith(PROPERTIES_PREFIX)){
            line = line.substring(PROPERTIES_PREFIX.length()).strip();
            String[] split = line.split("=");
            String prop = split[0].strip(), val = split[1].strip();
            properties.setProperty(prop,val);
            return "";
        }

        int headerLevel = 0;
        for (int i = 0; line.charAt(i) == '#'; i++) {
            headerLevel++;
        }
        line = line.substring(headerLevel).strip();

        String delimiter = "p";
        if(headerLevel > 0){
            delimiter = "h" + headerLevel;
        }

        line = formatParagraph(line);

        String tag = HTMLInterpreter.generateHTMLTag(delimiter,line);
        b.append(tag);

//        System.out.println(b);
        return b.append("\n").toString();
    }

    static String headProperties(Properties properties){
        StringBuilder head = new StringBuilder();
        //TODO script JS
        String[] styles = properties.getProperty("style").split(",");
        for(String style : styles){
            style = style.strip();
            File root = new File(Main.PROPERTIES.getProperty("src"));
            File stylesheet = Utils.recursiveSearch(root,style);
            if(stylesheet == null){
                Utils.log("WARNING: COULD NOT FIND STYLESHEET" + style,Utils.DEBUG_IMPORTANT);
                continue;
            }
            //<link rel="stylesheet" href="index.css">
            String rootPath = "src"+Utils.getRelativePath(root,stylesheet);
            String[][] props = {
                    {"rel","stylesheet"},
                    {"href",rootPath}
            };
            head.append(
                    HTMLInterpreter.generateHTMLTag("link",props,false,"")
            ).append("\n");
        }

        return head.toString();
    }

    public static void compileEntry(Properties properties){
        String[] authors = properties.getProperty("authors").split(",");
        String[][] authorsNsources = new String[authors.length][2];

        for (int i = 0; i < authors.length; i++) {
            String[] split = authors[0].split(":");
            authorsNsources[i][0] = split[0];
            authorsNsources[i][1] = split.length > 1 ? split[1] : "";
        }

        String date = properties.getProperty("date");
        //System.out.println(date);
        Main.ENTRIES.add(new BlogEntry(
                properties.getProperty("title"),
                properties.getProperty("description"),
                "uh oh", // não sei como faço isso no modelo atual de forma limpa :(
                Date.valueOf(date), //TODO suporte para a hora, 22:23 atualmente (nice)
                null, //update history, TODO
                authorsNsources,
                0, // TODO
                properties.getProperty("thumbnail"),//new File(properties.getProperty("thumbnail")), // TODO isso não funciona eu acho
                "true".equals(properties.getProperty("show_at_index")), //true no lado direito evita NullPointerEx
                properties.getProperty("link")
        ));
    }
    /**
     * Resolve hiperlinks e formatação HTML (breaks, bold, italics etc)
     * */
    public static String formatParagraph(String paragraph){
        //TODO
        paragraph = paragraph.strip();
        int openBrack = paragraph.indexOf('[');
        outer: while(openBrack != -1){
            boolean isImageEmbed = openBrack > 0 && paragraph.charAt(openBrack-1) == '!';
            int closingBrack = paragraph.indexOf(']',openBrack);
            if(closingBrack == -1){break;}
            int openPar = closingBrack + 1, closingPar;
            while(true){
                char c = paragraph.charAt(openPar);
                if (c == '(') {break;}
                else if(c != ' '){
                    openBrack = paragraph.indexOf('[',openBrack+1);
                    continue outer;
                }
                openPar++;
            }

            closingPar = paragraph.indexOf(')',openPar);
            String content = paragraph.substring(openBrack+1,closingBrack);
            String link = paragraph.substring(openPar+1,closingPar);
            String textChain = paragraph.substring(isImageEmbed ? openBrack - 1 : openBrack,closingPar+1);
            String htmlChain = isImageEmbed ? HTMLInterpreter.generateImageEmbed(link,content) : HTMLInterpreter.generateHyperlink(content,link);
            Utils.log("Replacing " + textChain + " with " + htmlChain, Utils.DEBUG_VERBOSE);
            paragraph = paragraph.replace(textChain,htmlChain);
        }
        return paragraph;
    }

    /**
     * Compila o conteúdo markdown nessa pasta e em todas as sub-pastas de forma recursiva.
     * */
    public static void compileMdContent(File srcFolder, File destFolder, String tree){
        Utils.log("Compiling content templates at " + tree + "|\\" + srcFolder.getName(),Utils.DEBUG_TASKS);
        File[] files = srcFolder.listFiles();
        Objects.requireNonNull(files);

        for (File act : files) {
            if (act.isDirectory()) {
                compileMdContent(act, destFolder, tree + "\\" + act.getName());
                continue;
            }
            String data = Utils.readFile(act).toString();
            data = mdToHTML(data,tree+"\\"+act.getName().replace(".md",".html")); //todo a TREE funciona direito?
            String dest = act.getName();
            dest = Utils.changeExtension(dest,"html");
            Utils.saveFile(new File(destFolder.getAbsoluteFile() + tree), dest, data);
        }
    }

    /**
     * Compila o conteúdo markdown nessa pasta e em todas as sub-pastas de forma recursiva.
     * */
    public static void compileMdContent(File srcFolder, File destFolder){
        compileMdContent(srcFolder, destFolder, "");
    }
}
