import java.util.HashMap;
import java.util.Map;
import java.util.List;

interface Message {
    Object apply(OObjet unObjet, Object... arguments);
}

interface OObjet {

    <T> T message(String nom, Object... arguments);

    <T> T superMessage(String nom, Object... arguments);

    <T> T error(String cause);
}

interface ObjVLisp {
    OObjet getClasse(String nomDeClasse);
}

class RealiseObjVLisp implements ObjVLisp {

    private Map<String, OObjet> nosClasses;

    // Objet :: toString, nouveau
    // Classe (MetaClasse) :: nouveau, :accept, :message

    // Systeme :: afficher (une classe avec message)
    // Chaine
    // Entier

    @Override
    public OObjet getClasse(String nomDeClasse) {
        // si n'existe pas -> exception ?
        // error ? ce n'est pas un OObjet
        return nosClasses.get(nomDeClasse);
    }

    public RealiseObjVLisp() {
        // pour l'instant pour pouvoir commencer, meme sans metaclass ...
    }

    public RealiseObjVLisp(Map<String, OObjet> nosClasses) {
        // instancie avec des classes deja existantes
        // créer dans la Fabrique
        this.nosClasses = nosClasses;
    }

}

class ObjVLispFabrique {
    private ObjVLispFabrique() {
        // pas d'instanciation possible
    }

    static ObjVLisp nouveau() {
        Map<String, OObjet> nosClasses = new HashMap<String, OObjet>();

        Map<String, Object> mapClasse = new HashMap<String, Object>();
        mapClasse.put("nomClasse", "Classe");
        mapClasse.put("nomsAttributs",
                List.of("nomClasse", "nomsAttributs", "messages", "superClasse"));
        mapClasse.put("superClasse", null);
        mapClasse.put("messages", new HashMap<String, Message>());

        UnObjet metaClass = new UnObjet(null, mapClasse);
        metaClass.setInfo("classe", metaClass);
        mapClasse.put("classe", metaClass);

        UnObjet objetClass = new UnObjet(metaClass, mapClasse);
        objetClass.setInfo("nomClasse", "Objet");

        metaClass.setInfo("superClasse", objetClass);

        Message deuxPointsNouveau = (o, a) -> {
            UnObjet oo = (UnObjet) o;
            Map<String, Object> aa = (Map<String, Object>) a[0];
            Map<String, Object> map = new HashMap<String, Object>();
            for (String s : oo.getListAttributs()) // o.message("nomsAttributs") et cast ici
                map.put(s, aa.get(s));
            return new UnObjet(o, map);
        };
        metaClass.setMessage(":nouveau", deuxPointsNouveau);

        Message nouveau = (o, a) -> {
            UnObjet oo = (UnObjet) o;
            Map<String, Object> map = new HashMap<String, Object>();
            for (String s : oo.getListAttributs()) // o.message("nomsAttributs") et cast ici
                map.put(s, null);
            return new UnObjet(o, map);
        };
        // metaClass.setMessage("nouveau", nouveau);
        objetClass.setMessage("nouveau", nouveau);

        Message toString = (o, a) -> {
            // si c'est un objet terminal :
            // if (o.message("classe")) cast != metaClass
            StringBuilder ch = new StringBuilder();
            UnObjet oo = (UnObjet) o;

            ch.append("instance de la classe ");
            ch.append(oo.getClasseFormeTextuelle());
            ch.append("\n");

            // comment choper tous les attributs c'est un objet terminal ?
            // aller chercher dans la classe correspondante la liste des attributs
            // et get ici ?

            if (oo.estClasse()) {
                ch.append("classe ");
                ch.append(oo.getNomClasse());
                ch.append("extends ");
                ch.append(oo.getSuperClasseFormeTextuelle());
                ch.append("\n");

                // get list attributs et imprimer la list ,,,, ;
                // messages seulement les noms des messages
            }

            // pour tout ce qui apaprtient à la map :
            // nom = () (que ce soit list, int, map ...)

            return ch.toString();
        };
        objetClass.setMessage("toString", toString);

        // à écrire en ObjVLisp
        // OObjet systemClass = metaClass.message(":nouveau", Map.of("nomClasse",
        // "Systeme"));
        // entier
        // chaine
        nosClasses.put("Classe", metaClass);
        nosClasses.put("Objet", objetClass);
        // nosClasses.put("Systeme", systemClass);

        return new RealiseObjVLisp(nosClasses);
    }
}

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

class UnObjet implements OObjet {

    private Map<String, Object> info;

    public UnObjet(OObjet saClasse, Map<String, Object> sesAttributs) {
        info = new HashMap<String, Object>();
        info.put("classe", saClasse);
        info.put("classe", saClasse);
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
        // if (estClasse())
        return (String) info.get("nomClasse");
        // return "";
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

    /**
     * Modifie la map info.
     * 
     * @param nomAttribut    la clef dans la map
     * @param valeurAttribut la valeur dans la map
     */
    public void setInfo(String nomAttribut, Object valeurAttribut) {
        info.put(nomAttribut, valeurAttribut);
    }

    /**
     * Récupère la liste des attributs pour un objet qui est une classe.
     * Exemple: Point retourne List.of("x","y")
     * 
     * @return la liste des attributs de l'objet (représentant une classe)
     */
    public List<String> getListAttributs() {
        List<String> ret = (List<String>) info.get("nomsAttributs");
        if (!getNomClasse().equals("Classe"))
            ret.addAll(((UnObjet) info.get("superclasse")).getListAttributs());
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
