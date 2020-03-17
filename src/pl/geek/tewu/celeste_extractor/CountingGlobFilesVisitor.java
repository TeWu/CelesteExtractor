package pl.geek.tewu.celeste_extractor;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;


public abstract class CountingGlobFilesVisitor extends SimpleFileVisitor<Path> {
    private final PathMatcher pathMatcher;
    private long successCount;


    public CountingGlobFilesVisitor(String globPattern) {
        this.pathMatcher = FileSystems.getDefault().getPathMatcher("glob:" + globPattern);
        this.successCount = 0;
    }

    public long getSuccessCount() {
        return successCount;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        if (pathMatcher.matches(file) && visitMatchedFile(file))
            successCount++;
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        return FileVisitResult.CONTINUE;
    }

    public abstract boolean visitMatchedFile(Path file);
}