
interface OObjet {

    <T> T message(String nom, Object... arguments);

    <T> T superMessage(String nom, Object... arguments);

    <T> T error(String cause);
}