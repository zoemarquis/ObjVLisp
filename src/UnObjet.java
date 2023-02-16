import java.util.HashMap;
import java.util.Map;
import java.util.List;

/*
     * un objet a toujours :
     * classe
     * objet terminal : ses attributs intancier ou non
     * 
     * une classe a toujours
     * (classe = Classe) (donc il faut connaitre metaclass de ObjVLisp ?)
     * nom
     * liste d'attributs (pour instancier des objets terminaux) dans une liste ?
     * map de messages
     * superclasse
     * 
     * déclarer des attributs de classe : on verra + tard
     */

public class UnObjet implements OObjet {

    private Map<String, Object> info;

    public UnObjet(OObjet saClasse, Map<String, Object> sesAttributs) {
        info = new HashMap<String, Object>();
        info.put("classe", saClasse);
        info.put("nomAttributs", sesAttributs);
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

    /**
     * Vérifie que cet objet est une classe (instance de Classe).
     * 
     * @return vrai si c'est une classe, faux si c'est un objet terminal
     */
    public Boolean estClasse() {
        return getClasseFormeTextuelle().equals("Classe");
    }

    /**
     * Si cet objet est une classe, retourne la représentation textuelle de son nom.
     * Exemple : l'objet représentant la classe Point retourne "Point"
     * 
     * @return une chaîne de caractères représentant le nom donné à cette classe
     */
    public String getNomClasse() {
        if (estClasse())
            return (String) info.get("nomClasse");
        return "";
    }

    /**
     * Récupère le nom de la superclasse dont cet objet est la sous classe.
     * Exemple : dans MonExemple : Crepe retourne "Dessert"
     * 
     * @return une chaîne de caractères composée du nom de la superclasse de cette
     *         classe
     */
    public String getSuperClasseFormeTextuelle() {
        if (estClasse())
            return ((UnObjet) info.get("superClasse")).getNomClasse();
        return "";
    }

    public void setAttribut(String nomAttribut, Object valeurAttribut) {
        info.put(nomAttribut, valeurAttribut);
    }

    public List<String> getListAttributs() {
        return (List<String>) info.get("nomsAttributs");
    }

    public Map<String, Message> getMessages() {
        // si pas de messages, objet terminal, return null
        return (Map<String, Message>) info.get("messages");
    }

    public void setMessage(String nomMsg, Message leMsg) {
        // seulement si c'est une instance de Classe
        // si objet terminal, error
        getMessages().put(nomMsg, leMsg);
    }

    @Override
    public <T> T message(String nom, Object... arguments) { // comment faire pour getter et setter ?
        Message leMsg = null;
        if (!estClasse()) { // si c'est un objet terminal
            leMsg = ((OObjet) info.get("classe")).message(nom, arguments);
        } else { // sinon c'est une classe
            leMsg = getMessages().get(nom); // le message est dans cette classe
            if (leMsg == null) // le message est dans une superclasse
                leMsg = ((OObjet) info.get("superclasse")).message(nom, arguments);
            // s'il n'a pas de superclasse -> lancer la methode error
        }
        leMsg.apply(this, arguments);
        return (T) this; // ? type T
    }

    @Override
    public <T> T superMessage(String nom, Object... arguments) {
        return (T) this;
    }

    @Override
    public <T> T error(String cause) {
        return (T) this;
    }

}
