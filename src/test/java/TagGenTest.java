import net.mega2223.bloginterpreter.dynamicinterpretation.HTMLInterpreter;

public class TagGenTest {
    public static void main(String[] args) {
        String[][] props = {
                {"class","\"par\""},
                {"id","\"teste\""}
        };
        String x = HTMLInterpreter.generateHTMLTag("p", props, true, "Oiee");
        System.out.println(x);
        if(!x.equals("<p class = \"par\"  id = \"teste\" >\n  Oiee\n</p>")){
            throw new RuntimeException("Failed tag generation test");
        }
    }
}
