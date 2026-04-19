package net.mega2223.bloginterpreter.dynamicinterpretation;

import net.mega2223.bloginterpreter.util.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLInterpreter {
    public static final String HTML_REPLACE_PATTERN_TEMPLATE = "::%s::";    // Talvez seja legal incluir a sintaxe de comentário no próprio pattern
    public static final String HTML_FULL_ELEMENT_PATH = "";
    public static final String HTML_TAG_PATTERN = "";

    public static final String BOLD_TEXT_TEMPLATE = "<b>%s</b>";
    public static final String ITALICIZED_TEXT_TEMPLATE = " <i>%s</i>";

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
        Pattern htmlReplacePattern = Pattern.compile(
                String.format(HTML_REPLACE_PATTERN_TEMPLATE,elementName)
        );
        Matcher m = htmlReplacePattern.matcher(data);
        return m.replaceAll(replacedText);
    }

    public static String getHyperlink(String text, String link){
        return HTMLInterpreter.generateHTMLTag(
                "a",
                new String[][]{{"href",link}},
                text
        );
    }

    public static String getImageEmbed(String imagePath, String alt){
        return HTMLInterpreter.generateHTMLTag(
                "img",
                new String[][]{
                        {"src",imagePath},
                        {"alt",alt}
                }, false
        );
    }

    public static String getBold(String content){
        return String.format(BOLD_TEXT_TEMPLATE,content);
    }

    public static String getItalicized(String content){
        return String.format(ITALICIZED_TEXT_TEMPLATE,content);
    }

    // Tarefa que compila os HTMLs, em tese deve fazer a recursão corretamente
    public static void compileHypertextContent(File srcFolder, File destFolder, String treeFromSrc, List<TagToReplace> tags){
        Utils.log("Compiling HTML templates at " + treeFromSrc + "|\\" + srcFolder.getName(),Utils.DEBUG_TASKS);
        File[] files = srcFolder.listFiles();
        if(files == null){
            System.err.println("File \"" + srcFolder.getAbsolutePath() + "\" is not a folder");
            System.exit(-2);
        }
        for (File act : files) {
            if (act.isDirectory()) {
                compileHypertextContent(act, destFolder, treeFromSrc + "\\" + act.getName(), tags);
                continue;
            }
            String data = Utils.readFile(act).toString();
            if(tags != null){
                for (TagToReplace tag : tags) {
                    // TODO patterns recursivos?
                    data = replacePatternByElement(data,tag.tag,tag.content);
                }
            }
            Utils.saveFile(new File(destFolder.getAbsoluteFile() + treeFromSrc), act.getName(), data);
        }
    }

    public static void compileHypertextContent(File srcFolder, File destFolder, List<TagToReplace> tags){
        compileHypertextContent(srcFolder,destFolder,"",tags);
    }

    public static class TagToReplace {
        String tag; String content;
        public TagToReplace(String tag, String content) {
            this.tag = tag;
            this.content = content;
        }
    }

    public static List<TagToReplace> defaultTags() {
        return new ArrayList<>();
    }
}
