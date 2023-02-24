import java.util.Map;
import java.util.List;

class RealiseObjVLisp implements ObjVLisp {
    private Map<String, OObjet> nosClasses;

    private OObjet getSystemClass() {
        OObjet systemClass = getClasse("Classe").message(":nouveau", Map.of("nomClasse",
                "Systeme"));
        systemClass.message(":message", "afficher",
                (Message) (o, a) -> {
                    System.out.println((String) ((OObjet) a[0]).message("toString"));
                    return a[0];
                });
        return systemClass;
    }

    private OObjet getEntierClass() {
        OObjet entierClass = getClasse("Classe").message(":nouveau", Map.of("nomClasse",
                "Entier", "nomsAttributs", List.of("valeur")));
        entierClass.message(":message", ":nouveau",
                (Message) (o, a) -> entierClass.superMessage(":nouveau", Map.of("valeur", a[0])));
        entierClass.message(":message", "+", (Message) (o, a) -> entierClass.message(":nouveau",
                (Integer) o.message("valeur") + (Integer) ((OObjet) a[0]).message("valeur")));
        entierClass.message(":message", "*", (Message) (o, a) -> entierClass.message(":nouveau",
                (Integer) o.message("valeur") * (Integer) ((OObjet) a[0]).message("valeur")));
        entierClass.message(":message", "/", (Message) (o, a) -> entierClass.message(":nouveau",
                (Integer) o.message("valeur") / (Integer) ((OObjet) a[0]).message("valeur")));
        return entierClass;
    }

    private OObjet getChaineClass() {
        OObjet chaineClasse = getClasse("Classe").message(":nouveau", Map.of("nomClasse",
                "Chaine", "nomsAttributs", List.of("str")));
        chaineClasse.message(":message", ":nouveau",
                (Message) (o, a) -> chaineClasse.superMessage(":nouveau", Map.of("str", a[0])));
        return chaineClasse;
    }

    @Override
    public OObjet getClasse(String nomDeClasse) {
        return nosClasses.get(nomDeClasse);
    }

    public RealiseObjVLisp(Map<String, OObjet> nosClasses) {
        this.nosClasses = nosClasses;
        nosClasses.put("Systeme", getSystemClass());
        nosClasses.put("Entier", getEntierClass());
        nosClasses.put("Chaine", getChaineClass());
    }
}
