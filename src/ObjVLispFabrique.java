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
        UnObjet objetClass = new UnObjet(metaClass, Map.of("nomClasse", "Objet", "nomsAttributs",
                List.of("classe"), "superClasse", null, "messages", null));
        metaClass.setAttribut("superClass", objetClass);

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
        // objetClass.setMessage("nouveau", nouveau);

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