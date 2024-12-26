public class Mouvement {
    private Touche debut;
    private Touche fin;

    public Mouvement(Touche debut, Touche fin) {
        this.debut = debut;
        this.fin = fin;
    }

    public Touche getDebut() {
        return debut;
    }

    public Touche getFin() {
        return fin;
    }

    public boolean estAlternanceMain() {
        return debut.getDoigt() != fin.getDoigt() && !memeMain(debut, fin);
    }

    private boolean memeMain(Touche a, Touche b) {
        // Par exemple, vérifier la colonne pour déterminer si les deux touches sont sur la même main
        return a.getColonne() < 5 && b.getColonne() < 5 || a.getColonne() >= 5 && b.getColonne() >= 5;
    }
}