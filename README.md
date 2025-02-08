# Duplicate File Remover (CLI)  

A simple Java program to scan a folder and remove duplicate files based on content (using SHA-256 hashing). Duplicate files are moved to a backup folder inside the given directory.  

## Usage  

1. **Compile:**  
   ```sh
   javac DuplicateFileRemover.java
   ```  
2. **Run:**  
   ```sh
   java DuplicateFileRemover /path/to/folder
   ```  
   Example:  
   ```sh
   java DuplicateFileRemover /home/user/Documents
   ```  

## How It Works  
- Computes SHA-256 hash for each file.  
- Keeps the first occurrence and moves duplicates to `backup_duplicates/`.  
- Ensures backup files don’t overwrite each other.  

## Notes  
- Works for all file types.  
- Does **not** scan subdirectories.  
- Pure Java (no external dependencies).
