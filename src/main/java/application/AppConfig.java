package application;

import data.repository.IAuthenticationRepository;
import data.repository.MySqlAuthenticationRepository;
import data.repository.MySqlProjectRepository;
import data.repository.IProjectRepository;
import data.service.AuthenticationService;
import data.service.IAuthenticationService;
import data.service.IProjectService;
import data.service.ProjectService;
import domain.IProjectManager;
import domain.ProjectManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rmi.fontys.IRemotePublisherForListener;
import rmi.fontys.RemotePublisher;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

@Configuration
public class AppConfig {
    @Bean
    public IProjectManager projectManager() {
        return ProjectManager.instance();
    }

    @Bean
    public IProjectService projectService(IProjectRepository repository) {
        return new ProjectService(repository);
    }

    @Bean
    public IProjectRepository projectRepository() {
        return new MySqlProjectRepository();
    }

    @Bean
    public IAuthenticationService authenticationService(IAuthenticationRepository repository) {
        return new AuthenticationService(repository);
    }

    @Bean
    public IAuthenticationRepository authenticationRepository() {
        return new MySqlAuthenticationRepository();
    }

    @Bean
    public RemotePublisher remotePublisher() throws RemoteException {
        RemotePublisher publisher = new RemotePublisher();

        String prop = Properties.get("rmi", "registerProperty");
        int port = Integer.parseInt(Properties.get("rmi", "port"));
        String property = Properties.get("rmi", "registerPublisher");

        publisher.registerProperty(prop);
        Registry registry = LocateRegistry.createRegistry(port);
        registry.rebind(property, publisher);

        return publisher;
    }
}
