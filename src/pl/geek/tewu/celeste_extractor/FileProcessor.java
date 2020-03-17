package pl.geek.tewu.celeste_extractor;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.function.Consumer;


public class FileProcessor {
    public static final String DATA_FILES_GLOB_PATTERN = "**/*.txt"; // TODO: change .txt to .data

    private long processedFilesCount;


    public boolean processDir(Path sourceDirPath, Path targetDirPath) throws IOException {
        long filesCount = countFiles(sourceDirPath, DATA_FILES_GLOB_PATTERN);
        processedFilesCount = 0;
        walkDir(sourceDirPath, DATA_FILES_GLOB_PATTERN, sourceFilePath -> {
            Path targetFilePath = rerootPath(sourceDirPath, targetDirPath, sourceFilePath, sourceFilePath.getFileName().toString() + ".png");
            if (processFile(sourceFilePath, targetFilePath))
                processedFilesCount++;
        });
        return processedFilesCount == filesCount;
    }

    public boolean processFile(Path sourceFilePath, Path targetFilePath) {
        System.out.println(">>>  " + sourceFilePath);
        System.err.println(">>>  " + targetFilePath);
//        DataFileConverter.convert(sourceFilePath, targetFilePath);
        return true;
    }


    private long countFiles(Path dirPath, String globPattern) throws IOException {
        final PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:" + globPattern);
        return Files.walk(dirPath)
                .parallel()
                .filter(pathMatcher::matches)
                .count();
    }

    private void walkDir(Path dirPath, String globPattern, Consumer<Path> action) throws IOException {
        final PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:" + globPattern);
        Files.walk(dirPath)
                .parallel()
                .filter(pathMatcher::matches)
                .forEach(action);
    }

    private Path rerootPath(Path sourceRoot, Path targetRoot, Path sourceFile, String targetFileName) {
        return targetRoot.resolve(sourceRoot.relativize(sourceFile)).resolveSibling(targetFileName);
    }
}
