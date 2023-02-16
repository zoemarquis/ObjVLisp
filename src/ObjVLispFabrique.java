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

        Message deuxPointsNouveau = (o, a) -> {
            UnObjet oo = (UnObjet) o;
            Map<String, Object> aa = (Map<String, Object>) a[0];
            Map<String, Object> map = new HashMap<String, Object>();
            for (String s : oo.getListAttributs()) // o.message("nomsAttributs") et cast ici
                map.put(s, aa.get(s));
            return new UnObjet(o, map);
        };

        Message toString = (o, a) -> {
            // si c'est un objet terminal :
            // if (o.message("classe")) cast != metaClass
            StringBuilder ch = new StringBuilder();
            String nomDeClasse = o.message("nomClasse");
            if (nomDeClasse != null) {
                ch.append("Classe ");
                ch.append(nomDeClasse);
                ch.append("\n");
            } else {
                ch.append("Instance de la classe");
                OObjet classeMere = (OObjet) o.message("classe");
                ch.append((String) classeMere.message("nomClasse"));
                ch.append("\n");
            }
            // ajouter ses attributs etc toute sa map
            return ch.toString();
        };

        // ecrire ici nouveau
        // ecrire objetC
        UnObjet objetClass = new UnObjet(metaClass, Map.of("nomClasse", "Objet", "nomsAttributs",
                List.of("classe"), "superClasse", null, "messages", Map.of("toString", toString, "nouveau", nouveau)));

        metaClass.setAttribut("superClass", objetClass);

        // UnObjet objetClass = metaClass.message(":nouveau", Map.of("nomClasse",
        // "Objet", "nomsAttributs", List.of("classe"), "superClasse", null, "messages",
        // null /*
        // toString */);

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