package pl.geek.tewu.celeste_extractor;

import java.io.File;


public class ArgsParser {
    public static final String DEFAULT_OUTPUT_DIR_NAME = "CelesteExtractorOutput";

    public static Args parse(String[] args) {
        Args parsedArgs = new Args();

        // Handle invalid arguments count and "--version" case
        if (args.length == 0 || args.length >= 3) {
            printUsage();
            System.exit(1);
        } else if (args[0].equals("--version")) {
            printNameAndVersion();
            System.exit(0);
        }

        // Parse and validate the first argument - source file/dir
        parsedArgs.sourceFile = new File(args[0]).getAbsoluteFile();
        if (!parsedArgs.sourceFile.exists()) {
            System.out.println("ERROR: File or directory '" + args[0] + "' doesn't exist");
            System.exit(2);
        }

        // Get or generate value for the second argument - target file/dir
        String targetFilePath;
        if (args.length >= 2)
            targetFilePath = args[1];
        else if (parsedArgs.sourceFile.isDirectory())
            targetFilePath = new File(DEFAULT_OUTPUT_DIR_NAME).getAbsolutePath();
        else
            targetFilePath = parsedArgs.sourceFile.toString() + ".png";

        // Parse and validate the second argument - target file/dir
        parsedArgs.targetFile = new File(targetFilePath).getAbsoluteFile();
        if (parsedArgs.sourceFile.isDirectory()) {
            if (!parsedArgs.targetFile.exists() && !parsedArgs.targetFile.mkdirs()) {
                System.out.println("ERROR: Unable to create directory '" + parsedArgs.targetFile.getAbsolutePath() + "'");
                System.exit(3);
            }
        } else {
            if (parsedArgs.targetFile.exists() && parsedArgs.targetFile.isDirectory()) {
                System.out.println("ERROR: '" + parsedArgs.targetFile.getAbsolutePath() + "' should be a file, or '" + parsedArgs.sourceFile.getAbsolutePath() + "' should be a directory");
                printUsage();
                System.exit(5);
            }
            File targetFileParent = parsedArgs.targetFile.getParentFile();
            if (!targetFileParent.exists() && !targetFileParent.mkdirs()) {
                System.out.println("ERROR: Unable to create directory '" + parsedArgs.targetFile.getParentFile().getAbsolutePath() + "'");
                System.exit(4);
            }
        }

        return parsedArgs;
    }


    private static void printUsage() {
        printNameAndVersion();
        System.out.println("Usage: ???"); // TODO
    }

    private static void printNameAndVersion() {
        System.out.println(AppInfo.NAME + "  v" + AppInfo.VERSION);
    }
}
