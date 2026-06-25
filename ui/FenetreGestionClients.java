package ui;

import gestionnaires.BanqueManager;
import modeles.Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Fenêtre d'administration des clients.
 * Permet d'ajouter, modifier et supprimer des clients.
 */
public class FenetreGestionClients extends JDialog {

    private final BanqueManager   banque;
    private DefaultTableModel     modele;
    private JTable                table;

    private static final String[] COLONNES = {"ID", "Nom", "Email", "Téléphone", "Ville"};

    public FenetreGestionClients(Frame parent, BanqueManager banque) {
        super(parent, "Gestion des clients", true);
        this.banque = banque;
        initUI();
        charger();
        setSize(750, 420);
        setLocationRelativeTo(parent);
    }

    private void initUI() {
        setLayout(new BorderLayout(6, 6));
        add(enTete("👥  Clients", new Color(110, 20, 160)), BorderLayout.NORTH);

        // Tableau
        modele = new DefaultTableModel(COLONNES, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(modele);
        table.setRowHeight(24);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Boutons
        JPanel boutons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        boutons.add(btn("+ Ajouter",   new Color(0, 130, 60),   e -> ajouter()));
        boutons.add(btn("✏ Modifier",  new Color(0, 100, 180),  e -> modifier()));
        boutons.add(btn("✖ Supprimer", new Color(180, 30, 30),  e -> supprimer()));
        boutons.add(btn("📋 Comptes",  new Color(100, 60, 160), e -> voirComptes()));
        add(boutons, BorderLayout.SOUTH);
    }

    private void charger() {
        modele.setRowCount(0);
        for (Client c : banque.getClients()) {
            modele.addRow(new Object[]{
                c.getId(),
                c.getNom(),
                c.getEmail(),
                c.getTelephone() != null ? c.getTelephone() : "—",
                c.getVille()     != null ? c.getVille()     : "—"
            });
        }
    }

    private void ajouter() {
        // Formulaire de saisie
        JTextField fNom    = new JTextField(20);
        JTextField fEmail  = new JTextField(20);
        JTextField fTel    = new JTextField(15);
        JTextField fVille  = new JTextField(15);

        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        form.add(new JLabel("Nom * :"));       form.add(fNom);
        form.add(new JLabel("Email * :"));     form.add(fEmail);
        form.add(new JLabel("Téléphone :"));   form.add(fTel);
        form.add(new JLabel("Ville :"));       form.add(fVille);

        int rep = JOptionPane.showConfirmDialog(this, form, "Nouveau client",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (rep != JOptionPane.OK_OPTION) return;

        String nom   = fNom.getText().trim();
        String email = fEmail.getText().trim();

        if (nom.isEmpty() || email.isEmpty()) {
            erreur("Le nom et l'email sont obligatoires.");
            return;
        }

        try {
            // Utilise le constructeur complet si toutes les infos sont renseignées
            String tel   = fTel.getText().trim();
            String ville = fVille.getText().trim();

            if (!tel.isEmpty() && !ville.isEmpty()) {
                banque.ajouterClient(nom, email, tel, null, ville);
            } else if (!tel.isEmpty()) {
                banque.ajouterClient(nom, email, tel);
            } else {
                banque.ajouterClient(nom, email);
            }

            charger();
            JOptionPane.showMessageDialog(this, "Client ajouté avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            erreur(ex.getMessage());
        }
    }

    private void modifier() {
        Client c = clientSelectionne();
        if (c == null) return;

        // Préremplir le formulaire avec les données existantes
        JTextField fNom   = new JTextField(c.getNom(), 20);
        JTextField fTel   = new JTextField(c.getTelephone() != null ? c.getTelephone() : "", 15);
        JTextField fVille = new JTextField(c.getVille()     != null ? c.getVille()     : "", 15);

        JPanel form = new JPanel(new GridLayout(3, 2, 5, 5));
        form.add(new JLabel("Nom :"));       form.add(fNom);
        form.add(new JLabel("Téléphone :")); form.add(fTel);
        form.add(new JLabel("Ville :"));     form.add(fVille);

        int rep = JOptionPane.showConfirmDialog(this, form, "Modifier client",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (rep != JOptionPane.OK_OPTION) return;

        c.setNom(fNom.getText().trim());
        c.setTelephone(fTel.getText().trim());
        c.setVille(fVille.getText().trim());
        charger();
    }

    private void supprimer() {
        Client c = clientSelectionne();
        if (c == null) return;

        int rep = JOptionPane.showConfirmDialog(this,
            "Supprimer " + c.getNom() + " et tous ses comptes ?",
            "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (rep == JOptionPane.YES_OPTION) {
            banque.supprimerClient(c);
            charger();
        }
    }

    private void voirComptes() {
        Client c = clientSelectionne();
        if (c == null) return;

        var comptes = banque.getComptesDeUser(c);
        StringBuilder sb = new StringBuilder("Comptes de " + c.getNom() + " :\n\n");

        if (comptes.isEmpty()) {
            sb.append("  Aucun compte ouvert.");
        } else {
            for (var compte : comptes) {
                sb.append(String.format("  %s  [%s]  —  %.2f€%n",
                    compte.getType(), compte.getNumeroCompte(), compte.getSolde()));
            }
        }
        JOptionPane.showMessageDialog(this, sb.toString(), "Comptes du client", JOptionPane.INFORMATION_MESSAGE);
    }

    /** Retourne le client correspondant à la ligne sélectionnée dans le tableau */
    private Client clientSelectionne() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un client dans la liste.");
            return null;
        }
        String email = (String) modele.getValueAt(row, 2);
        return banque.chercherClientParEmail(email);
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

    private void erreur(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}
