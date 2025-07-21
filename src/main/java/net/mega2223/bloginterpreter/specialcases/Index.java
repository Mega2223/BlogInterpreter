package net.mega2223.bloginterpreter.specialcases;

import net.mega2223.bloginterpreter.Main;
import net.mega2223.bloginterpreter.dynamicinterpretation.HTMLInterpreter;
import net.mega2223.bloginterpreter.util.BlogEntry;
import net.mega2223.bloginterpreter.util.Utils;

import java.io.File;

public class Index {
    private Index(){}

    public static void compileIndex(){
//        Main.entries.sort(Comparator.comparing(BlogEntry::getPublicationDate));
        Main.ENTRIES.sort((o1, o2) -> o2.getPublicationDate().compareTo(o1.getPublicationDate()));

        String body = Utils.readFile(
                new File(Main.PROPERTIES.getProperty("src") + "\\dynamic\\blog index.html")
        ).toString();

        String listComponent = Utils.readFile(
                new File(Main.PROPERTIES.getProperty("src") + "\\dynamic\\blog entry.html")
        ).toString();

        StringBuilder components = new StringBuilder();
        for(BlogEntry entry : Main.ENTRIES){
            String act = listComponent;
            act = HTMLInterpreter.solveReplace(act,"entry_date",entry.getPublicationDate().toString());
            act = HTMLInterpreter.solveReplace(act,"entry_title", entry.getTitle());
            act = HTMLInterpreter.solveReplace(act,"entry_description", entry.getDescription());
            act = HTMLInterpreter.solveReplace(act,"entry_link", "."+entry.getLink());
            act = HTMLInterpreter.solveReplace(act,"entry_image_src", entry.getThumbnail());
            //TODO THUMBNAIL RAAAARHG!!!!!
//            act = HTMLInterpreter.solveReplace(act,"date",entry.getPublicationDate().toString());
            components.append(act).append("\n");
        }

        body = HTMLInterpreter.solveReplace(body,"blog_list",components.toString());

        Utils.saveFile(
                new File(Main.PROPERTIES.getProperty("dest")),
                "index.html",
                body
        );
    }
}
