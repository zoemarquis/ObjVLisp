import java.util.List;
import java.util.Map;

public class MonExemple {

    public static void exempleMarquis() {

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
