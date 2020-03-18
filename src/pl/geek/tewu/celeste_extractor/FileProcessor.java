package pl.geek.tewu.celeste_extractor;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.stream.Collectors;


public class FileProcessor {
    public static final String DATA_FILES_GLOB_PATTERN = "**/*.data";

    private long filesCount = 1;
    private AtomicLong processedFilesCount = new AtomicLong();
    

    public boolean processDir(Path sourceDirPath, Path targetDirPath) throws IOException {
        filesCount = countFiles(sourceDirPath, DATA_FILES_GLOB_PATTERN);
        processedFilesCount.set(0);
        walkDirParallel(sourceDirPath, DATA_FILES_GLOB_PATTERN, sourceFilePath -> {
            Path targetFilePath = rerootPath(sourceDirPath, targetDirPath, sourceFilePath, sourceFilePath.getFileName().toString() + ".png");
            if (processFile(sourceFilePath, targetFilePath))
                processedFilesCount.incrementAndGet();
        });
        return processedFilesCount.get() == filesCount;
    }

    public boolean processFile(Path sourceFilePath, Path targetFilePath) {
        if (targetFilePath.toFile().exists()) {
            // If output file already exists, skip conversion
            System.out.println("Skipping conversion of '" + sourceFilePath + "' file because output file '" + targetFilePath + "' already exists");
            return true;
        }
        File targetFileParent = targetFilePath.getParent().toFile();
        if (!targetFileParent.exists())
            synchronized (this) { // We don't want to create the same dirs from multiple threads concurrently
                if (!targetFileParent.exists() && !targetFileParent.mkdirs()) {
                    // If can't create output file's parent dirs, report failure
                    System.out.println("ERROR: Can't create '" + targetFilePath + "' file parent directories");
                    return false;
                }
            }
        if (filesCount > 1) {
            long fileNum = processedFilesCount.get() + 1;
            System.out.print(fileNum + "/" + filesCount + " (" + fileNum * 100 / filesCount + "%) | ");
        }
        return DataFileConverter.convert(sourceFilePath, targetFilePath);
    }


    private long countFiles(Path dirPath, String globPattern) throws IOException {
        final PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:" + globPattern);
        return Files.walk(dirPath)
                .filter(pathMatcher::matches)
                .count();
    }

    private void walkDirParallel(Path dirPath, String globPattern, Consumer<Path> action) throws IOException {
        final PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:" + globPattern);
        Files.walk(dirPath)
                .filter(pathMatcher::matches)
                .collect(Collectors.toList())
                .parallelStream()
                .forEach(action);
    }

    private Path rerootPath(Path sourceRoot, Path targetRoot, Path sourceFile, String targetFileName) {
        return targetRoot.resolve(sourceRoot.relativize(sourceFile)).resolveSibling(targetFileName);
    }
}