package com.simplejava;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class FileSync {
    private final Path sourceDir;
    private final Path targetDir;

    public FileSync(String sourceDir, String targetDir) {
        this.sourceDir = Paths.get(sourceDir);
        this.targetDir = Paths.get(targetDir);
    }

    public void sync() throws IOException {
        // Ensure directories exist
        if (!Files.exists(sourceDir)) {
            throw new IOException("Source directory does not exist: " + sourceDir);
        }
        if (!Files.exists(targetDir)) {
            Files.createDirectories(targetDir);
        }

        // Walk through source directory
        Files.walkFileTree(sourceDir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path sourceFile, BasicFileAttributes attrs) throws IOException {
                // Compute relative path and corresponding target path
                Path relativePath = sourceDir.relativize(sourceFile);
                Path targetFile = targetDir.resolve(relativePath);

                // Check if target file exists and compare last modified time
                if (Files.exists(targetFile)) {
                    long sourceModified = Files.getLastModifiedTime(sourceFile).toMillis();
                    long targetModified = Files.getLastModifiedTime(targetFile).toMillis();
                    if (sourceModified <= targetModified) {
                        // Target file is up-to-date, skip
                        return FileVisitResult.CONTINUE;
                    }
                }

                // Ensure target directory exists
                Path targetParent = targetFile.getParent();
                if (!Files.exists(targetParent)) {
                    Files.createDirectories(targetParent);
                }

                // Copy the file
                Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Synced: " + sourceFile + " -> " + targetFile);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) {
                System.err.println("Failed to access file: " + file + " (" + exc.getMessage() + ")");
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java -jar SimpleJavaFileSync.jar <sourceDir> <targetDir>");
            System.exit(1);
        }

        String sourceDir = args[0];
        String targetDir = args[1];

        try {
            FileSync fileSync = new FileSync(sourceDir, targetDir);
            fileSync.sync();
            System.out.println("File synchronization completed.");
        } catch (IOException e) {
            System.err.println("Error during synchronization: " + e.getMessage());
            System.exit(1);
        }
    }
}