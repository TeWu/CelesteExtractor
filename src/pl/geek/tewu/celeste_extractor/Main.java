package pl.geek.tewu.celeste_extractor;

import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {
        Args parsedArgs = ArgsParser.parse(args);

        System.out.println("Starting processing with arguments: " + parsedArgs);

        boolean fullSuccess;
        FileProcessor processor = new FileProcessor();
        if (parsedArgs.sourcePath.toFile().isDirectory())
            fullSuccess = processor.processDir(parsedArgs.sourcePath, parsedArgs.targetPath);
        else
            fullSuccess = processor.processFile(parsedArgs.sourcePath, parsedArgs.targetPath);

        System.out.println("All done - " + (fullSuccess ? " All data files converted successfully :)" : "!! NOTE: Some data files couldn't be converted !!"));
    }

}