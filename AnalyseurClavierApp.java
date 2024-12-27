import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AnalyseurClavierApp extends Application {
    private File fichierTexte;
    private File fichierKlc;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Analyseur de Clavier");

        Label labelTexte = new Label("Aucun fichier texte sélectionné");
        Button boutonTexte = new Button("Charger un fichier texte");
        boutonTexte.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Sélectionner un fichier texte");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers texte", "*.txt"));
            fichierTexte = fileChooser.showOpenDialog(primaryStage);
            if (fichierTexte != null) {
                labelTexte.setText("Fichier texte : " + fichierTexte.getName());
            }
        });

        Label labelKlc = new Label("Aucun fichier KLC sélectionné");
        Button boutonKlc = new Button("Charger un fichier KLC");
        boutonKlc.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Sélectionner un fichier KLC");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers KLC", "*.klc"));
            fichierKlc = fileChooser.showOpenDialog(primaryStage);
            if (fichierKlc != null) {
                labelKlc.setText("Fichier KLC : " + fichierKlc.getName());
            }
        });

        Button boutonLancer = new Button("Lancer l'analyse");
        Text resultat = new Text();
        boutonLancer.setOnAction(e -> {
            if (fichierTexte != null && fichierKlc != null) {
                try {
                    lancerAnalyse(fichierTexte, fichierKlc);
                    resultat.setText("Analyse terminée ! Résultat enregistré dans 'resultats.txt'.");
                } catch (Exception ex) {
                    resultat.setText("Erreur : " + ex.getMessage());
                }
            } else {
                resultat.setText("Veuillez sélectionner un fichier texte et un fichier KLC.");
            }
        });

        VBox layout = new VBox(10, boutonTexte, labelTexte, boutonKlc, labelKlc, boutonLancer, resultat);
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void lancerAnalyse(File texte, File klc) throws Exception {
        // Charger la disposition de clavier
        DispositionClavier disposition = DispositionClavier.fromKlcFile(klc.getAbsolutePath());

        // Lire le texte et créer le corpus
        List<String> lignes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(texte))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                lignes.add(ligne);
            }
        }
        Corpus corpus = new Corpus(lignes);

        // Analyser le corpus
        AnalyseurCorpus analyseur = new AnalyseurCorpusImpl();
        Map<String, Integer> statistiques = analyseur.analyser(corpus);

        // Évaluer la disposition
        ConvertisseurMouvements convertisseur = new ConvertisseurMouvementsImpl();
        EvaluateurDisposition evaluateur = new EvaluateurDispositionImpl(convertisseur);
        double score = evaluateur.evaluer(disposition, corpus);

        // Écrire les résultats
        try (PrintWriter writer = new PrintWriter(new FileWriter("resultats.txt"))) {
            writer.println("N-grammes :");
            for (Map.Entry<String, Integer> entry : statistiques.entrySet()) {
                writer.println(entry.getKey() + " : " + entry.getValue());
            }
            writer.println("\nScore de la disposition : " + score);
        }
    }
}
