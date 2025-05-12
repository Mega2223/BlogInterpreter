package net.mega2223.bloginterpreter;

public class HTMLInterpreter {
    private HTMLInterpreter(){}

    public static String produceHTMLTag(String name, String[][] properties, boolean closeTag, String data){
        StringBuilder b = new StringBuilder();
        b.append("<").append(name).append(" ");
        if(properties != null){
            for (String[] property : properties) {
                b.append(property[0]).append(" = ").append(property[1]).append(" ");
            }
        }
        b.append(">\n");
        b.append(data).append("\n");
        if(closeTag){
            b.append("</").append(name).append(">");
        }
        return b.toString();
    }
}
