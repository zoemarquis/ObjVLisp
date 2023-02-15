import java.util.HashMap;
import java.util.Map;

class ObjVLispFabrique {
    private ObjVLispFabrique() {
        // pas d'instanciation possible
    }

    static ObjVLisp nouveau() {
        Map<String, OObjet> nosClasses = new HashMap<String, OObjet>();
        // crée la map avec entier, metaclass etc
        // pour l'instant rien, permet juste de créer

        OObjet metaClass = new UnObjet(null);
        OObjet objetClass = new UnObjet(metaClass);

        // à écrire en ObjVLisp
        OObjet systemClass = metaClass.message(":nouveau", Map.of("nomClasse", "Systeme"));

        nosClasses.put("Classe", metaClass);
        nosClasses.put("Objet", objetClass);
        nosClasses.put("Systeme", systemClass);

        return new RealiseObjVLisp(nosClasses);
    }
}