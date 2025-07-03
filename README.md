# Directory Size Calculator

A Java command-line application for simulating file system navigation and calculating directory sizes recursively.

PPT Link: https://docs.google.com/presentation/d/11wlY9I1B3B7DGZEjILol8sBhacrGlIm8zrehgv6ramE/edit?usp=sharing

## Overview

This project implements a hierarchical file system in memory with basic shell-like commands. I built it to demonstrate object-oriented design principles and recursive algorithms.

**Key Features:**
- Directory navigation (`cd`, `ls`, `pwd`)
- Recursive size calculation for directories
- Human-readable size formatting
- Interactive command-line interface
- Built-in test data for immediate testing

## Architecture

I used the **Composite Pattern** to represent the file system structure:

```
FileSystemNode (abstract)
├── File (leaf nodes)
└── Directory (composite nodes)
```

This design makes sense because directories can contain both files and other directories, and both need to calculate their size differently:
- Files return their stored size
- Directories recursively sum up all their children's sizes

### Project Structure
```
directory-size-calculator/
├── src/
│   └── DirectorySizeCalculator.java    # Main application file
│   └── TestRunner.java                 # Main application file
├── README.md                           # This documentation
├── .gitignore                         # Git ignore rules
└── .idea/                             # IntelliJ IDEA project files (ignored)
```

### Core Components

| File/Class | Purpose | Key Methods |
|------------|---------|-------------|
| `DirectorySizeCalculator.java` | Main application class | `run()`, `main()` |
| `FileSystemNode` | Abstract base class | `getSize()`, `isDirectory()` |
| `File` | Represents individual files | `getSize()` returns stored size |
| `Directory` | Represents folders | `getSize()` recursive calculation |

### Key Methods Breakdown

| Method | Class | Purpose |
|--------|--------|---------|
| `getSize()` | `File` | Returns file size directly |
| `getSize()` | `Directory` | **Recursive** - sums all children sizes |
| `handleCd()` | `DirectorySizeCalculator` | Directory navigation logic |
| `handleLs()` | `DirectorySizeCalculator` | List directory contents |
| `handleSize()` | `DirectorySizeCalculator` | Calculate and display total size |
| `getCurrentPath()` | `DirectorySizeCalculator` | Build current path string |
## Core Implementation

### Recursive Size Calculation
```java
public long getSize() {
    long totalsize = 0;
    for (FileSystemNode child : children.values()) {
        totalsize += child.getSize();  // Recursion happens here
    }
    return totalsize;
}
```

The algorithm traverses the entire directory tree, with O(n) time complexity where n is the total number of files and directories.

### Directory Navigation
I store each directory's children in a HashMap for O(1) lookup performance when navigating with `cd`. Each node also maintains a reference to its parent for upward navigation.

## How to Run

**Prerequisites:** Java 8+

### Command Line Compilation

```bash
# Navigate to project directory
cd directory-size-calculator

# Compile the Java file
javac src/DirectorySizeCalculator.java
javac src/TestRunner.java

# Run the application
java -cp src DirectorySizeCalculator
```
# Run tests
java -cp src TestRunner
## Test Data

The application starts with this directory structure:

```
/
├── documents/
│   ├── resume.pdf (1 MB)
│   ├── cover_letter.doc (512 KB)
│   └── projects/
│       ├── project1.zip (5 MB)
│       └── project2.tar (3 MB)
├── pictures/
│   ├── vacation.jpg (2 MB)
│   └── family.png (1.5 MB)
├── music/
│   ├── song1.mp3 (4 MB)
│   └── song2.mp3 (3.5 MB)
└── empty/
    (empty directory for testing)
```

## Sample Usage
The application initializes with the following test data:

```
/ $ ls
documents/           <DIR>
pictures/            <DIR>
music/              <DIR>
empty/              <DIR>

/ $ cd documents
/documents $ size
Total size of /documents: 9.28 MB (9728000 bytes)

/documents $ cd projects
/documents/projects $ ls
project1.zip            5120000 bytes
project2.tar            3072000 bytes

/documents/projects $ pwd
/documents/projects
```

## Testing

I've included automated tests that verify:
- File and directory size calculations
- Navigation logic and edge cases
- Recursive algorithm correctness
- Empty directory handling

I've manually tested all these use cases. Additionally, the test suite uses a simple framework I built (no external dependencies) and covers the core functionality with known test data.

## Commands

| Command | Description |
|---------|-------------|
| `cd <dir>` | Change directory |
| `cd ..` | Go to parent directory |
| `cd /` | Go to root |
| `ls` | List directory contents |
| `size` | Calculate total directory size |
| `pwd` | Print working directory |
| `help` | Show available commands |
| `exit` | Exit application |

## Design Decisions

**Why Composite Pattern?** It naturally represents the file system hierarchy and allows treating files and directories uniformly while having different size calculation behavior.

**Why HashMap for children?** O(1) lookup performance for directory navigation, which is important for the `cd` command.

**Why recursive size calculation?** It's the most intuitive approach for calculating directory sizes, and the tree structure makes recursion natural.

**In-memory storage:** Keeps the implementation simple and focused on the core algorithms rather than persistence concerns.

## Complexity Analysis

- **Space:** O(n) for storing n files/directories
- **Time:** O(n) for size calculation, O(1) for navigation
- **Recursion depth:** O(h) where h is the maximum directory depth

## Potential Improvements

If I were to extend this project:
- Add file search functionality
- Implement save/load to persist directory structures
- Add more Unix-like commands (mkdir, rm, cp)
- Include file timestamps and permissions

## Technical Notes

The recursive size calculation is the core algorithm - it demonstrates depth-first tree traversal and shows how the Composite pattern enables uniform treatment of leaf and composite nodes. The parent references allow for efficient upward navigation without storing full paths.

Built with standard Java libraries only, no external dependencies required.
