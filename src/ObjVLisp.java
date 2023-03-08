/**
 * Interface qui représente le langage ObjVLisp.
 * 
 * @author Zoé Marquis
 * @author Enzo Nulli
 * @version 1.0
 */

interface ObjVLisp {

    /**
     * Recupère une classe déjà instanciée.
     *
     * @param nomDeClasse le nom de la classe déjà instanciée
     * @return la classe correspondante
     */
    OObjet getClasse(String nomDeClasse);
}
