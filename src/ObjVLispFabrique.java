import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * La fabrique du langage ObjVLisp
 * 
 * @author Zoé Marquis
 * @author Enzo Nulli
 * @version 1.0
 */

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

        UnObjet metaClass = new UnObjet(mapMetaClasse);
        metaClass.setInfo("classe", metaClass);

        Map<String, Object> mapObjClasse = new HashMap<String, Object>();
        mapObjClasse.put("classe", metaClass);
        mapObjClasse.put("nomClasse", "Objet");
        mapObjClasse.put("nomsAttributs", List.of("classe"));
        mapObjClasse.put("superClasse", null);
        mapObjClasse.put("messages", new HashMap<String, Message>());

        UnObjet objetClass = new UnObjet(mapObjClasse);
        metaClass.setInfo("superClasse", objetClass);

        Message deuxPointsMessage = (o, a) -> {
            UnObjet oo = (UnObjet) o;
            oo.setMessage((String) a[0], (Message) a[1]);
            return null; // pour que le message soit déf (pas null (donc existe dans la map))
        };
        metaClass.setMessage(":message", deuxPointsMessage);

        Message deuxPointsNouveau = (o, a) -> {
            UnObjet oo = (UnObjet) o;
            Map<String, Object> aa = (Map<String, Object>) a[0];
            Map<String, Object> map = new HashMap<String, Object>();
            for (String s : oo.getListAllAttributs()) {
                Object valeur = aa.get(s);
                if (s.equals("classe")) {
                    map.put(s, o);
                } else if (s.equals("nomsAttributs") && valeur == null) {
                    map.put(s, new ArrayList<String>());
                } else if (s.equals("messages") && valeur == null) {
                    map.put(s, new HashMap<String, Message>());
                } else if (s.equals("superClasse") && valeur == null) {
                    map.put(s, objetClass);
                } else {
                    map.put(s, valeur);
                }
            }
            List<String> lesAttr = (List<String>) map.get("nomsAttributs");
            if (lesAttr != null) {
                Map<String, Object> msg = (Map<String, Object>) map.get("messages");
                for (String s : lesAttr) {
                    msg.put(s, (Message) (ob, arg) -> {
                        UnObjet oob = (UnObjet) ob;
                        return oob.getInfo(s);
                    });
                    msg.put(":" + s, (Message) (ob, arg) -> {
                        UnObjet oob = (UnObjet) ob;
                        oob.setInfo(s, arg[0]);
                        return null; // ?
                    });
                }
            }
            return new UnObjet(map);
        };
        metaClass.setMessage(":nouveau", deuxPointsNouveau);

        // ça c'est leouveau de metaclass enfaite, pas de Objet

        Message nouveau = (o, a) -> {
            UnObjet oo = (UnObjet) o;
            Map<String, Object> map = new HashMap<String, Object>();
            for (String s : oo.getListAllAttributs()) {
                if (s.equals("classe")) {
                    map.put(s, o);
                } else if (s.equals("nomsAttribus")) {
                    map.put(s, new ArrayList<String>());
                } else if (s.equals("messages")) {
                    map.put(s, new HashMap<String, Message>());
                } else if (s.equals("superClasse")) {
                    map.put(s, objetClass); // c'est le nouveau de metaclass ?
                } else {
                    map.put(s, null);
                }
            }
            return new UnObjet(map);
        };
        objetClass.setMessage("nouveau", nouveau);

        // faire une version de toString qui imprime toute la map, qu'on puisse check
        // que les objets n'ont que les bonnes méthodes
        Message toString = (o, a) -> {
            StringBuilder ch = new StringBuilder();
            UnObjet oo = (UnObjet) o;

            ch.append("instance de la classe ");
            ch.append(((UnObjet) oo.getInfo("classe")).getInfo("nomClasse"));
            ch.append("\n");

            if (oo.getInfo("nomClasse") != null) {
                ch.append("classe ");
                ch.append(oo.getInfo("nomClasse"));

                // si ce n'est pas Object, on met sa superClasse
                if (!oo.getInfo("nomClasse").equals("Objet")) {
                    ch.append(" extends ");
                    ch.append(((UnObjet) oo.getInfo("superClasse")).getInfo("nomClasse"));
                    ch.append("\n");
                }

                ch.append("ses attributs :\n");
                for (String s : oo.getListAllAttributs()) {
                    ch.append("\t" + s);
                }
                ch.append("\n");
                ch.append("ses messages :\n");
                for (String s : oo.getMapMessages().keySet()) {
                    ch.append("\t" + s);
                }
                ch.append("\n");
            } else {
                UnObjet mere = (UnObjet) oo.getInfo("classe");
                Object valeur = null;
                for (String s : mere.getListAllAttributs()) {
                    if (!s.equals("classe")) {
                        ch.append(s);
                        ch.append(" = ");
                        valeur = o.message(s); // ici faire to String aussi
                        ch.append((valeur != null) ? (valeur.toString()) : ("null"));
                        ch.append("\n");
                    }
                }
            }
            return ch.toString();
        };
        objetClass.setMessage("toString", toString);

        Message deuxPointsAccept = (o, a) -> {
            for (Object aa : a)
                o.message(":message", (String) aa, (Message) (ooo, aaa) -> null);
            return null; // pour que le message soit déf (pas null (donc existe dans la map))
        };
        metaClass.setMessage(":accept", deuxPointsAccept);

        nosClasses.put("Classe", metaClass);
        nosClasses.put("Objet", objetClass);

        return new RealiseObjVLisp(nosClasses);
    }
}
