package net.mega2223.bloginterpreter.util;

import net.mega2223.bloginterpreter.objects.Person;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@SuppressWarnings("unused")

public class BlogEntry {
    String title; String description; String content;
    String URL; // URL RELATIVO !!!
    Date publicationDate;
    List<Date> updateHistory; // como eu implemento isso?

    List<Person> authors;
    List<BlogEntry> internalLinks;
    List<String> externaLinks;

    int wordCount;
    String thumbnailPath;
    boolean showAtIndexPage;

    // para fazer a interpretação da descrição e dos autores seria legal só interpretar tudo como um markdown
    // é complicado eu fazer isso para os posts que já são html todavia
    // e eu não fiz o leitor de hiperlinks ainda então meh

    public BlogEntry(String title, String description, String content, String URL, Date publicationDate, String thumbnailPath, int wordCount) {
        this.title = title;
        this.description = description;
        this.content = content;
        this.publicationDate = publicationDate;
        this.URL = URL;

        this.wordCount = wordCount;
        this.thumbnailPath = thumbnailPath;

        this.showAtIndexPage = true;
        this.authors = new ArrayList<>();
        this.internalLinks = new ArrayList<>();
        this.externaLinks = new ArrayList<>();
    }

    public BlogEntry(Properties properties, String content, String URL){
        this(
                properties.getProperty("title"),
                properties.getProperty("description"),
                content,
                URL,
                Utils.stringToDate(properties.getProperty("date")),
                properties.getProperty("thumbnail"),
                0 // TODO
        );

    }

    @Override
    public String toString() {
        return "BlogEntry\nTitle: " +title + "\nDescription: " + description + "\nThumbnail: " + thumbnailPath +
                "\nDate: " + publicationDate + "\nShow at index: " + showAtIndexPage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public List<Date> getUpdateHistory() {
        return updateHistory;
    }

    public void setUpdateHistory(List<Date> updateHistory) {
        this.updateHistory = updateHistory;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public boolean isShowAtIndexPage() {
        return showAtIndexPage;
    }

    public void setShowAtIndexPage(boolean showAtIndexPage) {
        this.showAtIndexPage = showAtIndexPage;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public List<BlogEntry> getInternalLinks() {
        return internalLinks;
    }

    public void setInternalLinks(List<BlogEntry> internalLinks) {
        this.internalLinks = internalLinks;
    }

    public List<String> getExternaLinks() {
        return externaLinks;
    }

    public void setExternaLinks(List<String> externaLinks) {
        this.externaLinks = externaLinks;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }
}
