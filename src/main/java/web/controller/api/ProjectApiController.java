package web.controller.api;

import data.service.IProjectService;
import domain.IProjectManager;
import domain.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        return ok(projectManager.getLiveProjects());
    }

    @RequestMapping(value = "/all")
    public ResponseEntity<List<Project>> getAllProject() {
        return ok(service.getAll());
    }

    @RequestMapping(value = "/template")
    public ResponseEntity getAllTemplates() {
        return ok(service.getAllTemplates());
    }

    //region helper methods
    private <T> ResponseEntity<T> ok(T body) {
        return new ResponseEntity<>(body, HttpStatus.OK);
    }
    //endregion
}
