package pl.geek.tewu.celeste_extractor;

import java.io.File;

public class Args {
    public File sourceFile;
    public File targetFile;

    public Args() {
        this(null, null);
    }

    public Args(File sourceFile, File targetFile) {
        this.sourceFile = sourceFile;
        this.targetFile = targetFile;
    }

    @Override
    public String toString() {
        return "Args{" +
                "sourceFile='" + sourceFile + '\'' +
                ", targetFile='" + targetFile + '\'' +
                ", isDir=" + sourceFile.isDirectory() +
                '}';
    }
}
