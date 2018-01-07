package web.model;

import domain.Client;
import domain.Project;

public class StateChange {
    private Project project;
    private Client sender;

    public StateChange() {
    }

    public StateChange(Project project, Client sender) {
        this.project = project;
        this.sender = sender;
    }

    public Project getProject() {
        return project;
    }

    public Client getSender() {
        return sender;
    }
}
