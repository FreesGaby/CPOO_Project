public class Touche {
    private int colonne;
    private int rangée;
    private Doigt doigt;

    public Touche(int colonne, int rangée, Doigt doigt) {
        this.colonne = colonne;
        this.rangée = rangée;
        this.doigt = doigt;
    }

    public int getColonne() {
        return colonne;
    }   

    public int getRangée() {
        return rangée;
    }

    public Doigt getDoigt() {
        return doigt;
    }
}