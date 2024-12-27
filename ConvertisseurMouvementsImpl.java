import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConvertisseurMouvementsImpl implements ConvertisseurMouvements {
    @Override
    public List<Mouvement> convertir(String ngramme, DispositionClavier disposition) {
        List<Mouvement> mouvements = new ArrayList<>();

        Touche derniereTouche = null;
        for (char c : ngramme.toCharArray()) {
            Optional<Touche> toucheActuelle = disposition.getTouche(c, false, false); // Par défaut, sans Shift ni AltGr
            if (toucheActuelle.isPresent()) {
                if (derniereTouche != null) {
                    mouvements.add(new Mouvement(derniereTouche, toucheActuelle.get()));
                }
                derniereTouche = toucheActuelle.get();
            } else {
                // Gérer les cas où le caractère n'est pas trouvé dans la disposition
                System.err.println("Caractère introuvable dans la disposition : " + c);
            }
        }

        return mouvements;
    }
}
