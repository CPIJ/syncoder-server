package web.model;

import java.util.UUID;

public class ClientChange {

    private UUID clientId;
    private String email;
    private String projectId;

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
