import java.util.List;
import java.util.Map;

public class MonExemple {

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
            // Vitesse en km/h (1000 c'est absurde mais c'est juste pour l'assert a la fin sinon il faudrait un double vu qu'il y a des décimales)
    
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
