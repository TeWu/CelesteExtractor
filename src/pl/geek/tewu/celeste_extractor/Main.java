package pl.geek.tewu.celeste_extractor;

import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {

        DataFileConverter converter = new DataFileConverter();
        converter.convert("creepA00.data", "creepA00.data.png");
        converter.convert("04.data", "04.data.png");

        System.out.println("All done");
    }
}