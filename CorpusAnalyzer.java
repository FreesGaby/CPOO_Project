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
        // line = line.replaceAll("\s+", ""); // Supprimer les espaces
        for (int i = 0; i <= line.length() - n; i++) {
            String nGram = line.substring(i, i + n);
            nGramFrequencies.put(nGram, nGramFrequencies.getOrDefault(nGram, 0) + 1);
        }
    }

    // Méthode pour exporter les résultats dans un fichier CSV
    public void exportStatistics(String outputPath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            for (Map.Entry<String, Integer> entry : nGramFrequencies.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
        }
    }   

    // Méthode pour afficher les résultats de manière structurée
    public void displayStatistics() {
        System.out.println("\n=== Résultats des N-grammes ===");
        nGramFrequencies.entrySet().stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())) // Trier par fréquence décroissante
            .forEach(entry -> System.out.printf("N-gramme: '%s', Fréquence: %d\n", entry.getKey(), entry.getValue()));
        System.out.println("================================");
    }

    // Tests unitaires pour valider les fonctionnalités
    public static void main(String[] args) {
        try {
            CorpusAnalyzer analyzer = new CorpusAnalyzer();

            // Test 1 : Analyse d'un corpus fictif
            String testCorpus = "test_corpus.txt";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(testCorpus))) {
                writer.write("hello world\nhello java\nhello world");
            }
            analyzer.analyzeCorpus(testCorpus, 2);

            // Vérification des fréquences des bigrammes
            System.out.println("Test 1 - Analyse de corpus");
            analyzer.displayStatistics(); // Affichage structuré des résultats

            // Test 2 : Export au format CSV
            String outputCsv = "output_statistics.csv";
            analyzer.exportStatistics(outputCsv);
            System.out.println("Test 2 - Export CSV effectué : " + outputCsv);

            // Nettoyage des fichiers de test
            new File(testCorpus).delete();
            new File(outputCsv).delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
