import java.util.Map;

public class RealiseObjVLisp implements ObjVLisp {

    private Map<String, OObjet> nosClasses;
    // contient (meta)class, enier, systeme, chaine

    @Override
    public OObjet getClasse(String nomDeClasse) {
        // si n'existe pas -> exception ?
        // error ? ce n'est pas un OObjet
        return nosClasses.get("Point");
    }

    public RealiseObjVLisp() {
        // pour l'instant pour pouvoir commencer, meme sans metaclass ...
    }

    public RealiseObjVLisp(Map<String, OObjet> nosClasses) {
        // instancie avec des classes deja existantes
        // créer dans la Fabrique
    }

}
