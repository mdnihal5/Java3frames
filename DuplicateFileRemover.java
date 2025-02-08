import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class DuplicateFileRemover {
    private static final String BACKUP_FOLDER = "backup_duplicates";

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java DuplicateFileRemover <folder_path>");
            return;
        }

        String folderPath = args[0];
        File directory = new File(folderPath);
        if (!directory.isDirectory()) {
            System.out.println("Invalid folder path.");
            return;
        }

        File backupDir = new File(directory, BACKUP_FOLDER);
        if (!backupDir.exists()) backupDir.mkdir();

        removeDuplicates(directory, backupDir);
        System.out.println("Duplicate removal process completed.");
    }

    private static void removeDuplicates(File directory, File backupDir) {
        Map<String, File> uniqueFiles = new HashMap<>();
        File[] files = directory.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isFile()) {
                try {
                    String hash = getFileHash(file);
                    if (uniqueFiles.containsKey(hash)) {
                        moveToBackup(file, backupDir);
                    } else {
                        uniqueFiles.put(hash, file);
                    }
                } catch (IOException | NoSuchAlgorithmException e) {
                    System.out.println("Error processing file: " + file.getName());
                }
            }
        }
    }

    private static String getFileHash(File file) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (InputStream fis = new FileInputStream(file);
             DigestInputStream dis = new DigestInputStream(fis, digest)) {
            while (dis.read() != -1); // Read entire file
        }
        byte[] hashBytes = digest.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }

    private static void moveToBackup(File file, File backupDir) {
        File backupFile = new File(backupDir, file.getName());
        int count = 1;
        while (backupFile.exists()) {
            backupFile = new File(backupDir, file.getName() + "_" + count);
            count++;
        }
        file.renameTo(backupFile);
        System.out.println("Moved duplicate: " + file.getName() + " -> " + backupFile.getName());
    }
}
