package web.controller.api;

import data.repository.MySqlProjectRepository;
import data.service.IProjectService;
import data.service.ProjectService;
import domain.IProjectManager;
import domain.Project;
import domain.ProjectManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

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
    public ResponseEntity<List<Project>> getAllProject() {
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
