import java.util.HashMap;
import java.util.Map;

import java.util.List;
import java.util.ArrayList;

class UnObjet implements OObjet {

    private Map<String, Object> info;

    public UnObjet(Map<String, Object> sesAttributs) {
        info = new HashMap<String, Object>();
        info.putAll(sesAttributs);
    }

    /**
     * Récupère le nom de la classe dont cet objet est l'instance.
     * Exemple : aPoint retourne "Point"
     * l'objet représentant la classe Point retourne "Classe"
     * 
     * @return une chaîne de caractères composée du nom de la classe dont cet objet
     *         est l'instance
     */
    public String getClasseFormeTextuelle() {
        return ((UnObjet) info.get("classe")).getNomClasse();
    }

    public OObjet getClasse() {
        return (OObjet) info.get("classe");
    }

    /**
     * Vérifie que cet objet est une classe (instance de Classe).
     * 
     * @return vrai si c'est une classe, faux si c'est un objet terminal
     */
    public Boolean estClasse() {
        return info.get("nomClasse") != null;
    }

    /**
     * Si cet objet est une classe, retourne la représentation textuelle de son nom.
     * Exemple : l'objet représentant la classe Point retourne "Point"
     * 
     * @return une chaîne de caractères représentant le nom donné à cette classe
     */
    public String getNomClasse() {
        return (String) info.get("nomClasse");
    }

    public UnObjet getSuperClasse() {
        return (UnObjet) info.get("superClasse");
    }

    /**
     * Récupère le nom de la superclasse dont cet objet est la sous classe.
     * Exemple : dans MonExemple : Crepe retourne "Dessert"
     * 
     * @return une chaîne de caractères composée du nom de la superclasse de cette
     *         classe
     */
    public String getSuperClasseFormeTextuelle() {
        if (estClasse() && info.get("superClasse") != null)
            return ((UnObjet) info.get("superClasse")).getNomClasse();
        return "";
    }

    /**
     * Modifie la map info.
     * 
     * @param nomAttribut    la clef dans la map
     * @param valeurAttribut la valeur dans la map
     */
    public void setInfo(String nomAttribut, Object valeurAttribut) {
        info.put(nomAttribut, valeurAttribut);
    }

    public Object getInfo(String nomAttribut) {
        return info.get(nomAttribut);
    }

    /**
     * Récupère la liste des attributs pour un objet qui est une classe.
     * Exemple: Point retourne List.of("x","y")
     * 
     * @return la liste des attributs de l'objet (représentant une classe)
     */
    public List<String> getListAllAttributs() {
        List<String> ret = new ArrayList<String>((List<String>) info.get("nomsAttributs"));
        if (((UnObjet) info.get("superClasse")) != null)
            ret.addAll(List.copyOf(((UnObjet) info.get("superClasse")).getListAllAttributs()));
        return ret;
    }

    /**
     * Récupère la map des messages pour un objet qui est une classe.
     * 
     * @return la map des messages de l'objet (représentant une classe)
     */
    public Map<String, Message> getMessages() {
        return (Map<String, Message>) info.get("messages");
    }

    private Message getMessage(String nom) {
        Message leMsg = null;
        UnObjet c = (UnObjet) getClasse();
        if (estClasse())
            leMsg = getMessages().get(nom);
        if (leMsg == null) {
            while (leMsg == null && c != null) {
                leMsg = c.getMessages().get(nom);
                c = c.getSuperClasse();
            }
        }
        return leMsg;
    }

    private Message getSuperMessage(String nom) {
        Message leMsg = null;
        UnObjet classe = (UnObjet) getClasse();
        if (estClasse())
            leMsg = getMessages().get(nom);

        if (leMsg == null) {
            while (leMsg == null && classe != null) {
                leMsg = classe.getMessages().get(nom);
                classe = classe.getSuperClasse();
            }
        }
        if (leMsg == null || classe == null)
            return null;
        leMsg = null;
        while (leMsg == null && classe != null) {
            leMsg = classe.getMessages().get(nom);
            classe = classe.getSuperClasse();
        }
        return leMsg;
    }

    /**
     * Modifie la map des messages pour un objet qui est une classe.
     * 
     * @param nomMsg la clef du message à ajouter
     * @param leMsg  le message correspondant
     */
    public void setMessage(String nomMsg, Message leMsg) {
        if (estClasse())
            getMessages().put(nomMsg, leMsg);
    }

    @Override
    public <T> T message(String nom, Object... arguments) { // comment faire pour getter et setter ?
        Message leMsg = getMessage(nom);
        if (leMsg == null)
            return error("Ce message c'est pas défini pour cet objet.");
        return (T) leMsg.apply(this, arguments);
    }

    @Override
    public <T> T superMessage(String nom, Object... arguments) {
        Message leMsg = getSuperMessage(nom);
        if (leMsg == null)
            return error("Il n'y a pas de supermessage défini pour cet objet.");
        return (T) leMsg.apply(this, arguments);
    }

    @Override
    public <T> T error(String cause) {
        return (T) new Error(cause);
    }

}
