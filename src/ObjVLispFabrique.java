import java.util.HashMap;
import java.util.Map;
import java.util.List;

class ObjVLispFabrique {
    private ObjVLispFabrique() {
        // pas d'instanciation possible
    }

    static ObjVLisp nouveau() {
        Map<String, OObjet> nosClasses = new HashMap<String, OObjet>();

        UnObjet metaClass = new UnObjet(null, Map.of("nomClasse", "Classe", "nomsAttributs",
                List.of("nomClasse", "nomsAttributs", "messages", "superClasse"), "superClasse", null, "messages",
                null));
        metaClass.setAttribut("classe", metaClass);

        Message nouveau = (o, a) -> {
            UnObjet oo = (UnObjet) o;
            Map<String, Object> map = new HashMap<String, Object>();
            for (String s : oo.getListAttributs()) // o.message("nomsAttributs") et cast ici
                map.put(s, null);
            return new UnObjet(o, map);
        };
        // recuperer la liste du la classe et créer une map avec tou à null
        Message toString;
        Message deuxPointsNouveau;

        // ecrire ici nouveau
        // ecrire objetC
        UnObjet objetClass = new UnObjet(metaClass, Map.of("nomClasse", "Objet", "nomsAttributs",
                List.of("classe"), "superClasse", null, "messages", null /* toString */));
        metaClass.setAttribut("superClass", objetClass);

        // ecrire ici nouveau
        // et :nouveau

        // toString :
        // Je suis une instance de : (nom classe)
        // Mes attributs sont :
        // si je suis une classe, alors mon nom de classe
        // si superclasse : (nom super classe)
        // si mes messages : alors afficher liste string

        // ajouter a metaclass dans ses attributs les attributs liés à une classe :
        // nomclasse, nomsAttributs, message, superclasse

        // ajouter à metaclass : nouveau, :nouveau, getter et setter ?
        // nouveau -> créer unObjet avec classe = metaclass, et ses attributs de classe
        // tous à nul

        // à écrire en ObjVLisp
        OObjet systemClass = metaClass.message(":nouveau", Map.of("nomClasse", "Systeme"));
        // entier
        // chaine

        nosClasses.put("Classe", metaClass);
        nosClasses.put("Objet", objetClass);
        nosClasses.put("Systeme", systemClass);

        return new RealiseObjVLisp(nosClasses);
    }
}