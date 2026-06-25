package interfaces;
public interface Authentifiable {
    boolean authentifier(String motDePasse);
    void changerMotDePasse(String ancien, String nouveau) throws Exception;
    boolean estVerrouille();
}
