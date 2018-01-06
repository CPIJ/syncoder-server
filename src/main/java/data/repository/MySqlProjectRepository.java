package data.repository;

import application.Properties;
import domain.Project;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MySqlProjectRepository implements IProjectRepository {

    private Connection connection;

    public MySqlProjectRepository() {
        connection = Properties.getConnection(Properties.Db.PROJECT);

        if (connection == null) {
            throw new IllegalArgumentException("No database found!");
        }
    }

    @Override
    public void save(Project project) {
        boolean projectAlreadyExists = this.find(project.getId()) != null;
        int affectedRows = -1;

        if (projectAlreadyExists) {
            affectedRows = this.update(project);
        } else {
            try(PreparedStatement statement = connection.prepareStatement("INSERT INTO project VALUES(?, ?, ?)")) {

                statement.setString(1, project.getId());
                statement.setString(2, project.getContent());
                statement.setBoolean(3, false);

                affectedRows = statement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    private int update(Project project) {
        int affectedRows = -1;

        try(PreparedStatement statement = connection.prepareStatement("UPDATE project SET content = ? WHERE id LIKE ?")){

            statement.setString(1, project.getContent());
            statement.setString(2, project.getId());

            affectedRows = statement.executeUpdate();

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return affectedRows;
    }

    @Override
    public Project find(String projectId) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM project WHERE id = ?")) {
            statement.setString(1, projectId);

            ResultSet resultSet = statement.executeQuery();
            Project project = null;

            if (resultSet.next()) {
                project = new Project(resultSet.getString("id"));
                project.setContent(resultSet.getString("content"));
                project.setIsTemplate(resultSet.getBoolean("isTemplate"));
            }
            return project;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Project> getAll() {
        List<Project> projects = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM project")) {
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                Project project = new Project(result.getString("id"));
                project.setContent(result.getString("content"));
                project.setIsTemplate(result.getBoolean("isTemplate"));

                projects.add(project);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return projects;
    }
}
