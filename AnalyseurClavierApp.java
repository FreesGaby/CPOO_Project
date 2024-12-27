import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AnalyseurClavierApp {
    private File fichierTexte;
    private File fichierKlc;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AnalyseurClavierApp().createAndShowGUI());
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Analyseur de Clavier");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JLabel labelTexte = new JLabel("Aucun fichier texte sélectionné");
        JButton boutonTexte = new JButton("Charger un fichier texte");
        boutonTexte.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Sélectionner un fichier texte");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Fichiers texte", "txt"));
            int returnValue = fileChooser.showOpenDialog(frame);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                fichierTexte = fileChooser.getSelectedFile();
                labelTexte.setText("Fichier texte : " + fichierTexte.getName());
            }
        });

        JLabel labelKlc = new JLabel("Aucun fichier KLC sélectionné");
        JButton boutonKlc = new JButton("Charger un fichier KLC");
        boutonKlc.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Sélectionner un fichier KLC");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Fichiers KLC", "klc"));
            int returnValue = fileChooser.showOpenDialog(frame);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                fichierKlc = fileChooser.getSelectedFile();
                labelKlc.setText("Fichier KLC : " + fichierKlc.getName());
            }
        });

        JButton boutonLancer = new JButton("Lancer l'analyse");
        JTextArea resultat = new JTextArea(5, 30);
        resultat.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultat);
        boutonLancer.addActionListener(e -> {
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

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(boutonTexte);
        panel.add(labelTexte);
        panel.add(boutonKlc);
        panel.add(labelKlc);
        panel.add(boutonLancer);
        panel.add(scrollPane);

        frame.add(panel);
        frame.setVisible(true);
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
