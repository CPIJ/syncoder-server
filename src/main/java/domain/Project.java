package domain;

import java.io.Serializable;
import java.util.*;

public final class Project implements Serializable {

    private String id;
    private String content;
    private boolean isTemplate;
    private final Map<UUID, Client> clients = new HashMap<>();

    public Project() {

    }

    public Project(String id) {
        this.id = id;
    }

    public void addClient(Client client) {
        clients.put(client.getId(), client);
    }

    public void removeClient(Client client) {
        clients.remove(client.getId(), client);
    }

    public boolean hasClients() {
        return clients.size() > 0;
    }

    //region getters & setters

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Client> getClients() {
        return new ArrayList<>(clients.values());
    }

    public Client getClient(UUID clientId) {
        return clients.get(clientId);
    }

    public boolean getIsTemplate() {
        return isTemplate;
    }

    public void setIsTemplate(boolean isTemplate) {
        this.isTemplate = isTemplate;
    }

    //endregion

}
