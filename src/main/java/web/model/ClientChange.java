package web.model;

import java.util.UUID;

public class ClientChange {

    private final UUID clientId;
    private final String email;
    private final String projectId;

    public ClientChange(UUID clientId, String email, String projectId) {
        this.clientId = clientId;
        this.email = email;
        this.projectId = projectId;
    }

    public UUID getClientId() {
        return clientId;
    }
    public String getEmail() {
        return email;
    }
    public String getProjectId() {
        return projectId;
    }
}
