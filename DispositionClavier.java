import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class DispositionClavier {
    private String nom;
    private Map<Character, Touche> disposition;
    private Map<Character, Touche> shiftDisposition;
    private Map<Character, Touche> altGrDisposition;

    public DispositionClavier(String nom, Map<Character, Touche> disposition, Map<Character, Touche> shiftDisposition, Map<Character, Touche> altGrDisposition) {
        this.nom = nom;
        this.disposition = disposition;
        this.shiftDisposition = shiftDisposition;
        this.altGrDisposition = altGrDisposition;
    }

    public String getNom() {
        return nom;
    }

    public Optional<Touche> getTouche(char caractere, boolean shift, boolean altGr) {
        if (shift) {
            return Optional.ofNullable(shiftDisposition.get(caractere));
        } else if (altGr) {
            return Optional.ofNullable(altGrDisposition.get(caractere));
        } else {
            return Optional.ofNullable(disposition.get(caractere));
        }
    }

    public DispositionClavier cloneWithMutation() {
        // Clone la disposition actuelle avec une mutation aléatoire
        Map<Character, Touche> nouvelleDisposition = new HashMap<>(this.disposition);
        List<Character> caracteres = new ArrayList<>(nouvelleDisposition.keySet());
        Random random = new Random();

        // Mutation simple : inverser deux caractères
        int index1 = random.nextInt(caracteres.size());
        int index2 = random.nextInt(caracteres.size());

        char c1 = caracteres.get(index1);
        char c2 = caracteres.get(index2);

        Touche temp = nouvelleDisposition.get(c1);
        nouvelleDisposition.put(c1, nouvelleDisposition.get(c2));
        nouvelleDisposition.put(c2, temp);

        return new DispositionClavier(this.nom + "_mutated", nouvelleDisposition, this.shiftDisposition, this.altGrDisposition);
    }
}