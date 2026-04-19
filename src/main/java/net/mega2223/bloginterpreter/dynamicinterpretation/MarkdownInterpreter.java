package net.mega2223.bloginterpreter.dynamicinterpretation;

import net.mega2223.bloginterpreter.BlogInterpreter;
import net.mega2223.bloginterpreter.util.BlogEntry;
import net.mega2223.bloginterpreter.util.Templates;
import net.mega2223.bloginterpreter.util.Utils;

import java.io.File;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarkdownInterpreter {

    private MarkdownInterpreter(){}

    public static final String PROPERTIES_PREFIX = "%-";

    static String mdToHTML(String data, String link){
        //TODO eu nao vejo proposito em iterar por linhas
        StringBuilder bodyB = new StringBuilder();
        Properties properties = new Properties();
        properties.setProperty("link",link);
        String[] lines = data.split("\n");

        for (String line : lines) {
            bodyB.append(lineToHTML(line, properties));
        }

        String htmlBody = bodyB.toString();
        File templateFile = new File(Templates.HTMLPageTemplate);
        String htmlText = templateFile.exists() ?
                Utils.readFile(templateFile).toString() :
                Utils.readFile(Utils.getFile("TEMPLATE.html")).toString();

        htmlText = HTMLInterpreter.replacePatternByElement(htmlText,"title",properties.getProperty("title"));
        htmlText = HTMLInterpreter.replacePatternByElement(htmlText,"head",headProperties(properties));
        htmlText = HTMLInterpreter.replacePatternByElement(htmlText,"body",htmlBody);

//        compileEntry(properties);
        // TODO quebrou tudo aqui kkkmkkkajfk

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
        for (int i = 0; line.charAt(i) == '#'; i++) { // TODO meu deus usa um regex aqui
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

        return b.append("\n").toString();
    }

    static String headProperties(Properties properties){
        StringBuilder head = new StringBuilder();
        //TODO script JS
        String[] styles = properties.getProperty("style").split(",");
        for(String style : styles){
            style = style.strip();
            File root = new File(BlogInterpreter.PROPERTIES.getProperty("src"));
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

    static final String MARKDOWN_HYPERLINK_PATTERN = "\\[([^\\[\\]()]+)]\\(([^\\[\\]()]+)\\)";
    static final String MARKDOWN_ITALICS_PATTERN = "_([^_]+)_";
    static final String MARKDOWN_BOLD_PATTERN = "\\*\\*([^*]+)\\*\\*";
    static final String PROPERTY_PATTERN = "%-? +([^= ]+)? += +(.+)[ \\n]?";
    static final String MARKDOWN_IMAGE_PATTERN = "!\\[([^\\[\\]()]+)]\\(([^\\[\\]()]+)\\)";

    static Pattern[] markdownPatterns = {
            Pattern.compile(MARKDOWN_HYPERLINK_PATTERN),
            Pattern.compile(MARKDOWN_ITALICS_PATTERN),
            Pattern.compile(MARKDOWN_BOLD_PATTERN),
            Pattern.compile(PROPERTY_PATTERN),
            Pattern.compile(MARKDOWN_IMAGE_PATTERN)
    };
    /**
     * Resolve hiperlinks e formatação HTML (breaks, bold, italics etc)
     * */
    public static String formatParagraph(String paragraph){
        StringBuilder formattedParagraph = new StringBuilder();
        Matcher imageMatcher = markdownPatterns[4].matcher(paragraph);
        while(imageMatcher.find()){
            String imgDesc = imageMatcher.group(1);
            String link = imageMatcher.group(2);
            String hypertextLink = HTMLInterpreter.getImageEmbed(link,imgDesc);
            imageMatcher.appendReplacement(formattedParagraph,hypertextLink);
        }
        imageMatcher.appendTail(formattedParagraph);

        Matcher hyperlinkMatcher = markdownPatterns[0].matcher(formattedParagraph);
        formattedParagraph = new StringBuilder();
        while(hyperlinkMatcher.find()){
            String displayText = hyperlinkMatcher.group(1);
            String link = hyperlinkMatcher.group(2);
            String hypertextLink = HTMLInterpreter.getHyperlink(displayText,link);
            hyperlinkMatcher.appendReplacement(formattedParagraph,hypertextLink);
        }
        hyperlinkMatcher.appendTail(formattedParagraph);

        Matcher italicsMatcher = markdownPatterns[1].matcher(formattedParagraph);
        formattedParagraph = new StringBuilder();
        while(italicsMatcher.find()){
            String word = italicsMatcher.group(1);
            String italicized = HTMLInterpreter.getItalicized(word);
            italicsMatcher.appendReplacement(formattedParagraph,italicized);
        }
        italicsMatcher.appendTail(formattedParagraph);

        Matcher boldMatcher = markdownPatterns[2].matcher(formattedParagraph);
        formattedParagraph = new StringBuilder();
        while(boldMatcher.find()){
            String word = boldMatcher.group(1);
            String bolded = HTMLInterpreter.getBold(word);
            boldMatcher.appendReplacement(formattedParagraph,bolded);
        }
        boldMatcher.appendTail(formattedParagraph);

        return formattedParagraph.toString();
    }

    public static Properties extractProperties(String markdownData){
        Properties properties = new Properties();
        Matcher matcher = markdownPatterns[3].matcher(markdownData);
        while(matcher.find()){
            properties.setProperty(matcher.group(1), matcher.group(2));
        }
        return properties;
    }

    /**
     * Compila o conteúdo markdown nessa pasta e em todas as sub-pastas de forma recursiva.
     * */
    public static void compileMdContent(File srcFolder, File destFolder, String fileTree){
        Utils.log("Compiling md templates at " + fileTree + "|\\" + srcFolder.getName(),Utils.DEBUG_TASKS);
        File[] files = srcFolder.listFiles();
        if(files == null){
            System.err.println("File \"" + srcFolder.getAbsolutePath() + "\" is not a folder");
            System.exit(-2);
        }

        for (File act : files) {
            if (act.isDirectory()) {
                compileMdContent(act, destFolder, fileTree + BlogInterpreter.FILE_SEPARATOR + act.getName());
                continue;
            }
            String data = Utils.readFile(act).toString();
            Properties p = extractProperties(data);
            String URL = fileTree + BlogInterpreter.FILE_SEPARATOR + act.getName().replace(".md", ".html");
            BlogInterpreter.ENTRIES.add(new BlogEntry(p,data,URL));
            data = mdToHTML(data, URL); //todo a TREE funciona direito?
            String dest = act.getName();
            dest = Utils.changeExtension(dest,"html");
            Utils.saveFile(new File(destFolder.getAbsoluteFile() + fileTree), dest, data);
        }
    }

    /**
     * Compila o conteúdo markdown nessa pasta e em todas as sub-pastas de forma recursiva.
     * */
    public static void compileMdContent(File srcFolder, File destFolder){
        compileMdContent(srcFolder, destFolder, "");
    }
}
