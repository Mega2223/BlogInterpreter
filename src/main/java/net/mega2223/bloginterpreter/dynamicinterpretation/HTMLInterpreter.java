package net.mega2223.bloginterpreter.dynamicinterpretation;

public class HTMLInterpreter {
    public static final String DELIMITER_B = "::";
    public static final String DELIMITER_E = "::";

    private HTMLInterpreter(){}

    // TODO auto ident

    public static String produceHTMLTag(String tagName, String data){
        return produceHTMLTag(tagName,null,true,data);
    }

    public static String produceHTMLTag(String tagName, String[][] properties, String data){
        return produceHTMLTag(tagName,properties,true,data);
    }

    public static String produceHTMLTag(String tagName, String[][] properties, boolean closeTag){
        return produceHTMLTag(tagName,properties,closeTag,"");
    }

    public static String produceHTMLTag(String tagName, String[][] properties, boolean closeTag, String data){
        StringBuilder b = new StringBuilder();
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
            b.append(data).append("\n");
            b.append("</").append(tagName).append(">");
        }
        return b.toString();
    }

    public static String solveReplace(String dat, String tag, String sub){
        if (sub == null){return dat;}
        return dat.replace(DELIMITER_B+tag+DELIMITER_E,sub);
    }

    public static String produceHyperlink(String text, String link){
        return HTMLInterpreter.produceHTMLTag(
                "a",
                new String[][]{{"href",link}},
                text
        );
    }

    public static String produceImageEmbed(String imagePath, String alt){
        return HTMLInterpreter.produceHTMLTag(
                "img",
                new String[][]{
                        {"src",imagePath},
                        {"alt",alt}
                },
                false
        );
    }
}
