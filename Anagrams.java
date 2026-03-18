import java.io.*;
import java.nio.file.*;
import java.util.*;
 
public class Anagrams{

public static void main(String[] args) throws IOException {
 // prints the dictionary file to the console
findAnagrams(\ulysses.text);
}
// this method uses buffered reader to read the file line by line and print it to the console
public static void readLargeFile(String filePath) {
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line); 
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
// thos method finds anagrams in the file and prints them to the console
    public static void findAnagrams(String filePath) throws IOException {
        
        Map<String, List<String>> anagramMap = new HashMap<>();
 
        List<String> lines = Files.readAllLines(Path.of(filePath));

        for (String line : lines) {
              
            String[] words = line.split("\\s+");
            for (String w : words) {
                String cleanWord = w.replaceAll("[^a-zA-Z]", "").toLowerCase();
                if (cleanWord.isEmpty()) continue;

             
                char[] chars = cleanWord.toCharArray();
                Arrays.sort(chars);
                String key = new String(chars);
 
                anagramMap.computeIfAbsent(key, k -> new ArrayList<>()).add(cleanWord);
            }
        }
        anagramMap.forEach((key, list) -> {
            if (list.size() > 1) {  
                System.out.println("Key [" + key + "]: " + list);
            }
        });
    }
    //  this method creates a LaTeX file with the anagrams found in the file
    public void createLatexFile(Map<String, List<String>> anagramMap) throws IOException {
    Path latexPath = Paths.get("latex");
    Files.createDirectories(latexPath);
    try (PrintWriter writer = new PrintWriter(new FileWriter("latex/theAnagrams.tex"))) {
        char currentHeader = ' ';

        for (Map.Entry<String, List<String>> entry : anagramMap.entrySet()) {
            List<String> words = entry.getValue();
            if (words.size() > 1) {
                String firstWord = words.get(0);
                char initial = Character.toUpperCase(firstWord.charAt(0));
                if (initial != currentHeader) {
                    currentHeader = initial;
                    writer.printf("\n\\vspace{14pt}\n\\noindent\\textbf{\\Large %c}\\\\*[+12pt]\n", currentHeader);
                }
                String anagramLine = String.join(" ", words);
                writer.println(anagramLine + "\\\\");
            }
        }
    }
    System.out.println("LaTeX file successfully created in /latex directory.");
}








}