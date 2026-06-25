package ui;

import gestionnaires.BanqueManager;
import modeles.Employee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Fenêtre d'administration des employés.
 * Le rôle est un simple attribut texte de l'employé.
 */
public class FenetreGestionEmployes extends JDialog {

    private final BanqueManager   banque;
    private DefaultTableModel     modele;
    private JTable                table;

    private static final String[] COLONNES = {"ID", "Nom", "Email", "Rôle", "Salaire (€)", "Bonus (€)"};

    public FenetreGestionEmployes(Frame parent, BanqueManager banque) {
        super(parent, "Gestion des employés", true);
        this.banque = banque;
        initUI();
        charger();
        setSize(750, 420);
        setLocationRelativeTo(parent);
    }

    private void initUI() {
        setLayout(new BorderLayout(6, 6));
        add(enTete("🧑‍💼  Employés", new Color(160, 30, 30)), BorderLayout.NORTH);

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
        boutons.add(btn("+ Ajouter",   new Color(0, 130, 60),  e -> ajouter()));
        boutons.add(btn("✏ Modifier",  new Color(0, 100, 180), e -> modifier()));
        boutons.add(btn("✖ Supprimer", new Color(180, 30, 30), e -> supprimer()));
        boutons.add(btn("💳 Comptes",  new Color(80, 80, 0),   e -> voirComptes()));
        add(boutons, BorderLayout.SOUTH);
    }

    private void charger() {
        modele.setRowCount(0);
        for (Employee e : banque.getEmployes()) {
            modele.addRow(new Object[]{
                e.getId(),
                e.getNom(),
                e.getEmail(),
                e.getRole(),
                String.format("%.2f", e.getSalaire()),
                String.format("%.2f", e.calculerBonus())
            });
        }
    }

    private void ajouter() {
        JTextField fNom     = new JTextField(20);
        JTextField fEmail   = new JTextField(20);
        JTextField fRole    = new JTextField(15);
        JTextField fSalaire = new JTextField("2000", 10);

        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        form.add(new JLabel("Nom * :"));        form.add(fNom);
        form.add(new JLabel("Email * :"));      form.add(fEmail);
        form.add(new JLabel("Rôle * :"));       form.add(fRole);
        form.add(new JLabel("Salaire (€) :"));  form.add(fSalaire);

        int rep = JOptionPane.showConfirmDialog(this, form, "Nouvel employé",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (rep != JOptionPane.OK_OPTION) return;

        String nom   = fNom.getText().trim();
        String email = fEmail.getText().trim();
        String role  = fRole.getText().trim();

        if (nom.isEmpty() || email.isEmpty() || role.isEmpty()) {
            erreur("Nom, email et rôle sont obligatoires.");
            return;
        }

        double salaire;
        try {
            salaire = Double.parseDouble(fSalaire.getText().trim());
        } catch (NumberFormatException e) {
            erreur("Salaire invalide.");
            return;
        }

        try {
            banque.ajouterEmployee(new Employee(nom, email, role, salaire));
            charger();
            JOptionPane.showMessageDialog(this, "Employé ajouté.", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            erreur(ex.getMessage());
        }
    }

    private void modifier() {
        Employee e = employeSelectionne();
        if (e == null) return;

        JTextField fNom     = new JTextField(e.getNom(), 20);
        JTextField fRole    = new JTextField(e.getRole(), 15);
        JTextField fSalaire = new JTextField(String.valueOf(e.getSalaire()), 10);

        JPanel form = new JPanel(new GridLayout(3, 2, 5, 5));
        form.add(new JLabel("Nom :"));        form.add(fNom);
        form.add(new JLabel("Rôle :"));       form.add(fRole);
        form.add(new JLabel("Salaire (€) :")); form.add(fSalaire);

        int rep = JOptionPane.showConfirmDialog(this, form, "Modifier employé",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (rep != JOptionPane.OK_OPTION) return;

        try {
            e.setNom(fNom.getText().trim());
            e.setRole(fRole.getText().trim());
            e.setSalaire(Double.parseDouble(fSalaire.getText().trim()));
            charger();
        } catch (NumberFormatException ex) {
            erreur("Salaire invalide.");
        }
    }

    private void supprimer() {
        Employee e = employeSelectionne();
        if (e == null) return;

        int rep = JOptionPane.showConfirmDialog(this,
            "Supprimer l'employé " + e.getNom() + " ?",
            "Confirmation", JOptionPane.YES_NO_OPTION);

        if (rep == JOptionPane.YES_OPTION) {
            banque.supprimerEmployee(e.getId());
            charger();
        }
    }

    private void voirComptes() {
        Employee e = employeSelectionne();
        if (e == null) return;

        var comptes = banque.getComptesDeUser(e);
        StringBuilder sb = new StringBuilder("Comptes de " + e.getNom() + " :\n\n");

        if (comptes.isEmpty()) {
            sb.append("  Aucun compte ouvert.");
        } else {
            for (var c : comptes) {
                sb.append(String.format("  %s  [%s]  —  %.2f€%n",
                    c.getType(), c.getNumeroCompte(), c.getSolde()));
            }
        }
        JOptionPane.showMessageDialog(this, sb.toString(), "Comptes de l'employé", JOptionPane.INFORMATION_MESSAGE);
    }

    /** Retourne l'employé de la ligne sélectionnée */
    private Employee employeSelectionne() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un employé dans la liste.");
            return null;
        }
        String id = (String) modele.getValueAt(row, 0);
        return banque.chercherEmployeeParId(id);
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
