package web.model;

import java.util.UUID;

public class ProjectChange {
    private String id;
    private String content;
    private UUID clientId;

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
