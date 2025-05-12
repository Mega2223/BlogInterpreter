package net.mega2223.bloginterpreter;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        File src = new File(args[0]);
        File dest = new File(args[1]);

        Utils.DEBUG_LEVEL = Utils.DEBUG_VERBOSE;

        FileManager.compile(src,dest);
    }
}