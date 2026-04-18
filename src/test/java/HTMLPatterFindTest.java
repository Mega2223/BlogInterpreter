import net.mega2223.bloginterpreter.dynamicinterpretation.HTMLInterpreter;

public class HTMLPatterFindTest {
    public static void main(String[] args) {
        String s = "::teste1:: ::teste2:: ::teste3::";
        s = HTMLInterpreter.replacePatternByElement(s,"teste1","CAVALO");
        System.out.println(s);
    }
}
