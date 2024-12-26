import java.io.*;
import java.util.*;

// Classe pour l'analyse de corpus
public class CorpusAnalyzer {
    private Map<String, Integer> nGramFrequencies;

    public CorpusAnalyzer() {
        this.nGramFrequencies = new HashMap<>();
    }

    // Méthode pour lire un fichier corpus et analyser les N-grammes
    public void analyzeCorpus(String filePath, int n) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) processLine(line, n);
        }
    }

    private void processLine(String line, int n) {
        for (int i = 0; i <= line.length() - n; i++) {
            String nGram = line.substring(i, i + n);
            nGramFrequencies.put(nGram, nGramFrequencies.getOrDefault(nGram, 0) + 1);
        }
    }

    // Méthode pour exporter les résultats dans un fichier
    public void exportStatistics(String outputPath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            for (Map.Entry<String, Integer> entry : nGramFrequencies.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
        }
    }   
}
