package pl.geek.tewu.celeste_extractor;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;


public class ArgsParser {
    public static final String DEFAULT_OUTPUT_DIR_NAME = "CelesteGraphics";

    public static Args parse(String[] args) {
        // Handle invalid arguments count and "--version" and "--help" cases
        if (args.length == 0) {
            Path pwdPath = Paths.get("").toAbsolutePath();
            if (ask("Do you want to convert all .data files in '" + pwdPath + "' directory and its subdirectories to .png files? (yes/no): ")) {
                args = new String[]{pwdPath.toString()};
            } else {
                printUsage();
                System.exit(1);
            }
        }
        if (args[0].equals("--help")) {
            printUsage();
            System.exit(0);
        } else if (args[0].equals("--version")) {
            printBasicAppInfo();
            System.exit(0);
        } else if (args[0].startsWith("--")) {
            printUsage();
            System.exit(1);
        } else if (args.length >= 3) {
            printUsage();
            System.exit(1);
        }

        // Parse and validate the first argument - source file/dir
        File sourceFile = new File(args[0]).getAbsoluteFile();
        if (!sourceFile.exists()) {
            System.out.println("ERROR: File or directory '" + args[0] + "' doesn't exist");
            System.exit(2);
        }

        // Get or generate value for the second argument - target file/dir
        String targetFilePath;
        if (args.length >= 2)
            targetFilePath = args[1];
        else if (sourceFile.isDirectory())
            targetFilePath = new File(DEFAULT_OUTPUT_DIR_NAME).getAbsolutePath();
        else
            targetFilePath = sourceFile.getAbsolutePath() + ".png";

        // Parse and validate the second argument - target file/dir
        File targetFile = new File(targetFilePath).getAbsoluteFile();
        if (sourceFile.isDirectory()) {
            if (!targetFile.exists() && !targetFile.mkdirs()) {
                System.out.println("ERROR: Unable to create directory '" + targetFile.getAbsolutePath() + "'");
                System.exit(3);
            }
        } else {
            if (targetFile.exists() && targetFile.isDirectory()) {
                System.out.println("ERROR: '" + targetFile.getAbsolutePath() + "' should be a file, or '" + sourceFile.getAbsolutePath() + "' should be a directory");
                printUsage();
                System.exit(5);
            }
            File targetFileParent = targetFile.getParentFile();
            if (!targetFileParent.exists() && !targetFileParent.mkdirs()) {
                System.out.println("ERROR: Unable to create directory '" + targetFileParent.getAbsolutePath() + "'");
                System.exit(4);
            }
        }

        return new Args(sourceFile.toPath().toAbsolutePath(), targetFile.toPath().toAbsolutePath());
    }

    private static boolean ask(String message) {
        Scanner in = new Scanner(System.in);
        System.out.println(message);
        return in.nextLine().trim().toLowerCase().equals("yes");
    }

    private static void printUsage() {
        printBasicAppInfo();
        System.out.println(
                "\nUsage:\n" +
                        "    " + AppInfo.COMMAND + " [OPTION] [INPUT_DIR] [OUTPUT_DIR]\n" +
                        "            Converts all .data files in INPUT_DIR and its subdirectories and puts resulting .png\n" +
                        "            files in a matching directory structure rooted at OUTPUT_DIR. Both INPUT_DIR and\n" +
                        "            OUTPUT_DIR arguments are optional. If not specified, OUTPUT_DIR defaults to \n" +
                        "            '" + DEFAULT_OUTPUT_DIR_NAME + "', and INPUT_DIR defaults to current working directory.\n" +
                        "            If both INPUT_DIR and OUTPUT_DIR are not specified, program prompts for confirmation\n" +
                        "            before starting the conversion.\n" +
                        "\n" +
                        "    " + AppInfo.COMMAND + " [OPTION] INPUT_FILE [OUTPUT_FILE]\n" +
                        "            Converts INPUT_FILE (.data file) to OUTPUT_FILE (.png file).\n" +
                        "            OUTPUT_FILE argument is optional, and when not specified defaults to the value\n" +
                        "            of INPUT_FILE argument with '.png' appended at the end.\n" +
                        "\n" +
                        "\n" +
                        "Options:\n" +
                        "    --help      Print this help message\n" +
                        "    --version   Print basic information about this program\n" +
                        "\n" +
                        "\n" +
                        "Example:\n" +
                        "    " + AppInfo.COMMAND + " \"C:\\Program Files (x86)\\Steam\\steamapps\\common\\Celeste\\Content\\Graphics\\Atlases\"\n"
        );
    }

    private static void printBasicAppInfo() {
        System.out.println(
                AppInfo.NAME + "  v" + AppInfo.VERSION + "\n" +
                        AppInfo.SHORT_DESCRIPTION
        );
    }
}