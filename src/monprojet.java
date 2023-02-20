import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

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

        Map<String, Object> mapMetaClasse = new HashMap<String, Object>();
        mapMetaClasse.put("nomClasse", "Classe");
        mapMetaClasse.put("nomsAttributs", List.of("nomClasse", "nomsAttributs", "messages", "superClasse"));
        mapMetaClasse.put("superClasse", null);
        mapMetaClasse.put("messages", new HashMap<String, Message>());

        Map<String, Object> mapObjClasse = new HashMap<String, Object>();
        mapObjClasse.put("nomClasse", "Objet");
        mapObjClasse.put("nomsAttributs", List.of("classe"));
        mapObjClasse.put("superClasse", null);
        mapObjClasse.put("messages", new HashMap<String, Message>());

        UnObjet metaClass = new UnObjet(null, mapMetaClasse);
        metaClass.setInfo("classe", metaClass);

        UnObjet objetClass = new UnObjet(metaClass, mapObjClasse);
        metaClass.setInfo("superClasse", objetClass);

        Message setter = (o, a) -> {
            // si il ne l'accepte pas?
            UnObjet oo = (UnObjet) o;
            oo.setInfo((String) a[0], a[1]);
            return a[1]; // que doit il retourner ?
        };

        Message deuxPointsNouveau = (o, a) -> {
            UnObjet oo = (UnObjet) o;
            Map<String, Object> aa = (Map<String, Object>) a[0];
            Map<String, Object> map = new HashMap<String, Object>();
            for (String s : oo.getListAttributs()) {
                Object valeur = aa.get(s);
                if (s.equals("nomsAttribus") && valeur == null) {
                    map.put(s, new ArrayList<String>());
                } else if (s.equals("messages") && valeur == null) {
                    map.put(s, new HashMap<String, Message>());
                } else if (s.equals("superClasse") && valeur == null) {
                    map.put(s, objetClass);
                } else {
                    map.put(s, valeur); // getter
                    // ajouter getter et setter
                    map.put(":" + s, setter);
                }
            }
            return new UnObjet(o, map);
        };
        metaClass.setMessage(":nouveau", deuxPointsNouveau);

        // ça c'est leouveau de metaclass enfaite, pas de Objet

        Message nouveau = (o, a) -> {
            UnObjet oo = (UnObjet) o;
            Map<String, Object> map = new HashMap<String, Object>();
            for (String s : oo.getListAttributs()) {
                if (s.equals("nomsAttribus")) {
                    map.put(s, new ArrayList<String>());
                } else if (s.equals("messages")) {
                    map.put(s, new HashMap<String, Message>());
                } else if (s.equals("superClasse")) {
                    map.put(s, objetClass);
                } else {
                    map.put(s, null);
                }
            }
            return new UnObjet(o, map);
        };
        objetClass.setMessage("nouveau", nouveau);

        // faire une version de toString qui imprime toute la map, qu'on puisse check
        // que les objets n'ont que les bonnes méthodes
        Message toString = (o, a) -> {
            StringBuilder ch = new StringBuilder();
            UnObjet oo = (UnObjet) o;

            ch.append("instance de la classe ");
            ch.append(oo.getClasseFormeTextuelle());
            ch.append("\n");

            if (oo.estClasse()) {
                ch.append("classe ");
                ch.append(oo.getNomClasse());
                ch.append(" extends ");
                ch.append(oo.getSuperClasseFormeTextuelle());
                ch.append("\n");

                ch.append("ses attributs :\n");
                for (String s : oo.getListAttributs()) {
                    ch.append("\t" + s);
                }
                ch.append("\n");
                ch.append("ses messages :\n");
                for (String s : oo.getMessages().keySet()) {
                    ch.append("\t" + s);
                }
                ch.append("\n");

            } else {

                // récupérer la liste de la classe mere
                UnObjet mere = (UnObjet) oo.getClasse();
                // pour chaque attribut de sa liste d'attributs
                // pour chaque getter , afficher nom = valeur
                for (String s : mere.getListAttributs()) {
                    ch.append(s);
                    ch.append(" = ");
                    ch.append((String) oo.message(s));
                }

            }

            return ch.toString();

        };
        objetClass.setMessage("toString", toString);

        UnObjet systemClass = metaClass.message(":nouveau", Map.of("nomClasse",
                "Systeme", "messages", Map.of("afficher",
                        ((Message) (o, a) -> {
                            OObjet aa = (OObjet) a[0];
                            String chaine = aa.message("toString");
                            System.out.println(chaine);
                            return chaine;
                        }))));

        // entier
        // chaine

        nosClasses.put("Classe", metaClass);
        nosClasses.put("Objet", objetClass);
        nosClasses.put("Systeme", systemClass);

        return new RealiseObjVLisp(nosClasses);
    }
}

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

    public OObjet getClasse() {
        return (OObjet) info.get("classe");
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

    /**
     * Récupère la liste des attributs pour un objet qui est une classe.
     * Exemple: Point retourne List.of("x","y")
     * 
     * @return la liste des attributs de l'objet (représentant une classe)
     */
    public List<String> getListAttributs() {
        List<String> ret = (List<String>) info.get("nomsAttributs");
        /*
         * if (((UnObjet) info.get("superClasse")) != null)
         * ret.addAll(((UnObjet) info.get("superClasse")).getListAttributs());
         */
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

    public Message getMessage(String nom) {
        Message leMsg = null;
        UnObjet superC = ((UnObjet) info.get("classe"));
        if (estClasse())
            leMsg = getMessages().get(nom);
        if (leMsg == null) // sinon boucle infernale
            leMsg = superC.getMessages().get(nom);
        if (leMsg == null) { // verif que superClass pas a null
            superC = superC.getSuperClasse();
            while (leMsg == null && superC != null) {
                leMsg = superC.getMessages().get(nom);
                superC = superC.getSuperClasse();
            }
        }
        // si null -> error
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
        if (leMsg == null) {
            // error;
            System.out.println("Message introuvable");
        }
        return (T) leMsg.apply(this, arguments);
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
