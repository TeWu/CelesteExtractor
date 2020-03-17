package pl.geek.tewu.celeste_extractor;

import java.io.IOException;
import java.util.Arrays;


public class Main {
    public static void main(String[] args) throws IOException {
        Args parsedArgs = ArgsParser.parse(args);

        System.out.println(Arrays.toString(args));
        System.out.println(parsedArgs);

//        DataFileConverter converter = new DataFileConverter();
//        converter.convert("creepA00.data", "creepA00.data.png");
//        converter.convert("04.data", "04.data.png");

        System.out.println("All done");
    }
}