import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
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

    public static DispositionClavier fromKlcFile(String filePath) throws FileNotFoundException {
        Map<Character, Touche> baseDisposition = new HashMap<>();
        Map<Character, Touche> shiftDisposition = new HashMap<>();
        Map<Character, Touche> altGrDisposition = new HashMap<>();
        String nomDisposition = "KLC_Imported";

        Scanner scanner = new Scanner(new File(filePath));
        boolean isLayoutSection = false;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            if (line.startsWith("KBD")) {
                nomDisposition = line.split(" ")[1];
            }

            if (line.equals("LAYOUT")) {
                isLayoutSection = true;
                continue;
            }

            if (isLayoutSection && line.isEmpty()) {
                break;
            }

            if (isLayoutSection && !line.startsWith("//")) {
                String[] parts = line.split("\\s+", 5);
                if (parts.length >= 5) {
                    String vkCode = parts[0];
                    char baseChar = (char) Integer.parseInt(parts[1], 16);
                    char shiftChar = (char) Integer.parseInt(parts[2], 16);
                    char altGrChar = (char) Integer.parseInt(parts[4], 16);

                    int colonne = vkCode.hashCode() % 10; // Simplification pour colonne
                    int rangee = vkCode.hashCode() / 10 % 5; // Simplification pour rangée

                    Touche baseTouche = new Touche(colonne, rangee, Doigt.INDEX); // Par défaut, associer à un doigt
                    baseDisposition.put(baseChar, baseTouche);

                    if (shiftChar != 0) {
                        Touche shiftTouche = new Touche(colonne, rangee, Doigt.INDEX);
                        shiftDisposition.put(shiftChar, shiftTouche);
                    }

                    if (altGrChar != 0) {
                        Touche altGrTouche = new Touche(colonne, rangee, Doigt.INDEX);
                        altGrDisposition.put(altGrChar, altGrTouche);
                    }
                }
            }
        }

        scanner.close();
        return new DispositionClavier(nomDisposition, baseDisposition, shiftDisposition, altGrDisposition);
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