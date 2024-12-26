import java.util.*;

// Classe pour l'optimisation des dispositions
public class KeyBoardOptimizer {
    private List<Map<Character, String>> population;

    public KeyBoardOptimizer() {
        this.population = new ArrayList<>();
        initializePopulation();
    }

    // Méthode pour initialiser une population de dispositions
    private void initializePopulation() {
        // Exemple fictif : ajout d'une disposition de base
        Map<Character, String> defaultLayout = new HashMap<>();
        defaultLayout.put('a', "left_pinky");
        defaultLayout.put('s', "left_ring");
        this.population.add(defaultLayout);
    }

    // Méthode pour générer une nouvelle disposition
    public Map<Character, String> generateNewLayout(Map<Character, String> parentLayout) {
        Map<Character, String> newLayout = new HashMap<>(parentLayout);
        // Appliquer une mutation simple (échanger deux touches, par exemple)
        List<Character> keys = new ArrayList<>(newLayout.keySet());
        Collections.swap(keys, 0, 1);
        newLayout.put(keys.get(0), parentLayout.get(keys.get(1)));
        newLayout.put(keys.get(1), parentLayout.get(keys.get(0)));
        return newLayout;
    }

    // Méthode pour évaluer et sélectionner la meilleure disposition
    public void optimize(int iterations, KeyBoardEvaluator evaluator, Map<String, Integer> nGramFrequencies) {
        for (int i = 0; i < iterations; i++) {
            Map<Character, String> bestLayout = null;
            double bestScore = Double.MAX_VALUE;
            for (Map<Character, String> layout : population) {
                double score = evaluator.evaluate(nGramFrequencies);
                if (score < bestScore) {
                    bestScore = score;
                    bestLayout = layout;
                }
            }
            // Ajouter un nouveau candidat basé sur le meilleur layout
            if (bestLayout != null) {
                population.add(generateNewLayout(bestLayout));
            }
        }
    }
}
