package net.mega2223.bloginterpreter.util;

import java.util.Date;
import java.util.List;

@SuppressWarnings("unused")

public class BlogEntry {
    String title;
    String description;
    String sourceFile;
    Date publicationDate;
    List<Date> updateHistory; // como eu implemento isso?
    //seria legal saber quais entries linkam outras entries
    String[][] authors; // Autores e o respectivo link de redirecionamento (opcional)
    int wordcount;
    String thumbnail;
    boolean showAtIndexPage;
    private String link;

    // para fazer a interpretação da descrição e dos autores seria legal só interpretar tudo como um markdown
    // é complicado eu fazer isso para os posts que já são html todavia
    // e eu não fiz o leitor de hiperlinks ainda então meh

    public BlogEntry(String title, String description, String sourceFile, Date publicationDate, List<Date> updateHistory, String[][] authors, int wordcount, String thumbnail, boolean showAtIndexPage, String link) {
        this.title = title;
        this.description = description;
        this.sourceFile = sourceFile;
        this.publicationDate = publicationDate;
        this.updateHistory = updateHistory;
        this.authors = authors;
        this.wordcount = wordcount;
        this.thumbnail = thumbnail;
        this.showAtIndexPage = showAtIndexPage;
        this.link = link;
    }

    @Override
    public String toString() {
        return "BlogEntry\nTitle: " +title + "\nDescription: " + description + "\nThumbnail: " + thumbnail +
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

    public String getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
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

    public String[][] getAuthors() {
        return authors;
    }

    public void setAuthors(String[][] authors) {
        this.authors = authors;
    }

    public int getWordcount() {
        return wordcount;
    }

    public void setWordcount(int wordcount) {
        this.wordcount = wordcount;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public boolean isShowAtIndexPage() {
        return showAtIndexPage;
    }

    public void setShowAtIndexPage(boolean showAtIndexPage) {
        this.showAtIndexPage = showAtIndexPage;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link){
        this.link = link;
    }
}
