import java.util.Map;

public class MonExemple {

    public static void exempleMarquis() {

    }

    public static void exempleNulli() {

    }

    public static void exempleDLB() {
        ObjVLisp obj = ObjVLispFabrique.nouveau();
        OObjet systemClass = obj.getClasse("System");
        OObjet rootClass = obj.getClasse("Object");
        OObjet integerClass = obj.getClasse("Integer");
        systemClass.message("print", (Object) rootClass);
        OObjet dix = integerClass.message(":new", 10);
        OObjet vingt = integerClass.message(":new", 20);
        OObjet cinquante = integerClass.message(":new", 50);
        OObjet cent = integerClass.message(":new", 100);

        OObjet classA = rootClass.message(":subclass",
                Map.of("className", "A"));
        classA.message(":message", "foo", (Message) (o, a) -> dix);
        classA.message(":message", "bar", (Message) (o, a) -> o.message("foo"));
        OObjet classB = classA.message(":subclass", Map.of("className", "B"));
        classB.message(":message", "bar",
                (Message) (o, a) -> ((OObjet) o.superMessage("bar"))
                        .message("+", (Object) o.message("foo")));
        OObjet classC = classB.message(":subclass", Map.of("className", "C"));
        classC.message(":message", "foo", (Message) (i, a) -> cinquante);
        OObjet anA = classA.message("new");
        Object resA = anA.message("bar");
        assert dix.equals(resA);
        OObjet aB = classB.message("new");
        Object resB = aB.message("bar");
        assert vingt.equals(resB);
        OObjet aC = classC.message("new");
        Object resC = aC.message("bar");
        assert cent.equals(resC);

    }

    public static void main(String[] args) {
        exempleDLB();
        exempleMarquis();
        exempleNulli();
    }
}
