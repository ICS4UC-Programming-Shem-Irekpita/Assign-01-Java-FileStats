import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads a text file, computes line, word, character, and longest word
 * statistics, stores them in an array, and writes the output to a file.
 * @author Shem Irekpita
 * @version 1.0
 * @since 2026-11-19
 */
public final class FileStats {

    /** Index for total lines in the statistics array. */
    private static final int LINES_INDEX = 0;

    /** Index for total words in the statistics array. */
    private static final int WORDS_INDEX = 1;

    /** Index for total characters in the statistics array. */
    private static final int CHARS_INDEX = 2;

    /** Index for longest word length in the statistics array. */
    private static final int LONGEST_INDEX = 3;

    /** Total number of metrics stored in the statistics array. */
    private static final int STATS_SIZE = 4;

    /**
     * Private constructor to prevent instantiation of utility class.
     *
     * @throws IllegalStateException if called
     */
    private FileStats() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Analyzes lines of text and returns basic file statistics in an array.
     *
     * @param lines the list of lines read from the file
     * @return an integer array [lines, words, characters, longestWordLength]
     */
    public static int[] analyzeFile(final List<String> lines) {
        final int[] stats = new int[STATS_SIZE];
        stats[LINES_INDEX] = lines.size();

        int totalWords = 0;
        int totalChars = 0;
        int longestWord = 0;

        for (final String line : lines) {
            totalChars += line.length();
            final String trimmed = line.trim();
            if (!trimmed.isEmpty()) {
                // Split by one or more whitespace characters
                final String[] words = trimmed.split("\\s+");
                totalWords += words.length;
                for (final String word : words) {
                    if (word.length() > longestWord) {
                        longestWord = word.length();
                    }
                }
            }
        }

        stats[WORDS_INDEX] = totalWords;
        stats[CHARS_INDEX] = totalChars;
        stats[LONGEST_INDEX] = longestWord;
        return stats;
    }

    /**
     * Formats the statistics array and writes the report to an output file.
     *
     * @param outputPath the destination file path
     * @param stats the integer array containing computed file statistics
     * @throws IOException if an I/O error occurs while writing the file
     */
    public static void writeResults(
            final String outputPath, final int[] stats) throws IOException {
        final List<String> outputLines = new ArrayList<>();
        outputLines.add("File Statistics Report (Java)");
        outputLines.add("-----------------------------");
        outputLines.add("Total Lines: " + stats[LINES_INDEX]);
        outputLines.add("Total Words: " + stats[WORDS_INDEX]);
        outputLines.add("Total Characters: " + stats[CHARS_INDEX]);
        outputLines.add("Longest Word Length: " + stats[LONGEST_INDEX]);

        final Path path = Paths.get(outputPath);
        Files.write(path, outputLines, StandardCharsets.UTF_8);
    }

    /**
     * Entry point of the program. Reads input.txt, computes statistics,
     * displays them to the console, and writes them to output.txt.
     *
     * @param args command-line arguments (optional input file path)
     */
    public static void main(final String[] args) {
        final String inputPath;
        if (args.length > 0) {
            inputPath = args[0];
        } else {
            inputPath = "input.txt";
        }
        final String outputPath = "output.txt";

        try {
            final Path path = Paths.get(inputPath);
            final List<String> lines = Files.readAllLines(
                    path, StandardCharsets.UTF_8);
            final int[] stats = analyzeFile(lines);

            System.out.println("Language: Java");
            System.out.println("File Analyzed: " + inputPath);
            System.out.println("Total Lines: " + stats[LINES_INDEX]);
            System.out.println("Total Words: " + stats[WORDS_INDEX]);
            System.out.println("Total Characters: " + stats[CHARS_INDEX]);
            System.out.println(
                    "Longest Word Length: " + stats[LONGEST_INDEX]);

            writeResults(outputPath, stats);
            System.out.println("Results saved to " + outputPath);
        } catch (final IOException error) {
            System.err.println(
                    "Error processing file: " + error.getMessage());
        }
    }
}
