package net.mega2223.bloginterpreter.util;

public class Templates {
    private Templates(){}

    /**Template para o componente HTML de cada link das páginas dos blogs no índice*/
    public static String indexEntryTemplate = null;

    /**Modelo HTML da página do índice*/
    public static String indexPageTemplate = null;

    /**Pagina html em branco com o body e o head substituíveis*/
    public static String HTMLPageTemplate = null;

    public static void initTemplates(String src){
        indexPageTemplate = src + "\\dynamic\\blog index.html";
        indexEntryTemplate = src + "\\dynamic\\blog entry.html";
        HTMLPageTemplate = src + "\\dynamic\\TEMPLATE.html";
    }
}
