public class FakeObjVLisp implements ObjVLisp{

    @Override
    public OObjet getClasse(String nomDeClasse) {
        return new FakeObject();
    }
    
}
