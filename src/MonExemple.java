import java.util.List;
import java.util.Map;

public class MonExemple {

    public static void exempleMarquis() {
        ObjVLisp obj = ObjVLispFabrique.nouveau();
        OObjet metaClass = obj.getClasse("Classe");
        OObjet systemClass = obj.getClasse("Systeme");

        OObjet classDessert = metaClass.message(":nouveau", Map.of("nomClasse", "Dessert",
                "nomsAttributs", List.of("sucre", "farine", "oeuf", "lait", "beurre")));
        classDessert.message(":message", "modeCuisson", (Message) (o, a) -> "Four");
        classDessert.message(":message", "tempsCuisson",
                (Message) (o, a) -> (Integer) o.message("oeuf") * 12);
        // 12 minutes de cuisson par oeuf

        OObjet classCrepe = metaClass.message(":nouveau", Map.of("nomClasse", "Crepe",
                "superClasse", classDessert, "nomsAttributs", List.of("rhum")));
        classCrepe.message(":message", "modeCuisson", (Message) (o, a) -> "Poele");
        classCrepe.message(":message", "tempsCuisson", (Message) (o, a) -> 2);

        OObjet classGateau = metaClass.message(":nouveau", Map.of("nomClasse", "Gateau",
                "superClasse", classDessert, "nomsAttributs", List.of("levure")));
        classGateau.message(":message", "degreCuisson", (Message) (o, a) -> 180);

        OObjet classMarbre = metaClass.message(":nouveau", Map.of("nomClasse", "Marbre",
                "superClasse", classGateau, "nomsAttributs", List.of("chocolat")));
        classMarbre.message(":message", "tempsCuisson",
                (Message) (o, a) -> (Integer) o.superMessage("tempsCuisson") + 3);

        OObjet mesCrepes = classCrepe.message("nouveau");
        mesCrepes.message(":sucre", 30);
        mesCrepes.message(":farine", 200);
        mesCrepes.message(":oeuf", 2);
        mesCrepes.message(":lait", 40);
        mesCrepes.message(":beurre", 33);
        mesCrepes.message(":rhum", 4);

        OObjet monMarbre = classMarbre.message("nouveau");
        mesCrepes.message(":sucre", 200);
        mesCrepes.message(":farine", 200);
        mesCrepes.message(":oeuf", 3);
        mesCrepes.message(":lait", 12);
        mesCrepes.message(":beurre", 125);
        mesCrepes.message(":levure", 1);
        mesCrepes.message(":chocolat", 25);

        OObjet integerClass = obj.getClasse("Entier");
        OObjet deux = integerClass.message(":nouveau", 2);
        OObjet trenteNeuf = integerClass.message(":nouveau", 39);

        Object tempsCrepe = mesCrepes.message("tempsCuisson");
        assert deux.equals(tempsCrepe);

        Object tempsMarbre = monMarbre.message("tempsCuisson");
        assert trenteNeuf.equals(tempsMarbre);

        systemClass.message("afficher", (Object) mesCrepes.message("modeCuisson"));
        systemClass.message("afficher", (Object) monMarbre.message("tempsCuisson"));

        systemClass.message("afficher", (Object) monMarbre.message("modeCuisson"));
        systemClass.message("afficher", (Object) monMarbre.message("degreCuisson"));
        systemClass.message("afficher", (Object) monMarbre.message("tempsCuisson"));
    }


    public static void exempleNulli() {
        ObjVLisp obj = ObjVLispFabrique.nouveau();
        OObjet metaClass = obj.getClasse("Classe");

        OObjet dinosaureClass = metaClass.message(":nouveau", Map.of("nomClasse", "Dinosaure", "nomsAttributs", List.of("nom", "sexe", "taille", "poids", "couleur")));

        // v1
        OObjet enzoDino = dinosaureClass.message(":nouveau", List.of("Enzo", "Mâle", "Petit", "Leger", "Marron"));

        // v2
        enzoDino.message(":nom", "Enzo");
        enzoDino.message(":sexe", "Mâle");
        enzoDino.message(":taille", "Petit");
        enzoDino.message(":poids", "Leger");
        enzoDino.message(":couleur", "Marron");

    }

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

    public static void main(String[] args) {
        exempleDLB();
        exempleMarquis();
        exempleNulli();
    }
}
