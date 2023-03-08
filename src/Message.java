/**
 * Interface pour appliquer un message à un OObjet.
 * 
 * @author Zoé Marquis
 * @author Enzo Nulli
 * @version 1.0
 */
interface Message {
    Object apply(OObjet unObjet, Object... arguments);
}