# CLI-interpreter

A command line interface simulator that implements popular Linux OS commands.

---

## 📋 Table of Contents

-   [Overview](#overview)
-   [Commands](#commands)
    -   [Navigation & Information](#navigation--information)
    -   [File & Directory Management](#file--directory-management)
    -   [File Operations](#file-operations)
    -   [Archive Operations](#archive-operations)
    -   [Output Redirection](#output-redirection)

---

## Overview

This project simulates a command line interface (CLI) for Linux operating system commands. All relative paths are resolved against the current working directory maintained by the shell.

---

## Commands

### Navigation & Information

#### `pwd`

Print working directory - displays the current directory path.

```bash
pwd
```

#### `cd`

Change directory - navigate between directories.

```bash
cd              # Go to user home directory
cd <path>       # Go to specified directory (absolute or relative)
cd ..           # Go to parent directory
```

**Examples:**

```bash
cd /home/user/documents
cd documents
cd ..
```

#### `ls`

List directory contents - shows files and directories in current location.

```bash
ls
```

---

### File & Directory Management

#### `mkdir`

Create new directory/directories.

```bash
mkdir <name> [name2 ...]
```

**Examples:**

```bash
mkdir myFolder
mkdir dir1 dir2 dir3
```

#### `rmdir`

Remove empty directory/directories.

```bash
rmdir <name> [name2 ...]
rmdir *                      # Remove all empty directories
```

**Examples:**

```bash
rmdir oldFolder
rmdir temp backup cache
```

#### `touch`

Create new file(s) or update timestamp of existing file(s).

```bash
touch <file> [file2 ...]
```

**Examples:**

```bash
touch newfile.txt
touch file1.txt file2.txt file3.txt
```

#### `rm`

Remove file(s).

```bash
rm <file> [file2 ...]
```

**Examples:**

```bash
rm oldfile.txt
rm temp1.txt temp2.txt
```

---

### File Operations

#### `cp`

Copy files.

```bash
cp <source-file> <destination>
```

**Behavior:**

-   Source must be a file (not a directory)
-   If destination is an existing directory, file is copied into it
-   If destination is a file path, file is copied with that name

**Examples:**

```bash
cp file.txt backup.txt           # Copy file.txt to backup.txt
cp file.txt documents/           # Copy file.txt into documents directory
```

#### `cp -r`

Copy directories recursively.

```bash
cp -r <source-directory> <destination-directory>
```

**Behavior:**

-   Copies the entire source directory (with all contents) into the destination directory
-   Creates destination directory if it doesn't exist
-   Result: `destination/source-name/` will contain all source contents

**Examples:**

```bash
cp -r project backup             # Creates backup/project/ with all contents
cp -r /home/user/data archive    # Creates archive/data/ with all contents
```

#### `cat`

Display file contents.

```bash
cat <file> [file2 ...]
```

**Examples:**

```bash
cat readme.txt
cat file1.txt file2.txt
```

#### `wc`

Word count - displays line, word, and character count.

```bash
wc <file>
```

**Example:**

```bash
wc document.txt
```

---

### Archive Operations

#### `zip`

Create zip archive.

```bash
zip <archive.zip> <file|directory> [more...]
zip -r <archive.zip> <directory> [more...]    # Recursive (for directories)
```

**Examples:**

```bash
zip backup.zip file.txt                  # Zip a single file
zip -r project.zip myProject/            # Zip entire directory
zip archive.zip file1.txt file2.txt      # Zip multiple files
```

#### `unzip`

Extract zip archive.

```bash
unzip <archive.zip>                      # Extract to current directory
unzip <archive.zip> -d <destination>     # Extract to specified directory
```

**Examples:**

```bash
unzip backup.zip
unzip archive.zip -d extracted/
```

---

### Output Redirection

⚠️ **Status:** Not currently implemented

The following redirection operators are planned but not active:

-   `>` - Redirect output to file (overwrite)
-   `>>` - Redirect output to file (append)

**Planned usage:**

```bash
ls > files.txt           # Save ls output to files.txt
cat file.txt >> log.txt  # Append file.txt content to log.txt
```

---

## 💡 Tips

-   All commands support both **absolute** and **relative** paths
-   Relative paths are resolved against the shell's current working directory
-   Use `pwd` to check your current location
-   Use `cd` to navigate before performing file operations
-   The shell maintains its own current directory independent of the JVM working directory

---

## 🐛 Known Issues

-   Output redirection (`>`, `>>`) is not yet implemented
-   Some edge cases in path resolution may need handling

---
