package pl.geek.tewu.celeste_extractor;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.function.Consumer;


public class FileProcessor {
    public static final String DATA_FILES_GLOB_PATTERN = "**/*.data"; // TODO: change .txt to .data

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
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        if (targetFilePath.toFile().exists()) {
            // If output file already exists, skip conversion
            System.out.println("NOTE: Output file '" + targetFilePath + "' already exists - skipping conversion of '" + sourceFilePath + "' file");
            return true;
        }
        File targetFileParentPath = targetFilePath.getParent().toFile();
        if (!targetFileParentPath.exists() && !targetFileParentPath.mkdirs()) {
            // If can't create output file's parent dirs, report failure
            System.out.println("ERROR: Can't create '" + targetFilePath + "' file parent directories");
            return false;
        }
//        System.out.println(">>>  " + sourceFilePath);
//        System.err.println(">>>  " + targetFilePath);
//        return true;
        return DataFileConverter.convert(sourceFilePath, targetFilePath);
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
