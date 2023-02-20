import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

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
                    // map.put(":" + s, setter);
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
                "Systeme"));
        Message afficher = (o, a) -> {
            OObjet aa = (OObjet) a[0];
            String chaine = aa.message("toString");
            System.out.println(chaine);
            return chaine;
        };
        systemClass.setMessage("afficher", afficher);

        // entier
        // chaine

        nosClasses.put("Classe", metaClass);
        nosClasses.put("Objet", objetClass);
        nosClasses.put("Systeme", systemClass);

        return new RealiseObjVLisp(nosClasses);
    }
}