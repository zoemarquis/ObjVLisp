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

        // vérifier concaténation fonctionne
        private OObjet getChaineClass() {
                OObjet chaineClasse = getClasse("Classe").message(":nouveau", Map.of("nomClasse",
                                "Chaine", "nomsAttributs", List.of("str")));
                chaineClasse.message(":message", ":nouveau",
                                (Message) (o, a) -> chaineClasse.superMessage(":nouveau", Map.of("str", a[0])));
                chaineClasse.message(":message", "+", (Message) (o, a) -> chaineClasse.message(":nouveau",
                                (String) o.message("str") + (String) ((OObjet) a[0]).message("str")));
                return chaineClasse;
        }

        private OObjet getBooleenClass() {
                OObjet booleenClass = getClasse("Classe").message(":nouveau", Map.of("nomClasse",
                                "Booleen", "nomsAttributs", List.of("etat")));
                booleenClass.message(":message", ":nouveau",
                                (Message) (o, a) -> booleenClass.superMessage(":nouveau", Map.of("etat", a[0])));
                booleenClass.message(":message", "non", (Message) (o, a) -> booleenClass.message(":nouveau",
                                !((Boolean) o.message("etat"))));
                booleenClass.message(":message", "ou", (Message) (o, a) -> booleenClass.message(":nouveau",
                                (Boolean) o.message("etat") || (Boolean) ((OObjet) a[0]).message("etat")));
                booleenClass.message(":message", "et", (Message) (o, a) -> booleenClass.message(":nouveau",
                                (Boolean) o.message("etat") && (Boolean) ((OObjet) a[0]).message("etat")));
                return booleenClass;
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
                nosClasses.put("Booleen", getBooleenClass());
        }
}
