package ui;

import gestionnaires.BanqueManager;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import modeles.*;

/**
 * Fenêtre pour effectuer des opérations bancaires.
 * Trois onglets : Dépôt, Retrait, Virement.
 */
public class FenetreTransactions extends JDialog {

    private final BanqueManager banque;

    // Champs du formulaire Dépôt
    private JComboBox<Compte> comboDepotCompte;
    private JTextField        txtDepotMontant;
    private JTextField        txtDepotDesc;

    // Champs du formulaire Retrait
    private JComboBox<Compte> comboRetraitCompte;
    private JTextField        txtRetraitMontant;
    private JTextField        txtRetraitDesc;

    // Champs du formulaire Virement
    private JComboBox<Compte> comboVirSource;
    private JComboBox<Compte> comboVirDest;
    private JTextField        txtVirMontant;
    private JTextField        txtVirDesc;

    // Tableau de l'historique
    private DefaultTableModel modeleHisto;

    public FenetreTransactions(Frame parent, BanqueManager banque) {
        super(parent, "Transactions", true);
        this.banque = banque;
        initUI();
        remplirCombos();
        setSize(700, 480);
        setLocationRelativeTo(parent);
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JTabbedPane onglets = new JTabbedPane();
        onglets.addTab("💰 Dépôt",    creerOngletDepot());
        onglets.addTab("🏧 Retrait",  creerOngletRetrait());
        onglets.addTab("🔄 Virement", creerOngletVirement());
        onglets.addTab("📋 Historique", creerOngletHistorique());

        add(onglets, BorderLayout.CENTER);
    }

    // ================================================================
    // Construction des onglets
    // ================================================================

    private JPanel creerOngletDepot() {
        JPanel p = panneauFormulaire();
        comboDepotCompte = new JComboBox<>();
        txtDepotMontant  = new JTextField(15);
        txtDepotDesc     = new JTextField(15);

        ajouterLigne(p, "Compte cible :", comboDepotCompte, 0);
        ajouterLigne(p, "Montant (€) :", txtDepotMontant,   1);
        ajouterLigne(p, "Description :", txtDepotDesc,      2);
        ajouterBouton(p, btn("Déposer", new Color(20, 140, 80), e -> effectuerDepot()), 3);
        return p;
    }

    private JPanel creerOngletRetrait() {
        JPanel p = panneauFormulaire();
        comboRetraitCompte = new JComboBox<>();
        txtRetraitMontant  = new JTextField(15);
        txtRetraitDesc     = new JTextField(15);

        ajouterLigne(p, "Compte source :", comboRetraitCompte, 0);
        ajouterLigne(p, "Montant (€) :",   txtRetraitMontant,  1);
        ajouterLigne(p, "Description :",   txtRetraitDesc,     2);
        ajouterBouton(p, btn("Retirer", new Color(180, 40, 40), e -> effectuerRetrait()), 3);
        return p;
    }

    private JPanel creerOngletVirement() {
        JPanel p = panneauFormulaire();
        comboVirSource = new JComboBox<>();
        comboVirDest   = new JComboBox<>();
        txtVirMontant  = new JTextField(15);
        txtVirDesc     = new JTextField(15);

        ajouterLigne(p, "Compte source :",      comboVirSource, 0);
        ajouterLigne(p, "Compte destination :", comboVirDest,   1);
        ajouterLigne(p, "Montant (€) :",        txtVirMontant,  2);
        ajouterLigne(p, "Motif :",              txtVirDesc,     3);
        ajouterBouton(p, btn("Virer", new Color(25, 100, 200), e -> effectuerVirement()), 4);
        return p;
    }

    private JPanel creerOngletHistorique() {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        modeleHisto = new DefaultTableModel(
            new String[]{"ID", "Type", "Montant (€)", "Description", "Date", "Exécutée"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable t = new JTable(modeleHisto);
        t.setRowHeight(22);
        p.add(new JScrollPane(t), BorderLayout.CENTER);

        JButton btnRefresh = btn("Rafraîchir", new Color(60, 60, 60), e -> chargerHistorique());
        JPanel bas = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bas.add(btnRefresh);
        p.add(bas, BorderLayout.SOUTH);

        chargerHistorique();
        return p;
    }

    // ================================================================
    // Logique des opérations
    // ================================================================

    private void effectuerDepot() {
        try {
            Compte c      = compteSelectionne(comboDepotCompte);
            double montant = lireMontant(txtDepotMontant);
            String desc   = txtDepotDesc.getText().trim();

            Depot depot = new Depot(montant, c, desc.isEmpty() ? "Dépôt" : desc);
            depot.executer();
            banque.enregistrerTransaction(depot);

            succes(String.format("Dépôt de %.2f€ effectué sur %s", montant, c.getNumeroCompte()));
            vider(txtDepotMontant, txtDepotDesc);

        } catch (Exception ex) {
            erreur(ex.getMessage());
        }
    }

    private void effectuerRetrait() {
        try {
            Compte c       = compteSelectionne(comboRetraitCompte);
            double montant  = lireMontant(txtRetraitMontant);
            String desc    = txtRetraitDesc.getText().trim();

            Retrait retrait = new Retrait(montant, c, desc.isEmpty() ? "Retrait" : desc);
            retrait.executer();
            banque.enregistrerTransaction(retrait);

            succes(String.format("Retrait de %.2f€ effectué depuis %s", montant, c.getNumeroCompte()));
            vider(txtRetraitMontant, txtRetraitDesc);

        } catch (Exception ex) {
            erreur(ex.getMessage());
        }
    }

    private void effectuerVirement() {
        try {
            Compte src    = compteSelectionne(comboVirSource);
            Compte dest   = compteSelectionne(comboVirDest);
            double montant = lireMontant(txtVirMontant);
            String desc   = txtVirDesc.getText().trim();

            Virement v = new Virement(montant, src, dest, desc.isEmpty() ? "Virement" : desc);
            v.executer();
            banque.enregistrerTransaction(v);

            succes(String.format("Virement de %.2f€ de %s vers %s",
                montant, src.getNumeroCompte(), dest.getNumeroCompte()));
            vider(txtVirMontant, txtVirDesc);

        } catch (Exception ex) {
            erreur(ex.getMessage());
        }
    }

    // ================================================================
    // Chargement des données dans les combos et le tableau
    // ================================================================

    /** Remplit toutes les ComboBox avec la liste des comptes disponibles */
    private void remplirCombos() {
        List<Compte> comptes = banque.getComptes();
        for (@SuppressWarnings("unchecked") JComboBox<Compte> combo : new JComboBox[]{
                comboDepotCompte, comboRetraitCompte, comboVirSource, comboVirDest}) {
            combo.removeAllItems();
            comptes.forEach(combo::addItem);
        }
    }

    private void chargerHistorique() {
        modeleHisto.setRowCount(0);
        for (Transaction t : banque.getTransactions()) {
            modeleHisto.addRow(new Object[]{
                t.getId(), t.getType(),
                String.format("%.2f", t.getMontant()),
                t.getDescription(),
                t.getDate().toLocalDate(),
                t.isExecutee() ? "✔" : "✘"
            });
        }
    }

    // ================================================================
    // Helpers
    // ================================================================

    /** Lit et valide un montant depuis un champ texte */
    private double lireMontant(JTextField champ) throws Exception {
        try {
            double v = Double.parseDouble(champ.getText().replace(",", ".").trim());
            if (v <= 0) throw new Exception("Le montant doit être positif");
            return v;
        } catch (NumberFormatException e) {
            throw new Exception("Montant invalide : " + champ.getText());
        }
    }

    /** Retourne le compte sélectionné dans une combo */
    private Compte compteSelectionne(JComboBox<Compte> combo) throws Exception {
        Compte c = (Compte) combo.getSelectedItem();
        if (c == null) throw new Exception("Aucun compte sélectionné");
        return c;
    }

    private void succes(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Succès", JOptionPane.INFORMATION_MESSAGE);
        chargerHistorique();
        remplirCombos(); // Rafraîchit les soldes dans les combos
    }

    private void erreur(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    private void vider(JTextField... champs) {
        for (JTextField c : champs) c.setText("");
    }

    // ── Helpers layout ───────────────────────────────────────────────

    private JPanel panneauFormulaire() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        return p;
    }

    private void ajouterLigne(JPanel p, String label, JComponent champ, int ligne) {
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.anchor = GridBagConstraints.WEST;

        g.gridx = 0; g.gridy = ligne;
        p.add(new JLabel(label), g);

        g.gridx = 1; g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1;
        p.add(champ, g);
    }

    private void ajouterBouton(JPanel p, JButton btn, int ligne) {
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.gridy = ligne; g.gridwidth = 2;
        g.insets = new Insets(18, 0, 0, 0);
        p.add(btn, g);
    }

    private JButton btn(String texte, Color couleur, java.awt.event.ActionListener action) {
        JButton b = new JButton(texte);
        b.setBackground(couleur); b.setForeground(Color.WHITE);
        b.setFocusPainted(false); b.setBorderPainted(false);
        b.setFont(new Font("Arial", Font.BOLD, 13));
        b.addActionListener(action);
        return b;
    }
}
