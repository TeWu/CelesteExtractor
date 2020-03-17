package pl.geek.tewu.celeste_extractor;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;


public class Main {

    public static void main(String[] args) throws IOException {
        Args parsedArgs = ArgsParser.parse(args);
        boolean fullSuccess;

        System.out.println("Starting processing with arguments: " + parsedArgs);

        if (parsedArgs.sourcePath.toFile().isDirectory()) {
            long filesCount = countFiles(parsedArgs.sourcePath, "**/*.txt");
            long processedFilesCount = processDir(parsedArgs.sourcePath, parsedArgs.targetPath);
            fullSuccess = processedFilesCount == filesCount;
        } else {
            fullSuccess = processFile(parsedArgs.sourcePath, parsedArgs.targetPath);
        }

        System.out.println("All done - " + (fullSuccess ? " All data files converted successfully :)" : "Some data files couldn't be converted"));
    }

    public static long processDir(Path sourceDirPath, Path targetDirPath) throws IOException {
        CountingGlobFilesVisitor fileVisitor = new CountingGlobFilesVisitor("**/*.txt") { //TODO: change .txt to .data

            public boolean visitMatchedFile(Path sourceFilePath) {
                Path targetFilePath = rerootPath(sourceDirPath, targetDirPath, sourceFilePath, sourceFilePath.getFileName().toString() + ".png");
                return processFile(sourceFilePath, targetFilePath);
            }

        };
        Files.walkFileTree(sourceDirPath, fileVisitor);
        return fileVisitor.getSuccessCount();
    }

    public static boolean processFile(Path sourceFilePath, Path targetFilePath) {
        System.out.println(">>>  " + sourceFilePath);
        System.err.println(">>>  " + targetFilePath);
//        DataFileConverter.convert(sourceFilePath, targetFilePath);
        return true;
    }

    private static long countFiles(Path dirPath, String globPattern) throws IOException {
        final PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:" + globPattern);
        return Files.walk(dirPath)
                .parallel()
                .filter(pathMatcher::matches)
                .count();
    }

    private static Path rerootPath(Path sourceRoot, Path targetRoot, Path sourceFile, String targetFileName) {
        return targetRoot.resolve(sourceRoot.relativize(sourceFile)).resolveSibling(targetFileName);
    }
}