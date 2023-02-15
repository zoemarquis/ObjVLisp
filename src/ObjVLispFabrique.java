
class ObjVLispFabrique {
    private ObjVLispFabrique() {
        // pas d'instanciation possible
    }

    static ObjVLisp nouveau() {
        // crée la map avec entier, metaclass etc
        // pour l'instant rien, permet juste de créer

        // à écrire en ObjVLisp ?
        return new RealiseObjVLisp();
    }
}