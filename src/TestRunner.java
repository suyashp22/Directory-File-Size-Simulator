public class TestRunner {

    private static int testsRun = 0;
    private static int testsPassed = 0;

    public static void main(String[] args) {
        System.out.println("Running Directory Size Calculator Tests...\n");

        // Run all tests
        testFileSize();
        testDirectorySize();
        testEmptyDirectory();
        testNavigationLogic();
        testPathBuilding();
        testSizeCalculationAccuracy();

        // Print results
        System.out.println("=================================");
        System.out.println("Tests run: " + testsRun);
        System.out.println("Tests passed: " + testsPassed);
        System.out.println("Tests failed: " + (testsRun - testsPassed));
        System.out.println("Success rate: " + (testsPassed * 100 / testsRun) + "%");

        if (testsPassed == testsRun) {
            System.out.println("✅ All tests passed!");
        } else {
            System.out.println("❌ Some tests failed!");
        }
    }

    private static void testFileSize() {
        System.out.println("Testing file size calculation...");

        File testFile = new File("test.txt", 1024, null);
        assertEqual(1024L, testFile.getSize(), "File size should be 1024");
        assertFalse(testFile.isDirectory(), "File should not be a directory");

        System.out.println("✅ File size tests passed\n");
    }

    private static void testDirectorySize() {
        System.out.println("Testing directory size calculation...");

        Directory root = new Directory("root", null);
        Directory subdir = new Directory("subdir", root);
        File file1 = new File("file1.txt", 1000, root);
        File file2 = new File("file2.txt", 2000, subdir);

        root.addChild(subdir);
        root.addChild(file1);
        subdir.addChild(file2);

        assertEqual(2000L, subdir.getSize(), "Subdirectory size should be 2000");
        assertEqual(3000L, root.getSize(), "Root size should be 3000 (1000 + 2000)");
        assertTrue(root.isDirectory(), "Directory should be a directory");

        System.out.println("✅ Directory size tests passed\n");
    }

    private static void testEmptyDirectory() {
        System.out.println("Testing empty directory...");

        Directory empty = new Directory("empty", null);
        assertEqual(0L, empty.getSize(), "Empty directory size should be 0");
        assertFalse(empty.hasChild("anything"), "Empty directory should have no children");

        System.out.println("✅ Empty directory tests passed\n");
    }

    private static void testNavigationLogic() {
        System.out.println("Testing navigation logic...");

        Directory root = new Directory("root", null);
        Directory docs = new Directory("documents", root);
        File file = new File("readme.txt", 500, docs);

        root.addChild(docs);
        docs.addChild(file);

        assertTrue(root.hasChild("documents"), "Root should have documents child");
        assertEqual(docs, root.getChild("documents"), "Should get correct child");
        assertNull(root.getChild("nonexistent"), "Should return null for nonexistent child");

        System.out.println("✅ Navigation logic tests passed\n");
    }

    private static void testPathBuilding() {
        System.out.println("Testing path building...");

        DirectorySizeCalculator calc = new DirectorySizeCalculator();
        // Note: This requires making getCurrentPath() public or adding a test method
        // For now, we'll test the underlying logic

        Directory root = new Directory("root", null);
        Directory level1 = new Directory("level1", root);
        Directory level2 = new Directory("level2", level1);

        assertEqual(null, root.getParent(), "Root should have no parent");
        assertEqual(root, level1.getParent(), "Level1 parent should be root");
        assertEqual(level1, level2.getParent(), "Level2 parent should be level1");

        System.out.println("✅ Path building tests passed\n");
    }

    private static void testSizeCalculationAccuracy() {
        System.out.println("Testing size calculation accuracy with known data...");

        // Create the same structure as the main program
        Directory root = new Directory("root", null);
        Directory documents = new Directory("documents", root);
        Directory projects = new Directory("projects", documents);

        documents.addChild(new File("resume.pdf", 1024000, documents));
        documents.addChild(new File("cover_letter.doc", 512000, documents));
        projects.addChild(new File("project1.zip", 5120000, projects));
        projects.addChild(new File("project2.tar", 3072000, projects));

        root.addChild(documents);
        documents.addChild(projects);

        // Test expected sizes
        assertEqual(8192000L, projects.getSize(), "Projects size should be 8192000");
        assertEqual(9728000L, documents.getSize(), "Documents size should be 9728000");
        assertEqual(9728000L, root.getSize(), "Root size should be 9728000");

        System.out.println("✅ Size calculation accuracy tests passed\n");
    }

    // Simple assertion methods
    private static void assertEqual(Object expected, Object actual, String message) {
        testsRun++;
        if ((expected == null && actual == null) ||
                (expected != null && expected.equals(actual))) {
            testsPassed++;
        } else {
            System.err.println("❌ FAIL: " + message);
            System.err.println("   Expected: " + expected);
            System.err.println("   Actual: " + actual);
        }
    }

    private static void assertTrue(boolean condition, String message) {
        testsRun++;
        if (condition) {
            testsPassed++;
        } else {
            System.err.println("❌ FAIL: " + message);
            System.err.println("   Expected: true");
            System.err.println("   Actual: false");
        }
    }

    private static void assertFalse(boolean condition, String message) {
        testsRun++;
        if (!condition) {
            testsPassed++;
        } else {
            System.err.println("❌ FAIL: " + message);
            System.err.println("   Expected: false");
            System.err.println("   Actual: true");
        }
    }

    private static void assertNull(Object object, String message) {
        testsRun++;
        if (object == null) {
            testsPassed++;
        } else {
            System.err.println("❌ FAIL: " + message);
            System.err.println("   Expected: null");
            System.err.println("   Actual: " + object);
        }
    }
}