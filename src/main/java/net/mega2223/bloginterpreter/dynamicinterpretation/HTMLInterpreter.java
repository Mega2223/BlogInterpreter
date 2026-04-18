package net.mega2223.bloginterpreter.dynamicinterpretation;

import net.mega2223.bloginterpreter.util.Utils;

import java.io.File;
import java.util.Objects;
import java.util.Properties;

public class HTMLInterpreter {
    public static final String HTML_REPLACE_PATTERN = "::.+::";    // Talvez seja legal incluir a sintaxe de comentário no próprio pattern
    public static final String HTML_FULL_ELEMENT_PATH = "";
    public static final String HTML_TAG_PATTERN = "";

    private HTMLInterpreter(){}

    public static String generateHTMLTag(String tagName, String data){
        return generateHTMLTag(tagName,null,true,data);
    }

    public static String generateHTMLTag(String tagName, String[][] properties, String data){
        return generateHTMLTag(tagName,properties,true,data);
    }

    public static String generateHTMLTag(String tagName, String[][] properties, boolean closeTag){
        return generateHTMLTag(tagName,properties,closeTag,"");
    }

    public static String generateHTMLTag(String tagName, String[][] properties, boolean closeTag, String data){
        return generateHTMLTag(tagName, properties, closeTag, data, 0);
    }

    public static String generateHTMLTag(String tagName, String[][] properties, boolean closeTag, String data, int ident){
        StringBuilder b = new StringBuilder();
        b.repeat("  ", Math.max(0, ident));
        b.append("<").append(tagName);
        if(properties != null){
            for (String[] property : properties) {
                if(!(property[1].startsWith("\"") && property[1].endsWith("\""))){
                    property[1] = "\"" + property[1] + "\"";
                }
                b.append(" ").append(property[0]).append(" = ").append(property[1]).append(" ");
            }
        }
        b.append(">\n");
        if(closeTag){
            b.repeat("  ", Math.max(0, ident + 1));
            b.append(data).append("\n");
            b.append("</").append(tagName).append(">");
        }
        return b.toString();
    }

    public static String replacePatternByElement(String data, String elementName, String replacedText){
        // Dar replace em todos os elementos de data que cumprem o padrão "::elementName::"
        return null;
    }

    public static String generateHyperlink(String text, String link){
        return HTMLInterpreter.generateHTMLTag(
                "a",
                new String[][]{{"href",link}},
                text
        );
    }

    public static String generateImageEmbed(String imagePath, String alt){
        return HTMLInterpreter.generateHTMLTag(
                "img",
                new String[][]{
                        {"src",imagePath},
                        {"alt",alt}
                },
                false
        );
    }

    /**
     * @param link url???
     * */
    static String compileHTML(String data, String link){
        Properties properties = new Properties();
        properties.setProperty("link",link);
        String[] lines = data.split("\n");
        for(int i = 0; i < lines.length; i++){
            if (lines[i].startsWith(MarkdownInterpreter.PROPERTIES_PREFIX)){
                lines[i] = lines[i].substring(2);
                String[] spl = lines[i].split("=");
                properties.setProperty(spl[0].strip(),spl[1].strip());
            }
        }
        MarkdownInterpreter.compileEntry(properties);
        return data;
    }

    public static void compileHypertextContent(File srcFolder, File destFolder, String tree){
        Utils.log("Compiling HTML templates at " + tree + "|\\" + srcFolder.getName(),Utils.DEBUG_TASKS);
        File[] files = srcFolder.listFiles();
        Objects.requireNonNull(files);
        for (File act : files) {
            if (act.isDirectory()) {
                compileHypertextContent(act, destFolder, tree + "\\" + act.getName());
                continue;
            }
            String data = Utils.readFile(act).toString();
            data = compileHTML(data,tree+"\\"+act.getName());
            Utils.saveFile(new File(destFolder.getAbsoluteFile() + tree), act.getName(), data);
        }
    }

    public static void compileHypertextContent(File srcFolder, File destFolder){
        compileHypertextContent(srcFolder,destFolder,"");
    }
}
