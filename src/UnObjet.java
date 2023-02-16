import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class UnObjet implements OObjet {

    private Map<String, Object> info;

    /*
     * un objet a toujours :
     * classe :
     * attributs : aussi une map
     * 
     * une classe a toujours
     * (classe = Classe) (donc il faut connaitre metaclass de ObjVLisp ?)
     * dans ses attributs : aussi une map
     * nom
     * liste d'attributs (pour instancier des objets terminaux) dans une liste ?
     * map de messages
     * superclasse
     * 
     * déclarer des attributs de classe : on verra + tard
     */
    // quand on appelle un nouveau objet, il doit forcement connaitre sa super
    // classe et de quelle classe il est l'instance

    private Boolean estClasse() {
        if (info.get("nomClasse") != null)
            return true;
        return false;
    }

    public UnObjet(OObjet saClasse) {
        // si c'est une classe, alors saClasse = metaClasse (on peut regarder le nom
        // (string) de la classe pour savoir)
        // sinon
        info = new HashMap<String, Object>();
        info.put("classe", saClasse);
    }

    public UnObjet(OObjet saClasse, Map<String, Object> sesAttributs) {
        // si c'est un objet terminal, une liste plutot ? (nouveau)
        // si :nouveau, instancier en associant ?
        this(saClasse);
        info.put("nomAttributs", sesAttributs);

        // nomClasse dont il est l'instance ? seulement si c'est une classe
        // dedans il y a nomClasse ?
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
            leMsg = ((OObjet) info.get("superclasse")).message(nom, arguments);
        } else {
            leMsg = getMessages().get(nom); // si c'est une classe et que le msg est ici
            if (leMsg == null)
                leMsg = ((OObjet) info.get("superclasse")).message(nom, arguments);
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
