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
        indexPageTemplate = indexPageTemplate == null ? src + "\\dynamic\\blog index.html" : indexPageTemplate;
        indexEntryTemplate = indexEntryTemplate == null ? src + "\\dynamic\\blog entry.html" : indexEntryTemplate;
        HTMLPageTemplate = HTMLPageTemplate == null ? src + "\\dynamic\\TEMPLATE.html" : HTMLPageTemplate;
    }
}
