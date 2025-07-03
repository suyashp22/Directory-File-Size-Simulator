import java.util.*;

// Abstract base class for file system nodes
abstract class FileSystemNode {
    protected String name;
    protected Directory parent;

    public FileSystemNode(String name, Directory parent) {
        this.name = name;
        this.parent = parent;
    }

    public String getName() { return name; }
    public Directory getParent() { return parent; }
    public abstract long getSize();
    public abstract boolean isDirectory();
}

// File class representing individual files
class File extends FileSystemNode {
    private long size;

    public File(String name, long size, Directory parent) {
        super(name, parent);
        this.size = size;
    }

    @Override
    public long getSize() { return size; }

    @Override
    public boolean isDirectory() { return false; }

    @Override
    public String toString() {
        return String.format("%-20s %10d bytes", name, size);
    }
}

// Directory class representing folders
class Directory extends FileSystemNode {
    private Map<String, FileSystemNode> children;

    public Directory(String name, Directory parent) {
        super(name, parent);
        this.children = new HashMap<>();
    }

    public void addChild(FileSystemNode node) {
        children.put(node.getName(), node);
    }

    public FileSystemNode getChild(String name) {
        return children.get(name);
    }

    public Collection<FileSystemNode> getChildren() {
        return children.values();
    }

    public boolean hasChild(String name) {
        return children.containsKey(name);
    }

    @Override
    public long getSize() {
        // Recursive calculation of directory size
        long totalsize = 0;
        for (FileSystemNode child : children.values()) {
            totalsize += child.getSize();
        }
        return totalsize;
    }

    @Override
    public boolean isDirectory() { return true; }

    @Override
    public String toString() {
        return String.format("%-20s %10s", name + "/", "<DIR>");
    }
}

// Main file system class
public class DirectorySizeCalculator {
    private Directory root;
    private Directory currentDirectory;
    private Scanner scanner;

    public DirectorySizeCalculator() {
        this.root = new Directory("root", null);
        this.currentDirectory = root;
        this.scanner = new Scanner(System.in);
        initializeTestData();
    }

    // Initialize with some test data
    private void initializeTestData() {
        // Create some directories and files
        Directory documents = new Directory("documents", root);
        Directory pictures = new Directory("pictures", root);
        Directory music = new Directory("music", root);
        Directory empty = new Directory("empty", root);
        root.addChild(documents);
        root.addChild(pictures);
        root.addChild(music);
        root.addChild(empty);

        // Add files to documents
        documents.addChild(new File("resume.pdf", 1024000, documents));
        documents.addChild(new File("cover_letter.doc", 512000, documents));

        // Add subdirectory to documents
        Directory projects = new Directory("projects", documents);
        documents.addChild(projects);
        projects.addChild(new File("project1.zip", 5120000, projects));
        projects.addChild(new File("project2.tar", 3072000, projects));

        // Add files to pictures
        pictures.addChild(new File("vacation.jpg", 2048000, pictures));
        pictures.addChild(new File("family.png", 1536000, pictures));

        // Add files to music
        music.addChild(new File("song1.mp3", 4096000, music));
        music.addChild(new File("song2.mp3", 3584000, music));

    }

    public void run() {
        System.out.println("Directory Size Calculator");
        System.out.println("Available commands: cd <directory>, ls, size, pwd, exit");
        System.out.println("Type 'help' for more info\n");

        while (true) {
            System.out.print(getCurrentPath() + " $ ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) continue;

            String[] parts = input.split("\\s+");
            String command = parts[0].toLowerCase();

            switch (command) {
                case "cd":
                    handleCd(parts);
                    break;
                case "ls":
                    handleLs();
                    break;
                case "size":
                    handleSize();
                    break;
                case "pwd":
                    System.out.println(getCurrentPath());
                    break;
                case "help":
                    showHelp();
                    break;
                case "exit":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Unknown command: " + command);
                    System.out.println("Type 'help' for available commands");
            }
        }
    }

    private void handleCd(String[] parts) {
        if (parts.length < 2) {
            System.out.println("Usage: cd <directory>");
            return;
        }

        String target = parts[1];

        if (target.equals("..")) {
            if (currentDirectory.getParent() != null) {
                currentDirectory = currentDirectory.getParent();
            } else {
                System.out.println("Already at root directory");
            }
        } else if (target.equals("/") || target.equals("root")) {
            currentDirectory = root;
        } else {
            FileSystemNode node = currentDirectory.getChild(target);
            if (node == null) {
                System.out.println("Directory not found: " + target);
            } else if (!node.isDirectory()) {
                System.out.println(target + " is not a directory");
            } else {
                currentDirectory = (Directory) node;
            }
        }
    }

    private void handleLs() {
        Collection<FileSystemNode> children = currentDirectory.getChildren();

        if (children.isEmpty()) {
            System.out.println("Directory is empty");
            return;
        }

        System.out.println("\nContents of " + getCurrentPath() + ":");
        System.out.println("Name                 Size");
        System.out.println("--------------------+----------");

        for (FileSystemNode node : children) {
            System.out.println(node.toString());
        }
        System.out.println();
    }

    private void handleSize() {
        long totalSize = currentDirectory.getSize();
        System.out.println("Total size of " + getCurrentPath() + ": " +
                formatSize(totalSize) + " (" + totalSize + " bytes)");
    }

    private String getCurrentPath() {
        if (currentDirectory == root) {
            return "/";
        }

        List<String> pathParts = new ArrayList<>();
        Directory current = currentDirectory;

        while (current != null && current != root) {
            pathParts.add(current.getName());
            current = current.getParent();
        }

        Collections.reverse(pathParts);
        return "/" + String.join("/", pathParts);
    }

    private String formatSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.2f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.2f MB", bytes / (1024.0 * 1024));
        return String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024));
    }

    private void showHelp() {
        System.out.println("\nAvailable commands:");
        System.out.println("  cd <directory>  - Change to specified directory");
        System.out.println("  cd ..          - Go to parent directory");
        System.out.println("  cd /           - Go to root directory");
        System.out.println("  ls             - List contents of current directory");
        System.out.println("  size           - Calculate total size of current directory");
        System.out.println("  pwd            - Print current working directory");
        System.out.println("  help           - Show this help message");
        System.out.println("  exit           - Exit the application");
        System.out.println();
    }

    public static void main(String[] args) {
        new DirectorySizeCalculator().run();
    }
}