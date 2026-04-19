package net.mega2223.bloginterpreter.specialcases;

import net.mega2223.bloginterpreter.BlogInterpreter;
import net.mega2223.bloginterpreter.dynamicinterpretation.HTMLInterpreter;
import net.mega2223.bloginterpreter.util.BlogEntry;
import net.mega2223.bloginterpreter.util.Utils;

import java.io.File;

public class IndexBuilder {
    private IndexBuilder(){}

    public static void compileIndexPage(){
//        Main.entries.sort(Comparator.comparing(BlogEntry::getPublicationDate));
        BlogInterpreter.ENTRIES.sort((o1, o2) -> o2.getPublicationDate().compareTo(o1.getPublicationDate()));

        String body = Utils.readFile(
                new File(BlogInterpreter.PROPERTIES.getProperty("src") + BlogInterpreter.FILE_SEPARATOR + "dynamic"+BlogInterpreter.FILE_SEPARATOR+"blog index.html")
        ).toString();

        String listComponent = Utils.readFile(
                new File(BlogInterpreter.PROPERTIES.getProperty("src") + BlogInterpreter.FILE_SEPARATOR+  "dynamic"+BlogInterpreter.FILE_SEPARATOR+"blog entry.html")
        ).toString();

        StringBuilder components = new StringBuilder();
        for(BlogEntry entry : BlogInterpreter.ENTRIES){
            String act = listComponent;
            act = HTMLInterpreter.replacePatternByElement(act,"entry_date",entry.getPublicationDate().toString());
            act = HTMLInterpreter.replacePatternByElement(act,"entry_title", entry.getTitle());
            act = HTMLInterpreter.replacePatternByElement(act,"entry_description", entry.getDescription());
            act = HTMLInterpreter.replacePatternByElement(act,"entry_link", "."+entry.getURL());
            act = HTMLInterpreter.replacePatternByElement(act,"entry_image_src", entry.getThumbnailPath());
            //TODO THUMBNAIL RAAAARHG!!!!!
            components.append(act).append("\n");
        }

        body = HTMLInterpreter.replacePatternByElement(body,"blog_list",components.toString());

        Utils.saveFile(
                new File(BlogInterpreter.PROPERTIES.getProperty("dest")),
                "index.html",
                body
        );
    }
}
