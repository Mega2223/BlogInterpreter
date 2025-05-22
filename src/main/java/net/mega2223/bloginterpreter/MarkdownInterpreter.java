package net.mega2223.bloginterpreter;

import java.io.File;
import java.util.Properties;

public class MarkdownInterpreter {

    private MarkdownInterpreter(){}

    public static final String PROPERTIES_PREFIX = "%-";

    public static String mdToHTML(String data){
        StringBuilder html = new StringBuilder();
        Properties properties = new Properties();
        String[] lines = data.split("\n");

        for (String line : lines) {
            html.append(lineToHTML(line, properties));
        }

        String htmlBody = html.toString();
        String htmlText = Utils.readFile(Utils.getFile("TEMPLATE.html")).toString();

        htmlText = Utils.solveReplace(htmlText,"body",htmlBody);
        htmlText = Utils.solveReplace(htmlText,"title",properties.getProperty("title"));
        htmlText = Utils.solveReplace(htmlText,"head",headProperties(properties));

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

        String tag = HTMLInterpreter.produceHTMLTag(delimiter,line);
        b.append(tag);

        System.out.println(b);
        return b.append("\n").toString();
    }

    static String headProperties(Properties properties){
        StringBuilder head = new StringBuilder();
        //TODO script
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
                    HTMLInterpreter.produceHTMLTag("link",props,false,"")
            ).append("\n");
        }

        return head.toString();
    }
}
