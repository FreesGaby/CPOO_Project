import java.util.*;

// Classe pour l'évaluation des dispositions
public class KeyBoardEvaluator {
    private Map<Character, String> layout;
    private Map<String, Double> movementWeights;

    public KeyBoardEvaluator() {
        this.layout = new HashMap<>();
        this.movementWeights = new HashMap<>();
    }

    // Méthode pour charger une disposition de clavier
    public void loadKeyboardLayout(String layoutFile) {
        // Exemple de chargement fictif
        layout.put('a', "left_pinky");
        layout.put('s', "left_ring");
        layout.put('d', "left_middle");
        layout.put('f', "left_index");
        // Ajout de plus de touches selon le fichier fourni
    }

    // Méthode pour évaluer une disposition par rapport à un corpus
    public double evaluate(Map<String, Integer> nGramFrequencies) {
        double score = 0.0;
        for (Map.Entry<String, Integer> entry : nGramFrequencies.entrySet()) {
            String nGram = entry.getKey();
            int frequency = entry.getValue();
            score += calculateMovementScore(nGram) * frequency;
        } 
        return score;
    }

    private double calculateMovementScore(String nGram) {
        // Exemple de calcul fictif basé sur les poids des mouvements
        return movementWeights.getOrDefault(nGram, 1.0);
    }
}
