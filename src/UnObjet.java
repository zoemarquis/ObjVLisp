import java.util.HashMap;
import java.util.Map;

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

    public UnObjet(OObjet saClasse) {
        // si c'est une classe, alors saClasse = metaClasse (on peut regarder le nom
        // (string) de la classe pour savoir)
        // sinon
        info = new HashMap<String, Object>();
        info.put("classe", saClasse);
    }

    // private static final OObjet metaClass = Map.of("classe",OO);

    public UnObjet(OObjet saClasse, Map<String, Object> sesAttributs) {
        // si c'est un objet terminal, une liste plutot ? (nouveau)
        // si :nouveau, instancier en associant ?
        this(saClasse);
        info.put("nomAttributs", sesAttributs);
        // dedans il y a nomClasse ?

    }

    @Override
    public <T> T message(String nom, Object... arguments) {
        return (T) this;
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
