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
import web.model.ClientInProject;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ProjectController {

    private final SimpMessagingTemplate template;
    private final IAuthenticationService service = new AuthenticationService(new MySqlAuthenticationRepository());

    @Autowired
    public ProjectController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("/project/onClosed")
    public void onClosed(ClientInProject clientInProject) {
        Project project = ProjectManager.load(clientInProject.getProjectId());
        Client client = project.getClient(clientInProject.getClientId());
        project.removeClient(client);

        if (!project.hasClients()) {
            ProjectManager.unload(project);
            return;
        }

        clientCountChanged(project);
    }

    @MessageMapping("/project/onOpened")
    public void onOpened(ClientInProject clientInProject) {
        Project project = ProjectManager.load(clientInProject.getProjectId());
        Account account = service.find(clientInProject.getEmail());
        Client client = new Client(account);

        project.addClient(client);

        clientCountChanged(project);
    }

    @MessageMapping("/project/change/{room}")
    public void onChange(Project project) {
        ProjectManager
                .load(project.getId())
                .setContent(project.getContent());

        template.convertAndSend("/topic/project/onchange/" + project.getId(), project);
    }

    //region helper methods
    private void clientCountChanged(Project project) {
        template.convertAndSend("/topic/project/onClientCountChange/" + project.getId(), new Object() {
            public final String id = project.getId();
            public final String content = project.getContent();
            public final List<Client> clients = new ArrayList<>(project.getClients());
        });
    }
    //endregion
}
