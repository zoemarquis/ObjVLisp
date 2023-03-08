import java.util.List;
import java.util.Map;

/**
 * Classe constituée d'exemples qui modélisent le fonctionnement d'ObjVLisp
 * 
 * @author Zoé Marquis
 * @author Enzo Nulli
 * @version 1.0
 */
public class MonExemple {

    /**
     * L'exemple de Zoé Marquis.
     */
    public static void exempleMarquis() {
        ObjVLisp obj = ObjVLispFabrique.nouveau();
        OObjet metaClass = obj.getClasse("Classe");
        OObjet systemClass = obj.getClasse("Systeme");

        OObjet integerClass = obj.getClasse("Entier");
        OObjet un = integerClass.message(":nouveau", 1);
        OObjet deux = integerClass.message(":nouveau", 2);
        OObjet trois = integerClass.message(":nouveau", 3);
        OObjet quatre = integerClass.message(":nouveau", 4);

        OObjet douze = integerClass.message(":nouveau", 12);
        OObjet vingtCinq = integerClass.message(":nouveau", 25);

        OObjet trente = integerClass.message(":nouveau", 30);
        OObjet trenteTrois = integerClass.message(":nouveau", 33);
        OObjet trenteNeuf = integerClass.message(":nouveau", 39);
        OObjet quarante = integerClass.message(":nouveau", 40);

        OObjet centVingtCinq = integerClass.message(":nouveau", 125);
        OObjet centQuatreVingt = integerClass.message(":nouveau", 180);
        OObjet deuxCent = integerClass.message(":nouveau", 200);

        OObjet classDessert = metaClass.message(":nouveau", Map.of("nomClasse", "Dessert",
                "nomsAttributs",
                List.of("sucre", "farine", "oeuf", "lait", "beurre")));
        classDessert.message(":message", "tempsCuisson", (Message) (o,
                a) -> ((OObjet) o.message("oeuf")).message("*", (Object) douze));
        // 12 minutes de cuisson par oeuf

        OObjet classCrepe = metaClass.message(":nouveau", Map.of("nomClasse", "Crepe",
                "superClasse", classDessert, "nomsAttributs", List.of("rhum")));
        classCrepe.message(":message", "tempsCuisson", (Message) (o, a) -> deux);

        OObjet classGateau = metaClass.message(":nouveau", Map.of("nomClasse", "Gateau",
                "superClasse", classDessert, "nomsAttributs", List.of("levure")));
        classGateau.message(":message", "degreCuisson",
                (Message) (o, a) -> centQuatreVingt);

        OObjet classMarbre = metaClass.message(":nouveau", Map.of("nomClasse", "Marbre",
                "superClasse", classGateau, "nomsAttributs", List.of("chocolat")));
        classMarbre.message(":message", "tempsCuisson",
                (Message) (o, a) -> ((OObjet) o.superMessage("tempsCuisson"))
                        .message("+", (Object) trois));

        OObjet mesCrepes = classCrepe.message("nouveau");
        mesCrepes.message(":sucre", trente);
        mesCrepes.message(":farine", deuxCent);
        mesCrepes.message(":oeuf", deux);
        mesCrepes.message(":lait", quarante);
        mesCrepes.message(":beurre", trenteTrois);
        mesCrepes.message(":rhum", quatre);

        OObjet monMarbre = classMarbre.message("nouveau");
        monMarbre.message(":sucre", deuxCent);
        monMarbre.message(":farine", deuxCent);
        monMarbre.message(":oeuf", trois);
        monMarbre.message(":lait", douze);
        monMarbre.message(":beurre", centVingtCinq);
        monMarbre.message(":levure", un);
        monMarbre.message(":chocolat", vingtCinq);

        Object tempsCrepe = mesCrepes.message("tempsCuisson");
        assert deux.equals(tempsCrepe);
        Object tempsMarbre = monMarbre.message("tempsCuisson");
        assert trenteNeuf.equals(tempsMarbre);

        systemClass.message("afficher", (Object) mesCrepes.message("tempsCuisson"));
        systemClass.message("afficher", (Object) monMarbre.message("degreCuisson"));
        systemClass.message("afficher", (Object) monMarbre.message("tempsCuisson"));
    }

    /**
     * L'exemple d'Enzo Nulli.
     */
    public static void exempleNulli() {
        ObjVLisp obj = ObjVLispFabrique.nouveau();
        OObjet metaClass = obj.getClasse("Classe");
        OObjet systemClass = obj.getClasse("Systeme");

        OObjet integerClass = obj.getClasse("Entier");
        OObjet StringClass = obj.getClasse("Chaine");

        OObjet un = integerClass.message(":nouveau", 1);
        OObjet mille = integerClass.message(":nouveau", 1000);

        OObjet nom_enzoString = StringClass.message(":nouveau", "Enzo");
        OObjet maleString = StringClass.message(":nouveau", "Mâle");
        OObjet petitString = StringClass.message(":nouveau", "Petit");
        OObjet poidsString = StringClass.message(":nouveau", "30t");
        OObjet marronString = StringClass.message(":nouveau", "Marron");
        OObjet v_volInteger = integerClass.message(":nouveau", 1000);
        // Vitesse en km/h (1000 c'est absurde mais c'est juste pour l'assert a la fin
        // sinon il faudrait un double vu qu'il y a des décimales)

        OObjet dinosaureClass = metaClass.message(":nouveau", Map.of("nomClasse", "Dinosaure", "nomsAttributs",
                List.of("nom", "sexe", "taille", "poids", "couleur")));

        OObjet pterosaureClass = metaClass.message(":nouveau", Map.of("nomClasse", "Pterosaure",
                "superClasse", dinosaureClass, "nomsAttributs", List.of("vitesse_vol")));

        OObjet enzoLeZigoto = pterosaureClass.message("nouveau");
        enzoLeZigoto.message(":nom", nom_enzoString);
        enzoLeZigoto.message(":sexe", maleString);
        enzoLeZigoto.message(":taille", petitString);
        enzoLeZigoto.message(":poids", poidsString);
        enzoLeZigoto.message(":couleur", marronString);
        enzoLeZigoto.message(":vitesse_vol", v_volInteger);

        pterosaureClass.message(":message", "parcours1000km", (Message) (o, a) -> ((OObjet) mille
                .message("/", (Object) o.message("vitesse_vol"))));
        // Temps en H pour parcourir 1000km

        Object vitesseEnzo = enzoLeZigoto.message("parcours1000km");
        assert un.equals(vitesseEnzo);

        systemClass.message("afficher", (Object) enzoLeZigoto.message("parcours1000km"));

    }

    /**
     * L'exemple de DLB.
     */
    public static void exempleDLB() {
        ObjVLisp obj = ObjVLispFabrique.nouveau();
        OObjet metaClass = obj.getClasse("Classe");
        // La classe Entier permet de manipuler des entiers OObjet
        OObjet integerClass = obj.getClasse("Entier");
        OObjet dix = integerClass.message(":nouveau", 10);
        OObjet vingt = integerClass.message(":nouveau", 20);
        OObjet cinquante = integerClass.message(":nouveau", 50);
        OObjet cent = integerClass.message(":nouveau", 100);

        OObjet classA = metaClass.message(":nouveau", Map.of("nomClasse", "A"));
        classA.message(":message", "foo", (Message) (o, a) -> dix);
        classA.message(":message", "bar", (Message) (o, a) -> o.message("foo"));
        OObjet classB = metaClass.message(":nouveau", Map.of("nomClasse", "B", "superClasse", classA));
        classB.message(":message", "bar", (Message) (o, a) -> ((OObjet) o.superMessage("bar"))
                .message("+", (Object) o.message("foo")));
        OObjet classC = metaClass.message(":nouveau", Map.of("nomClasse", "C", "superClasse", classB));
        classC.message(":message", "foo", (Message) (i, a) -> cinquante);
        OObjet anA = classA.message("nouveau");
        Object resA = anA.message("bar");
        assert dix.equals(resA);
        OObjet aB = classB.message("nouveau");
        Object resB = aB.message("bar");
        assert vingt.equals(resB);
        OObjet aC = classC.message("nouveau");
        Object resC = aC.message("bar");
        assert cent.equals(resC);

    }

    /**
     * L'exemple de Nicolas Henocque.
     */
    public static void groupeANicolasHenocque() {
        /* Base */
        ObjVLisp obj = ObjVLispFabrique.nouveau(); // On définie notre langage

        OObjet metaClass = obj.getClasse("Classe"); // Méta-classe
        OObjet systemClass = obj.getClasse("Systeme"); // Classe induite Système
        OObjet integerClass = obj.getClasse("Entier"); // Classe induite Entier

        /* Debug */
        systemClass.message("afficher", (Object) integerClass); // On vérifie que la classe Entier
                                                                // est initialisée

        /* Objets (Entiers) */
        OObjet zero = integerClass.message(":nouveau", 0);
        OObjet un = integerClass.message(":nouveau", 1);
        OObjet deux = integerClass.message(":nouveau", 2);
        OObjet trois = integerClass.message(":nouveau", 3);
        OObjet quatre = integerClass.message(":nouveau", 4);
        OObjet cinq = integerClass.message(":nouveau", 5);
        OObjet six = integerClass.message(":nouveau", 6);
        OObjet neuf = integerClass.message(":nouveau", 9);

        /* Mes propres classes */
        // Point (x, y)
        OObjet pointClass = metaClass.message(":nouveau",
                Map.of("nomClasse", "Point", "nomsAttributs", List.of("x", "y")));

        // Forme
        OObjet formeClass = metaClass.message(":nouveau", Map.of("nomClasse", "Forme"));

        // [Forme] surface() -> Retourne la surface de la forme (par défaut, zéro)
        formeClass.message(":message", "surface", (Message) (o, a) -> zero);

        // Rectangle (père: Forme)
        OObjet rectangleClass = metaClass.message(":nouveau", Map.of("nomClasse", "Rectangle",
                "nomsAttributs", List.of("longueur", "largeur"), "superClasse", formeClass));

        // [Rectangle] surface() -> longueur * largeur
        rectangleClass.message(":message", "surface",
                (Message) (o, a) -> ((OObjet) o.message("longueur")).message("*",
                        ((OObjet) o.message("largeur"))));

        // Carré (père: Rectangle, fils de Forme)
        OObjet carreClass = metaClass.message(":nouveau", Map.of("nomClasse", "Carré",
                "nomsAttributs", List.of("cote"), "superClasse", rectangleClass));

        // [Carré] surface() -> cote * cote
        carreClass.message(":message", "surface", (Message) (o, a) -> ((OObjet) o.message("cote"))
                .message("*", ((OObjet) o.message("cote"))));

        // Cercle (père: Forme)
        OObjet cercleClass = metaClass.message(":nouveau", Map.of("nomClasse", "Cercle",
                "nomsAttributs", List.of("centre", "rayon"), "superClasse", formeClass));

        // [Cercle] surface() -> Test: non implémenté
        formeClass.message(":message", "surface",
                (Message) (o, a) -> zero);

        /* Objets (classes) */
        // Rectangle
        OObjet monRect = rectangleClass.message(":nouveau", Map.of("longueur", deux, "largeur", trois));
        Object resMonRect = monRect.message("surface");
        assert six.equals(resMonRect); // 2 * 3 = 6 ?

        // Rectangle 2
        OObjet monRect2 = rectangleClass.message(":nouveau", Map.of("longueur", un, "largeur", un));
        Object resMonRect2 = monRect2.message("surface");
        assert un.equals(resMonRect2); // 1 * 1 = 1 ?

        // On vérifie que les pointeurs des deux rectangles sont différents
        assert !(monRect.equals(monRect2));

        // Carré
        OObjet monCarre = carreClass.message(":nouveau", Map.of("cote", trois));
        Object resMonCarre = monCarre.message("surface");
        assert neuf.equals(resMonCarre); // 3 * 3 = 9 ?

        // Cercle
        OObjet monCentre = pointClass.message(":nouveau", Map.of("x", cinq, "y", quatre));
        OObjet monCercle = cercleClass.message(":nouveau", Map.of("centre", monCentre, "rayon", deux));
        Object resMonCercle = monCercle.message("surface");
        assert zero.equals(resMonCercle); // 0 car non implémenté
    }

    /**
     * Création d'une hiérarchie pour représenter le type de données Booléens en
     * langage objet.
     */
    public static void booleen() {
        ObjVLisp obj = ObjVLispFabrique.nouveau();
        OObjet metaClass = obj.getClasse("Classe");
        OObjet systemClass = obj.getClasse("Systeme");

        OObjet booleenClass = metaClass.message(":nouveau", Map.of("nomClasse", "Booleen"));
        booleenClass.message(":accept", "non", "et", "ou");

        OObjet trueClass = metaClass.message(":nouveau",
                Map.of("nomClasse", "True", "nomsAttributs", List.of("bool"), "superClasse", booleenClass));

        OObjet falseClass = metaClass.message(":nouveau",
                Map.of("nomClasse", "False", "nomsAttributs", List.of("bool"), "superClasse", booleenClass));

        OObjet trueInstance = trueClass.message(":nouveau", Map.of("bool", true));

        OObjet falseInstance = falseClass.message(":nouveau", Map.of("bool", false));

        trueClass.message(":message", "non", (Message) (o, a) -> falseInstance);
        trueClass.message(":message", "ou", (Message) (o, a) -> trueInstance);
        trueClass.message(":message", "et", (Message) (o, a) -> a[0]);

        falseClass.message(":message", "non", (Message) (o, a) -> trueInstance);
        falseClass.message(":message", "ou", (Message) (o, a) -> a[0]);
        falseClass.message(":message", "et", (Message) (o, a) -> falseInstance);

        systemClass.message("afficher", (Object) trueInstance.message("non"));
        systemClass.message("afficher", (Object) trueInstance.message("ou", "bonjour"));
        systemClass.message("afficher", (Object) falseInstance.message("et", 12));
    }

    /**
     * Lance tous les exemples.
     * 
     * @param args
     */
    public static void main(String[] args) {
        exempleDLB();
        exempleMarquis();
        exempleNulli();
        groupeANicolasHenocque();
        booleen();
    }
}
