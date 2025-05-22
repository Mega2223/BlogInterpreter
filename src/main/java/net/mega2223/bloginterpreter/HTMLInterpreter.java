package net.mega2223.bloginterpreter;

public class HTMLInterpreter {
    private HTMLInterpreter(){}

    // TODO auto ident

    public static String produceHTMLTag(String name, String data){
        return produceHTMLTag(name,null,true,data);
    }

    public static String produceHTMLTag(String name, String[][] properties, String data){
        return produceHTMLTag(name,properties,true,data);
    }

    public static String produceHTMLTag(String name, String[][] properties, boolean closeTag, String data){
        StringBuilder b = new StringBuilder();
        b.append("<").append(name);
        if(properties != null){
            for (String[] property : properties) {
                if(!(property[1].startsWith("\"") && property[1].endsWith("\""))){
                    property[1] = "\"" + property[1] + "\"";
                }
                b.append(" ").append(property[0]).append(" = ").append(property[1]).append(" ");
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
