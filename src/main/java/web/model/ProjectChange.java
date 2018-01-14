package web.model;

import java.util.UUID;

public final class ProjectChange {
    private  String id;
    private  String content;
    private  UUID clientId;

    public ProjectChange() {

    }

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
