package com.rukojara.runnerz.foo.bar.run;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class RunRepository {

    public static final Logger log = LoggerFactory.getLogger(RunRepository.class);

    // Dependency injection using constructor injection
    private final JdbcClient jdbcClient;
    public RunRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Run> findAll() {
        return jdbcClient.sql("select * from run")
                .query(Run.class)
                .list();
    }

    public Optional<Run> findById(Integer id) {
        log.info("inside findById");
        return jdbcClient.sql("select * from Run where id = :id")
                .param("id", id)
                .query(Run.class)
                .optional();
    }

    public void create(Run run) {
        int updated = jdbcClient.sql("INSERT INTO Run (id, title, started_on, completed_on, miles, location) VALUES(?, ?, ?, ?, ?, ?)")
                .params(List.of(run.id(), run.title(), run.startedOn(), run.completedOn(), run.miles(), run.location().toString()))
                .update();
        Assert.state(updated == 1, "failed to create run" + run.title());
    }

    public void update(Run run, Integer id) {
        Optional<Run> existingId = findById(id);
        if(existingId.isPresent()) {
            int updated = jdbcClient.sql("update Run set title = ?, started_on = ?, completed_on = ?, miles = ?, location = ? where id = ?")
                    .params(List.of(run.title(), run.startedOn(), run.completedOn(), run.miles(), run.location().toString(), id))
                    .update();

            Assert.state(updated == 1, "failed to update run " + run.title());
        }
    }


    public void delete(Integer id) {
        Optional<Run> existingId = findById(id);
        if(existingId.isPresent()) {
            int updated = jdbcClient.sql("delete from Run where id = :id")
                    .param("id", id)
                    .update();

            Assert.state(updated == 1, "Failed to delete " + id);
        }
    }

    public int count() {
        return jdbcClient.sql("select * from Run")
                .query()
                .listOfRows().size();
    }

    public void saveAll(List<Run> runs) {
        runs.forEach(this::create);
    }

    public List<Run> findByLocation(String location) {
        return jdbcClient.sql("select * from Run where location = : location")
                .param("location", location)
                .query(Run.class)
                .list();
    }

}
