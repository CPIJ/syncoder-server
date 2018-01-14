package web.model;

import java.util.UUID;

public class ProjectChange {
    private final String id;
    private final String content;
    private final UUID clientId;

    public ProjectChange(String id, String content, UUID clientId) {
        this.id = id;
        this.content = content;
        this.clientId = clientId;
    }

    public String getContent() {
        return content;
    }
    public String getId() {
        return id;
    }
    public UUID getClientId() {
        return clientId;
    }
}
