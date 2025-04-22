package com.simplejava;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class FileSyncTest {
    @TempDir
    Path tempDir;

    private Path sourceDir;
    private Path targetDir;

    @BeforeEach
    void setUp() throws IOException {
        // Create source and target directories
        sourceDir = tempDir.resolve("source");
        targetDir = tempDir.resolve("target");
        Files.createDirectories(sourceDir);
        Files.createDirectories(targetDir);
    }

    @Test
    void testSyncSingleFile() throws IOException {
        // Create a file in source directory
        Path sourceFile = sourceDir.resolve("test.txt");
        Files.writeString(sourceFile, "Hello, World!");

        // Sync directories
        FileSync fileSync = new FileSync(sourceDir.toString(), targetDir.toString());
        fileSync.sync();

        // Verify target file exists and has same content
        Path targetFile = targetDir.resolve("test.txt");
        assertTrue(Files.exists(targetFile));
        assertEquals("Hello, World!", Files.readString(targetFile));
    }

    @Test
    void testSyncDoesNotOverwriteNewerFile() throws IOException {
        // Create a file in source directory
        Path sourceFile = sourceDir.resolve("test.txt");
        Files.writeString(sourceFile, "Source content");
        try {
            Thread.sleep(1000); // Ensure time difference
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
            fail("Test interrupted unexpectedly: " + e.getMessage());
        }

        // Create a newer file in target directory
        Path targetFile = targetDir.resolve("test.txt");
        Files.writeString(targetFile, "Target content");

        // Sync directories
        FileSync fileSync = new FileSync(sourceDir.toString(), targetDir.toString());
        fileSync.sync();

        // Verify target file is unchanged (newer file should not be overwritten)
        assertEquals("Target content", Files.readString(targetFile));
    }

    @Test
    void testSyncThrowsExceptionForNonExistentSource() {
        // Non-existent source directory
        FileSync fileSync = new FileSync("non/existent/source", targetDir.toString());
        IOException exception = assertThrows(IOException.class, fileSync::sync);
        assertTrue(exception.getMessage().contains("Source directory does not exist"));
    }
}