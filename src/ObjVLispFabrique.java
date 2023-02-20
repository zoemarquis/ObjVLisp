import java.util.HashMap;
import java.util.Map;
import java.util.List;

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

        // faire une version de toString qui imprime toute la map, qu'on puisse check
        // que les objets n'ont que les bonnes méthodes
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

        // à écrire en ObjVLisp plus tard
        // entier
        // chaine

        UnObjet systemClass = metaClass.message(":nouveau", Map.of("nomClasse", "Systeme"));
        Message afficher = (o, a) -> {
            String chaine = o.message("toString");
            System.out.println(chaine);
            return chaine;
        };
        systemClass.setMessage("afficher", afficher);

        nosClasses.put("Classe", metaClass);
        nosClasses.put("Objet", objetClass);
        nosClasses.put("Systeme", systemClass);

        return new RealiseObjVLisp(nosClasses);
    }
}