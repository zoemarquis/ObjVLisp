import java.util.Map;

class RealiseObjVLisp implements ObjVLisp {

    private Map<String, OObjet> nosClasses;

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
