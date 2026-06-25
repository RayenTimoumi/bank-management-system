package ui;

import gestionnaires.BanqueManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Fenetre principale de l'application.
 * Les donnees sont conservees uniquement en memoire dans des tableaux.
 */
public class FenetrePrincipale extends JFrame {

    private final BanqueManager banque;
    private JLabel lblStatut;

    public FenetrePrincipale(BanqueManager banque) {
        super("MaBank - Administration");
        this.banque = banque;
        initUI();
        rafraichirStatut();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(900, 620);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();

        JMenu menuComptes = new JMenu("Comptes");
        menuComptes.add(item("Gestion des comptes", e -> ouvrirComptes()));
        menuComptes.add(item("Transactions", e -> ouvrirTransactions()));
        menuBar.add(menuComptes);

        JMenu menuUsers = new JMenu("Utilisateurs");
        menuUsers.add(item("Clients", e -> ouvrirClients()));
        menuUsers.add(item("Employes", e -> ouvrirEmployes()));
        menuBar.add(menuUsers);

        JMenu menuRapports = new JMenu("Rapports");
        menuRapports.add(item("Statistiques", e -> ouvrirRapports()));
        menuBar.add(menuRapports);

        JMenu menuFichier = new JMenu("Fichier");
        menuFichier.add(item("Actualiser", e -> rafraichirStatut()));
        menuFichier.addSeparator();
        menuFichier.add(item("Quitter", e -> quitter()));
        menuBar.add(menuFichier);

        setJMenuBar(menuBar);

        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setBackground(new Color(20, 60, 120));
        header.setBorder(BorderFactory.createEmptyBorder(18, 0, 18, 0));

        JLabel lblTitre = new JLabel("MaBank - Espace Administration", SwingConstants.CENTER);
        lblTitre.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitre.setForeground(Color.WHITE);

        JLabel lblSous = new JLabel("Gestion complete des comptes, clients et employes", SwingConstants.CENTER);
        lblSous.setFont(new Font("Arial", Font.PLAIN, 13));
        lblSous.setForeground(new Color(180, 210, 255));

        header.add(lblTitre);
        header.add(lblSous);
        add(header, BorderLayout.NORTH);

        JPanel grille = new JPanel(new GridLayout(2, 3, 12, 12));
        grille.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        grille.setBackground(new Color(240, 244, 252));

        grille.add(tuile("Comptes", new Color(25, 100, 200), e -> ouvrirComptes()));
        grille.add(tuile("Transactions", new Color(20, 140, 80), e -> ouvrirTransactions()));
        grille.add(tuile("Rapports", new Color(160, 80, 10), e -> ouvrirRapports()));
        grille.add(tuile("Clients", new Color(110, 20, 160), e -> ouvrirClients()));
        grille.add(tuile("Employes", new Color(160, 30, 30), e -> ouvrirEmployes()));
        grille.add(tuile("Memoire", new Color(50, 50, 60), e -> rafraichirStatut()));

        add(grille, BorderLayout.CENTER);

        lblStatut = new JLabel("  Chargement...");
        lblStatut.setFont(new Font("Monospaced", Font.PLAIN, 12));
        lblStatut.setBorder(BorderFactory.createEtchedBorder());
        add(lblStatut, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { quitter(); }
        });
    }

    private JMenuItem item(String texte, java.awt.event.ActionListener action) {
        JMenuItem m = new JMenuItem(texte);
        m.addActionListener(action);
        return m;
    }

    private JButton tuile(String texte, Color couleur, java.awt.event.ActionListener action) {
        JButton btn = new JButton("<html><center>" + texte + "</center></html>");
        btn.setBackground(couleur);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(action);
        return btn;
    }

    public void rafraichirStatut() {
        lblStatut.setText(String.format(
            "  Clients : %d   |   Employes : %d   |   Comptes : %d   |   Solde total banque : %.2f EUR   |   Stockage : tableaux en memoire",
            banque.getClients().size(),
            banque.getEmployes().size(),
            banque.getComptes().size(),
            banque.calculerSoldeTotal()
        ));
    }

    private void ouvrirComptes() {
        new FenetreGestionComptes(this, banque).setVisible(true);
        rafraichirStatut();
    }

    private void ouvrirTransactions() {
        new FenetreTransactions(this, banque).setVisible(true);
        rafraichirStatut();
    }

    private void ouvrirClients() {
        new FenetreGestionClients(this, banque).setVisible(true);
        rafraichirStatut();
    }

    private void ouvrirEmployes() {
        new FenetreGestionEmployes(this, banque).setVisible(true);
        rafraichirStatut();
    }

    private void ouvrirRapports() {
        new FenetreRapports(this, banque).setVisible(true);
    }

    private void quitter() {
        int rep = JOptionPane.showConfirmDialog(
            this,
            "Quitter l'application ? Les donnees en memoire seront perdues.",
            "Quitter",
            JOptionPane.YES_NO_OPTION
        );
        if (rep == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
