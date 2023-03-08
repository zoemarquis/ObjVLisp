import java.util.Map;
import java.util.List;

/**
 * Classe qui réalise l'nterface qui représente le langage ObjVLisp.
 * Instancie en ObjVLisp les classes Systeme, Entier et Chaine.
 * Permet d'accéder aux classes natives du langage et à celles instanciées.
 * 
 * @author Zoé Marquis
 * @author Enzo Nulli
 * @version 1.0
 */
class RealiseObjVLisp implements ObjVLisp {
        private Map<String, OObjet> nosClasses;

        /**
         * Instancie la classe Système.
         * Permet d'afficher des OObjet.
         *
         * @return OObjet la classe Système
         */
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

        /**
         * Instancie la classe Entier.
         *
         * @return OObjet la classe Entier
         */
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

        /**
         * Instancie la classe Chaine.
         *
         * @return OObjet la classe Chaine
         */
        // vérifier concaténation fonctionne !!
        private OObjet getChaineClass() {
                OObjet chaineClasse = getClasse("Classe").message(":nouveau", Map.of("nomClasse",
                                "Chaine", "nomsAttributs", List.of("str")));
                chaineClasse.message(":message", ":nouveau",
                                (Message) (o, a) -> chaineClasse.superMessage(":nouveau", Map.of("str", a[0])));
                chaineClasse.message(":message", "+", (Message) (o, a) -> chaineClasse.message(":nouveau",
                                (String) o.message("str") + (String) ((OObjet) a[0]).message("str")));
                return chaineClasse;
        }

        @Override
        public OObjet getClasse(String nomDeClasse) {
                return nosClasses.get(nomDeClasse);
        }

        /**
         * Constructeur de la classe
         * 
         * @param nosClasses la map qui contient toutes les classes instanciées
         */
        public RealiseObjVLisp(Map<String, OObjet> nosClasses) {
                this.nosClasses = nosClasses;
                nosClasses.put("Systeme", getSystemClass());
                nosClasses.put("Entier", getEntierClass());
                nosClasses.put("Chaine", getChaineClass());
        }
}
