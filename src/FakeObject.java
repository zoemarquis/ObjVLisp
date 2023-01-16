public class FakeObject implements OObjet {

    @Override
    public <T> T message(String nom, Object... arguments) {
        return (T) this;
    }

    @Override
    public <T> T superMessage(String nom, Object... arguments) {
        return (T) this;
    }

    @Override
    public <T> T error(String cause) {
        return (T) this;
    }
    
}
