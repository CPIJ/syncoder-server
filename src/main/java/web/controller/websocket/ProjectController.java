package web.controller.websocket;

import data.repository.MySqlAuthenticationRepository;
import data.service.AuthenticationService;
import data.service.IAuthenticationService;
import domain.Account;
import domain.Client;
import domain.Project;
import domain.ProjectManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import web.model.ClientChange;
import web.model.ProjectChange;

@Controller
public class ProjectController {

    private final SimpMessagingTemplate template;
    private final IAuthenticationService service = new AuthenticationService(new MySqlAuthenticationRepository());

    @Autowired
    public ProjectController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("/project/onClosed")
    public void onClosed(ClientChange clientChange) {
        Project project = ProjectManager.find(clientChange.getProjectId());

        if (project == null) return;

        Client client = project.getClient(clientChange.getClientId());
        client.removeWindow();

        if (client.getOpenWindows() != 0) return;

        project.removeClient(client);

        if (project.hasClients()) {
            clientStateChange(project, client);
        } else {
            ProjectManager.unload(project);
        }
    }

    @MessageMapping("/project/onOpened")
    public void onOpened(ClientChange clientChange) {
        Project project = ProjectManager.load(clientChange.getProjectId());
        Client client = project.getClient(clientChange.getClientId());

        if (client == null) {
            Account account = service.find(clientChange.getEmail());
            client = new Client(clientChange.getClientId(), account);
            project.addClient(client);
        }

        client.addWindow();

        template.convertAndSend("/topic/project/onJoin/" + client.getId(), project);
        clientStateChange(project, client);
    }

    @MessageMapping("/project/change/{room}")
    public void onChange(ProjectChange p) {
        Project changedProject = ProjectManager.load(p.getId());
        changedProject.setContent(p.getContent());

        template.convertAndSend("/topic/project/onchange/" + p.getId(), new Object(){
            public final Project project = changedProject;
            public final Client sender = changedProject.getClient(p.getClientId());
        });
    }

    private void clientStateChange(Project p, Client c) {
        template.convertAndSend("/topic/project/onClientCountChange/" + p.getId(), new Object(){
            public final Project project = p;
            public final Client sender = c;
        });
    }

}

