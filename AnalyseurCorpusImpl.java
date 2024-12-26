import java.util.HashMap;
import java.util.Map;

public class AnalyseurCorpusImpl implements AnalyseurCorpus {

    @Override
    public Map<String, Integer> analyser(Corpus corpus) {
        Map<String, Integer> statistiques = new HashMap<>();

        for (String texte : corpus.getTextes()) {
            texte = texte.replaceAll("\\s+", " ").trim().toLowerCase();

            for (int i = 0; i < texte.length(); i++) {
                // 1-grammes
                String unGram = texte.substring(i, i + 1);
                statistiques.put(unGram, statistiques.getOrDefault(unGram, 0) + 1);

                // Bigrammes
                if (i < texte.length() - 1) {
                    String biGram = texte.substring(i, i + 2);
                    statistiques.put(biGram, statistiques.getOrDefault(biGram, 0) + 1);
                }

                // Trigrammes
                if (i < texte.length() - 2) {
                    String triGram = texte.substring(i, i + 3);
                    statistiques.put(triGram, statistiques.getOrDefault(triGram, 0) + 1);
                }
            }
        }

        return statistiques;
    }
}