package domain;

import java.io.Serializable;
import java.util.*;

public final class Project implements Serializable {

    private String id;
    private String content;
    private Map<UUID, Client> clients;

    public Project() {
        clients = new HashMap<>();
    }

    public Project(String id) {
        this.id = id;
        clients = new HashMap<>();
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

    @Override
    public String toString() {
        return id;
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

    //endregion

}
