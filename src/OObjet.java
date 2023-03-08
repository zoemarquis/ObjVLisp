
interface OObjet {

    /**
     * Un message est l'équivalent d'une fonction en java. On peut appliquer des
     * messages à tout OObjet (qu'il représente une classe, un objet terminal, une
     * metaclasse...). Comme une fonction, il peut, ou non, retourner quelque chose.
     * 
     * @param <T>       type générique pour pouvoir accepter tous les types de
     *                  retour
     * @param nom       le nom du message
     * @param arguments les arguments du message
     * @return le retour du message
     */
    <T> T message(String nom, Object... arguments);

    /**
     * Un superMessage est l'équivalent de super.maFonction() en java. Le but est de
     * redéfinir une méthode dans une hiérarchie de classe sans perdre la méthode
     * "plus haute" dans la hiérarchie. Comme une fonction, il peut, ou non,
     * retourner quelque chose.
     * 
     * @param <T>       type générique pour pouvoir accepter tous les types de
     *                  retour
     * @param nom       le nom du superMessage
     * @param arguments les arguments du superMessage
     * @return le retour du superMessage
     */
    <T> T superMessage(String nom, Object... arguments);

    /**
     * En cas d'erreur, cette méthode se comporte comme un message (d'erreur).
     * Nous avons décidé de retourner un objet de la classe Erro de java.
     * Cette méthode est appelée seulement lorsqu'on cherche un message ou un
     * superMessage qui n'existe pas.
     * 
     * @param <T>   type générique pour pouvoir accepter tous les types de
     *              retour
     * @param cause une chaine de caractères décrivant le problème
     * @return un objet Error
     */
    <T> T error(String cause);
}