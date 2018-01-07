package web.controller.websocket;

import data.service.IAuthenticationService;
import domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import web.model.ClientChange;
import web.model.ProjectChange;
import web.model.StateChange;

@Controller
public class ProjectController {
    private final SimpMessagingTemplate template;
    private final IAuthenticationService service;
    private final IProjectManager projectManager;

    @Autowired
    public ProjectController(SimpMessagingTemplate template, IAuthenticationService service, IProjectManager projectManager) {
        this.template = template;
        this.projectManager = projectManager;
        this.service = service;
    }

    @MessageMapping("/project/onClosed")
    public void onClosed(ClientChange clientChange) {
        Project project = projectManager.find(clientChange.getProjectId());

        if (project == null) return;

        Client client = project.getClient(clientChange.getClientId());
        client.removeWindow();

        if (client.getOpenWindows() != 0) return;

        project.removeClient(client);

        if (project.hasClients()) {
            clientStateChange(project, client);
        } else {
            projectManager.unload(project);
        }
    }

    @MessageMapping("/project/onOpened")
    public void onOpened(ClientChange clientChange) {
        Project project = projectManager.load(clientChange.getProjectId());
        Client client = project.getClient(clientChange.getClientId());

        if (client == null) {
            Account account = service.find(clientChange.getEmail());
            client = new Client(clientChange.getClientId(), account);
            project.addClient(client);
        }

        client.addWindow();

        template.convertAndSend("/topic/project/onJoin/" + client.getId(), project);

        if (project.getClients().size() > 1) {
            clientStateChange(project, client);
        }
    }

    @MessageMapping("/project/change/{room}")
    public void onChange(ProjectChange change) {
        Project project = projectManager.load(change.getId());
        project.setContent(change.getContent());

        template.convertAndSend("/topic/project/onchange/" + change.getId(), new StateChange(project, project.getClient(change.getClientId())));
    }

    private void clientStateChange(Project p, Client c) {
        template.convertAndSend("/topic/project/onClientCountChange/" + p.getId(), new StateChange(p, c));
    }
}

