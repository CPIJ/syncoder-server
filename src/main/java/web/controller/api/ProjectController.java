package web.controller.api;

import data.repository.MySqlProjectRepository;
import data.service.IProjectService;
import data.service.ProjectService;
import domain.ProjectManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/project")
public class ProjectController {

    private IProjectService service;

    public ProjectController() {
        service = new ProjectService(new MySqlProjectRepository());
    }

    @RequestMapping(value = "/live")
    public ResponseEntity getLiveProjects() {
        return Ok(ProjectManager.getLiveProjects());
    }

    @RequestMapping(value = "/all")
    public ResponseEntity getAllProject() {
        return Ok(service.getAll());
    }

    //region helper methods
    private <T> ResponseEntity<T> Ok(T body) {
        return new ResponseEntity<>(body, HttpStatus.OK);
    }
    //endregion
}
