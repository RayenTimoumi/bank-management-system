package ui;

import gestionnaires.BanqueManager;
import java.awt.*;
import java.util.Map;
import javax.swing.*;
import modeles.Employee;

/**
 * Fenêtre de statistiques et de rapports financiers.
 */
public class FenetreRapports extends JDialog {

    private final BanqueManager banque;
    private JTextArea           aireTexte;

    public FenetreRapports(Frame parent, BanqueManager banque) {
        super(parent, "Rapports & Statistiques", true);
        this.banque = banque;
        initUI();
        afficherRapportGeneral(); // Affiche le rapport général au démarrage
        setSize(680, 520);
        setLocationRelativeTo(parent);
    }

    private void initUI() {
        setLayout(new BorderLayout(6, 6));
        add(enTete("📊  Rapports financiers", new Color(160, 80, 10)), BorderLayout.NORTH);

        // Zone de texte pour afficher les rapports
        aireTexte = new JTextArea();
        aireTexte.setFont(new Font("Monospaced", Font.PLAIN, 13));
        aireTexte.setEditable(false);
        aireTexte.setBackground(new Color(248, 248, 252));
        add(new JScrollPane(aireTexte), BorderLayout.CENTER);

        // Boutons pour choisir le type de rapport
        JPanel boutons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        boutons.add(btn("Rapport général",       new Color(160, 80, 10),  e -> afficherRapportGeneral()));
        boutons.add(btn("Soldes par type",       new Color(25, 100, 200), e -> afficherSoldesParType()));
        boutons.add(btn("Top clients",           new Color(110, 20, 160), e -> afficherTopClients()));
        boutons.add(btn("Récap. employés",       new Color(160, 30, 30),  e -> afficherRecapEmployes()));
        boutons.add(btn("Transactions",          new Color(50, 50, 60),   e -> afficherTransactions()));
        add(boutons, BorderLayout.SOUTH);
    }

    // ================================================================
    // Rapports
    // ================================================================

    /** Vue d'ensemble de la banque */
    private void afficherRapportGeneral() {
        StringBuilder sb = new StringBuilder();
        sb.append("╔══════════════════════════════════════════╗\n");
        sb.append("║         RAPPORT GÉNÉRAL — MaBank         ║\n");
        sb.append("╚══════════════════════════════════════════╝\n\n");

        sb.append(String.format("  Clients enregistrés  : %d%n",  banque.getClients().size()));
        sb.append(String.format("  Employés             : %d%n",  banque.getEmployes().size()));
        sb.append(String.format("  Comptes ouverts      : %d%n",  banque.getComptes().size()));
        sb.append(String.format("  Transactions         : %d%n",  banque.getTransactions().size()));
        sb.append("\n");
        sb.append(String.format("  Solde total banque   : %.2f €%n", banque.calculerSoldeTotal()));
        sb.append(String.format("  Masse salariale      : %.2f €%n", banque.calculerMasseSalariale()));

        aireTexte.setText(sb.toString());
    }

    /** Solde total regroupé par type de compte */
    private void afficherSoldesParType() {
        StringBuilder sb = new StringBuilder();
        sb.append("╔══════════════════════════════════════════╗\n");
        sb.append("║           SOLDES PAR TYPE DE COMPTE      ║\n");
        sb.append("╚══════════════════════════════════════════╝\n\n");

        Map<String, Double> soldes = banque.getSoldesParType();
        if (soldes.isEmpty()) {
            sb.append("  Aucun compte enregistré.\n");
        } else {
            soldes.forEach((type, total) ->
                sb.append(String.format("  %-25s  %.2f €%n", type, total)));
            sb.append("\n");
            sb.append(String.format("  %-25s  %.2f €%n", "TOTAL", banque.calculerSoldeTotal()));
        }

        aireTexte.setText(sb.toString());
    }

    /** Les clients classés par solde total décroissant */
    private void afficherTopClients() {
        StringBuilder sb = new StringBuilder();
        sb.append("╔══════════════════════════════════════════╗\n");
        sb.append("║           CLASSEMENT DES CLIENTS         ║\n");
        sb.append("╚══════════════════════════════════════════╝\n\n");

        // Trie les clients par solde total (via Streams)
        banque.getClients().stream()
            .sorted((a, b) -> Double.compare(banque.calculerSoldeUser(b), banque.calculerSoldeUser(a)))
            .forEach(c -> {
                int nbComptes = banque.getComptesDeUser(c).size();
                sb.append(String.format("  %-25s  %d compte(s)  %.2f €%n",
                    c.getNom(), nbComptes, banque.calculerSoldeUser(c)));
            });

        if (banque.getClients().isEmpty()) sb.append("  Aucun client enregistré.\n");
        aireTexte.setText(sb.toString());
    }

    /** Récapitulatif du personnel avec salaires et bonus */
    private void afficherRecapEmployes() {
        StringBuilder sb = new StringBuilder();
        sb.append("╔══════════════════════════════════════════╗\n");
        sb.append("║           RÉCAPITULATIF EMPLOYÉS         ║\n");
        sb.append("╚══════════════════════════════════════════╝\n\n");

        for (Employee e : banque.getEmployes()) {
            sb.append(String.format("  %-20s  %-15s  Salaire: %.2f€  Bonus: %.2f€%n",
                e.getNom(), "[" + e.getRole() + "]", e.getSalaire(), e.calculerBonus()));
        }

        if (banque.getEmployes().isEmpty()) {
            sb.append("  Aucun employé enregistré.\n");
        } else {
            sb.append("\n");
            sb.append(String.format("  Masse salariale totale : %.2f €%n", banque.calculerMasseSalariale()));
        }

        aireTexte.setText(sb.toString());
    }

    /** Liste toutes les transactions enregistrées */
    private void afficherTransactions() {
        StringBuilder sb = new StringBuilder();
        sb.append("╔══════════════════════════════════════════╗\n");
        sb.append("║           HISTORIQUE DES TRANSACTIONS    ║\n");
        sb.append("╚══════════════════════════════════════════╝\n\n");

        var transactions = banque.getTransactions();
        if (transactions.isEmpty()) {
            sb.append("  Aucune transaction enregistrée.\n");
        } else {
            for (var t : transactions) {
                sb.append(String.format("  %s  %-12s  %.2f€  %s%n",
                    t.getDate().toLocalDate(), t.getType(), t.getMontant(), t.getDescription()));
            }
        }

        aireTexte.setText(sb.toString());
    }

    // ── Helpers UI ───────────────────────────────────────────────────

    private JButton btn(String t, Color c, java.awt.event.ActionListener a) {
        JButton b = new JButton(t);
        b.setBackground(c); b.setForeground(Color.WHITE);
        b.setFocusPainted(false); b.setBorderPainted(false);
        b.addActionListener(a);
        return b;
    }

    private JLabel enTete(String texte, Color couleur) {
        JLabel l = new JLabel("  " + texte, SwingConstants.LEFT);
        l.setOpaque(true); l.setBackground(couleur); l.setForeground(Color.WHITE);
        l.setFont(new Font("Arial", Font.BOLD, 15));
        l.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 0));
        return l;
    }
}
