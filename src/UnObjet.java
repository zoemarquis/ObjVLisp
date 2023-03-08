import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

class UnObjet implements OObjet {

    private Map<String, Object> info;

    public UnObjet(Map<String, Object> sesAttributs) {
        info = new HashMap<String, Object>();
        info.putAll(sesAttributs);
    }

    public Object getInfo(String nomAttribut) {
        return info.get(nomAttribut);
    }

    public void setInfo(String nomAttribut, Object valeurAttribut) {
        info.put(nomAttribut, valeurAttribut);
    }

    public List<String> getListAllAttributs() {
        List<String> ret = new ArrayList<String>((List<String>) getInfo("nomsAttributs"));
        if (((UnObjet) getInfo("superClasse")) != null)
            ret.addAll(List.copyOf(((UnObjet) getInfo("superClasse")).getListAllAttributs()));
        return ret;
    }

    public Map<String, Message> getMapMessages() {
        return (Map<String, Message>) getInfo("messages");
    }

    // on n'arrive pas à null( car :message n'existe pas pour un objet terminal
    // (objet qui n'est pas l'instance de Classe)
    public void setMessage(String nomMsg, Message leMsg) {
        if (getInfo("nomClasse") != null)
            getMapMessages().put(nomMsg, leMsg);
    }

    // recherche le msg
    private Message getMessage(String nom) {
        Message leMsg = null;
        UnObjet c = (UnObjet) getInfo("classe");
        if (getInfo("nomClasse") != null)
            leMsg = getMapMessages().get(nom);
        if (leMsg == null) {
            while (leMsg == null && c != null) {
                leMsg = c.getMapMessages().get(nom);
                c = (UnObjet) c.getInfo("superClasse");
            }
        }
        return leMsg;
    }

    // recherche le superMsg
    private Message getSuperMessage(String nom) {
        Message leMsg = null;
        UnObjet classe = (UnObjet) getInfo("classe");
        if (getInfo("nomClasse") != null)
            leMsg = getMapMessages().get(nom);

        if (leMsg == null) {
            while (leMsg == null && classe != null) {
                leMsg = classe.getMapMessages().get(nom);
                classe = (UnObjet) classe.getInfo("superClasse");
            }
        }
        if (leMsg == null || classe == null)
            return null;
        leMsg = null;
        while (leMsg == null && classe != null) {
            leMsg = classe.getMapMessages().get(nom);
            classe = (UnObjet) classe.getInfo("superClasse");
        }
        return leMsg;
    }

    @Override
    public <T> T message(String nom, Object... arguments) { // comment faire pour getter et setter ?
        Message leMsg = getMessage(nom);
        if (leMsg == null)
            return error("Ce message c'est pas défini pour cet objet.");
        return (T) leMsg.apply(this, arguments);
    }

    @Override
    public <T> T superMessage(String nom, Object... arguments) {
        Message leMsg = getSuperMessage(nom);
        if (leMsg == null)
            return error("Il n'y a pas de supermessage défini pour cet objet.");
        return (T) leMsg.apply(this, arguments);
    }

    @Override
    public <T> T error(String cause) {
        return (T) new Error(cause);
    }

    private Map<String, Object> getMap() {
        return info;
    }

    public boolean equals(Object o) {
        UnObjet oo = (UnObjet) o;
        Map<String, Object> oMap = oo.getMap();
        for (String cle : info.keySet()) {
            if (oMap.containsKey(cle)) {
                if ((cle == "nomsAttributs" || cle == "methodes") && (!getInfo(cle).equals(oMap.get(cle))))
                    return false;
                if (getInfo(cle) != oMap.get(cle))
                    return false;
            } else
                return false;
        }
        return info.size() == oMap.size();
    }

}