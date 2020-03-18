package pl.geek.tewu.celeste_extractor;

import java.nio.file.Path;

public class Args {
    public final Path sourcePath;
    public final Path targetPath;

    public Args(Path sourcePath, Path targetPath) {
        this.sourcePath = sourcePath;
        this.targetPath = targetPath;
    }

    @Override
    public String toString() {
        return "sourcePath='" + sourcePath + '\'' +
                ", targetPath='" + targetPath + '\'' +
                ", isSourcePathDir=" + sourcePath.toFile().isDirectory();
    }
}