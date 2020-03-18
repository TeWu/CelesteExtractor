package pl.geek.tewu.celeste_extractor;

import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        Args parsedArgs = ArgsParser.parse(args);

        System.out.println("Starting processing with arguments: " + parsedArgs + "\n");

        boolean fullSuccess;
        FileProcessor processor = new FileProcessor();
        if (parsedArgs.sourcePath.toFile().isDirectory())
            fullSuccess = processor.processDir(parsedArgs.sourcePath, parsedArgs.targetPath);
        else
            fullSuccess = processor.processFile(parsedArgs.sourcePath, parsedArgs.targetPath);

        long processingTime = System.currentTimeMillis() - start;
        System.out.println("\nAll done in " + (processingTime / 1000) + " seconds - " + (fullSuccess ? " All .data files converted successfully :)" : "!! NOTE: Some .data files were not converted !! - You can try rerunning the program, also make sure you're passing correct paths to the program"));
    }

}