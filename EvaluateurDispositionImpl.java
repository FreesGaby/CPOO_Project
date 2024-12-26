import java.util.List;
import java.util.Map;

public class EvaluateurDispositionImpl implements EvaluateurDisposition {
    private final ConvertisseurMouvements convertisseur;

    public EvaluateurDispositionImpl(ConvertisseurMouvements convertisseur) {
        this.convertisseur = convertisseur;
    }

    @Override
    public double evaluer(DispositionClavier disposition, Corpus corpus) {
        double scoreTotal = 0;
        Map<String, Integer> statistiques = new AnalyseurCorpusImpl().analyser(corpus);

        for (Map.Entry<String, Integer> entry : statistiques.entrySet()) {
            String ngramme = entry.getKey();
            int occurrence = entry.getValue();

            List<Mouvement> mouvements = convertisseur.convertir(ngramme, disposition);
            for (Mouvement mouvement : mouvements) {
                scoreTotal += calculerScoreMouvement(mouvement) * occurrence;
            }
        }

        return scoreTotal / corpus.getTextes().size();
    }

    private double calculerScoreMouvement(Mouvement mouvement) {
        if (mouvement.estAlternanceMain()) {
            return 1.0; // Bonus pour alternance entre les mains
        } else {
            return 2.0; // Malus pour mouvement sur une seule main
        }
    }
}