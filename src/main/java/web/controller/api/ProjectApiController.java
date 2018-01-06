package web.controller.api;

import data.repository.MySqlProjectRepository;
import data.service.IProjectService;
import data.service.ProjectService;
import domain.IProjectManager;
import domain.ProjectManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.rmi.RemoteException;

@RestController
@RequestMapping("/project")
public class ProjectApiController {
    private final IProjectService service;
    private final IProjectManager projectManager;

    @Autowired
    public ProjectApiController(IProjectService service, IProjectManager projectManager) {
        this.service = service;
        this.projectManager = projectManager;
    }

    @RequestMapping(value = "/live")
    public ResponseEntity getLiveProjects() {
        return Ok(projectManager.getLiveProjects());
    }

    @RequestMapping(value = "/all")
    public ResponseEntity getAllProject() {
        return Ok(service.getAll());
    }

    @RequestMapping(value = "/template")
    public ResponseEntity getAllTemplates() {
        return Ok(service.getAllTemplates());
    }

    //region helper methods
    private <T> ResponseEntity<T> Ok(T body) {
        return new ResponseEntity<>(body, HttpStatus.OK);
    }
    //endregion
}
