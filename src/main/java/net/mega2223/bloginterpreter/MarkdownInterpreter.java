package net.mega2223.bloginterpreter;

import java.io.File;

public class MarkdownInterpreter {
    private MarkdownInterpreter(){}

    public static String mdToHTML(String data){
        StringBuilder html = new StringBuilder();
        String[] lines = data.split("\n");
        for (String line : lines) {
            html.append(lineToHTML(line)).append("\n");
        }
        String htmlData = html.toString();
        String htmlText = Utils.readFile(Utils.getFile("TEMPLATE.html")).toString();
        htmlText = Utils.solveReplace(htmlText,"body",htmlData);
        return htmlText;
    }

    static String lineToHTML(String line){
        StringBuilder b = new StringBuilder();

        line = line.strip();
        if(line.isEmpty()){return line;}

        int header = 0;
        for (int i = 0; line.charAt(i) == '#'; i++) {
            header++;
        }
        line = line.substring(header).strip();

        String[] delimiters = new String[2];
        if(header == 0){
            delimiters[0] = "<p>";
            delimiters[1] = "</p>";
        } else {
            delimiters[0] = "<h" + header + ">";
            delimiters[1] = "</h" + header + ">";
        }

        b.insert(0,delimiters[0]);
        b.append(line);
        b.append(delimiters[1]);
        System.out.println(b);
        return b.toString();
    }
}
