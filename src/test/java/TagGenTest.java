import net.mega2223.bloginterpreter.HTMLInterpreter;

public class TagGenTest {
    public static void main(String[] args) {
        String[][] props = {
                {"class","\"par\""},
                {"id","\"teste\""}
        };
        String x = HTMLInterpreter.produceHTMLTag("p", props, true, "Oiee");
        System.out.println(x);
        if(!x.equals("<p class = \"par\" id = \"teste\" >\nOiee\n</p>")){
            throw new RuntimeException("Failed tag generation test");
        }
    }
}
