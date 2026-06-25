package ui;

import gestionnaires.BanqueManager;
import modeles.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Fenêtre de gestion des comptes bancaires.
 * Permet de créer, fermer et consulter les comptes.
 */
public class FenetreGestionComptes extends JDialog {

    private final BanqueManager   banque;
    private DefaultTableModel     modele;
    private JTable                table;
    private JLabel                lblTotal;

    // Colonnes du tableau
    private static final String[] COLONNES = {
        "Numéro", "Type", "Propriétaire", "Profil", "Solde (€)", "Ouvert le"
    };

    public FenetreGestionComptes(Frame parent, BanqueManager banque) {
        super(parent, "Gestion des comptes", true);
        this.banque = banque;
        initUI();
        charger();
        setSize(820, 480);
        setLocationRelativeTo(parent);
    }

    private void initUI() {
        setLayout(new BorderLayout(6, 6));

        // ── En-tête coloré ───────────────────────────────────────────
        add(enTete("💳  Comptes bancaires", new Color(25, 100, 200)), BorderLayout.NORTH);

        // ── Tableau ──────────────────────────────────────────────────
        modele = new DefaultTableModel(COLONNES, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(modele);
        table.setRowHeight(24);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ── Panneau bas (solde + boutons) ────────────────────────────
        JPanel bas = new JPanel(new BorderLayout());

        lblTotal = new JLabel("Total : 0,00 €", SwingConstants.RIGHT);
        lblTotal.setFont(new Font("Arial", Font.BOLD, 13));
        lblTotal.setForeground(new Color(0, 110, 0));
        lblTotal.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 15));
        bas.add(lblTotal, BorderLayout.EAST);

        JPanel boutons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        boutons.add(btn("+ Compte Client",        new Color(25, 100, 200), e -> creerCompte("client")));
        boutons.add(btn("+ Compte Employé",       new Color(20, 130, 80),  e -> creerCompte("employe")));
        boutons.add(btn("+ Compte Investissement",new Color(160, 80, 10),  e -> creerCompte("investissement")));
        boutons.add(btn("✖ Fermer compte",        new Color(180, 30, 30),  e -> fermerCompte()));
        boutons.add(btn("🔍 Historique",          new Color(80, 80, 80),   e -> voirHistorique()));
        bas.add(boutons, BorderLayout.WEST);

        add(bas, BorderLayout.SOUTH);
    }

    /** Charge tous les comptes dans le tableau */
    private void charger() {
        modele.setRowCount(0);
        double total = 0;

        for (Compte c : banque.getComptes()) {
            modele.addRow(new Object[]{
                c.getNumeroCompte(),
                c.getType(),
                c.getProprietaire().getNom(),
                c.getProprietaire().getType(), // "Client" ou "Employé"
                String.format("%.2f", c.getSolde()),
                c.getDateCreation()
            });
            total += c.getSolde();
        }
        lblTotal.setText(String.format("Total : %.2f €", total));
    }

    /** Ouvre un dialogue pour créer un nouveau compte */
    private void creerCompte(String type) {
        // Choisir le propriétaire selon le type de compte
        User[] choix;
        String label;

        if (type.equals("employe")) {
            // Compte employé → liste des employés
            List<Employee> emp = banque.getEmployes();
            if (emp.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Aucun employé enregistré.");
                return;
            }
            choix = emp.toArray(new User[0]);
            label = "Choisir l'employé :";
        } else {
            // Compte client ou investissement → liste des clients
            Object[] arr = banque.getClients().toArray();
            if (arr.length == 0) {
                JOptionPane.showMessageDialog(this, "Aucun client enregistré.");
                return;
            }
            choix = java.util.Arrays.copyOf(arr, arr.length, User[].class);
            label = "Choisir le client :";
        }

        User selectionne = (User) JOptionPane.showInputDialog(
            this, label, "Nouveau compte",
            JOptionPane.QUESTION_MESSAGE, null, choix, choix[0]);
        if (selectionne == null) return;

        try {
            Compte c = banque.creerCompte(type, selectionne);
            JOptionPane.showMessageDialog(this,
                "Compte créé : " + c.getNumeroCompte(), "Succès", JOptionPane.INFORMATION_MESSAGE);
            charger();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Ferme le compte sélectionné */
    private void fermerCompte() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Sélectionnez un compte."); return; }

        String numero = (String) modele.getValueAt(row, 0);
        int rep = JOptionPane.showConfirmDialog(this,
            "Fermer le compte " + numero + " ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (rep != JOptionPane.YES_OPTION) return;

        try {
            Compte c = banque.chercherCompte(numero);
            banque.fermerCompte(c);
            charger();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Affiche l'historique des opérations du compte sélectionné */
    private void voirHistorique() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Sélectionnez un compte."); return; }

        String numero = (String) modele.getValueAt(row, 0);
        try {
            Compte c = banque.chercherCompte(numero);
            JTextArea area = new JTextArea(String.join("\n", c.getHistorique()));
            area.setFont(new Font("Monospaced", Font.PLAIN, 12));
            area.setEditable(false);
            JScrollPane scroll = new JScrollPane(area);
            scroll.setPreferredSize(new Dimension(500, 280));
            JOptionPane.showMessageDialog(this, scroll, "Historique — " + numero, JOptionPane.PLAIN_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Helpers UI ───────────────────────────────────────────────────

    private JButton btn(String texte, Color couleur, java.awt.event.ActionListener action) {
        JButton b = new JButton(texte);
        b.setBackground(couleur); b.setForeground(Color.WHITE);
        b.setFocusPainted(false); b.setBorderPainted(false);
        b.addActionListener(action);
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
