package com.controller;

import com.model.agent.*;
import com.model.Response;
import com.model.agent.FileCreateRequest;
import com.model.agent.ProcessRequest;
import com.repository.AgentRepository;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// DELETE: used for testing
@CrossOrigin

@RestController
@RequestMapping("/files")
public class FileController {
    private static final Logger log = LoggerFactory.getLogger(FileController.class);
    private static final AgentRepository agentRepo = AgentRepository.get();

    private String getJSONResponse(String url, String jsonRequestBody) {
        var client = HttpClient.newHttpClient();

        try {
            URI uri = new URI(url);

            var request = HttpRequest.newBuilder(uri).
                    POST(HttpRequest.BodyPublishers.ofString(jsonRequestBody))
                    .header("Content-type", "application/json")
                    .build();

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();

        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/agents")
    public Response showAgents() {
        return new AgentsResponse(agentRepo.getAgents());
    }

    @PostMapping("/dir")
    public Response showDirectory(@RequestBody DirectoryRequest body) {
        log.info(body.toString());

        var maybeAgent = agentRepo.findById(body.getId());
        if(maybeAgent.isEmpty()) {
            return Response.bad("No such agent");
        }

        String ip = maybeAgent.get().getIp();
        String port = maybeAgent.get().getPort();

        String url = "http://" + ip + ":" + port + "/files";
        String jsonInputString = new JSONObject()
                .put("path", body.getPath())
                .toString();

        return new DirectoryResponse(getJSONResponse(url, jsonInputString));
    }

    @PostMapping("/create")
    public Response createFile(@RequestBody FileCreateRequest body) {
        log.info(body.toString());

        var maybeAgent = agentRepo.findById(body.getId());
        if (maybeAgent.isEmpty()) {
            return Response.bad("No such agent");
        }

        String ip = maybeAgent.get().getIp();
        String port = maybeAgent.get().getPort();

        String url = "http://" + ip + ":" + port + "/files" + "/create";
        JSONObject jsonInput = new JSONObject()
                .put("path", body.getPath())
                .put("type", body.getType());

        if (body.hasContent()) {
            jsonInput.put("content", body.getContent());
        }
        String jsonInputString = jsonInput.toString();

        String response = getJSONResponse(url, jsonInputString);
        // TODO maybe check response

        return Response.good();
    }

    @PostMapping("/delete")
    public Response deletePath(@RequestBody DeleteRequest body) {
        log.info(body.toString());

        var maybeAgent = agentRepo.findById(body.getId());
        if(maybeAgent.isEmpty()) {
            return Response.bad("No such agent");
        }

        String ip = maybeAgent.get().getIp();
        String port = maybeAgent.get().getPort();

        String url = "http://" + ip + ":" + port + "/files" + "/delete";
        String jsonInputString = new JSONObject()
                .put("path", body.getPath())
                .toString();

        String response = getJSONResponse(url, jsonInputString);
        // TODO maybe check response

        return Response.good();
    }

    @PostMapping("/content")
    public Response deletePath(@RequestBody ContentRequest body) {
        log.info(body.toString());

        var maybeAgent = agentRepo.findById(body.getId());
        if(maybeAgent.isEmpty()) {
            return Response.bad("No such agent");
        }

        String ip = maybeAgent.get().getIp();
        String port = maybeAgent.get().getPort();

        String url = "http://" + ip + ":" + port + "/files" + "/content";
        String jsonInputString = new JSONObject()
                .put("path", body.getPath())
                .toString();

        String response = getJSONResponse(url, jsonInputString);
        // TODO maybe check response

        return Response.good();
    }

    @PostMapping("/procs")
    public Response deletePath(@RequestBody ProcessRequest body) {
        log.info(body.toString());

        var maybeAgent = agentRepo.findById(body.getId());
        if(maybeAgent.isEmpty()) {
            return Response.bad("No such agent");
        }

        String ip = maybeAgent.get().getIp();
        String port = maybeAgent.get().getPort();

        String url = "http://" + ip + ":" + port + "/files" + "/procs";

        JSONObject jsonInput = new JSONObject()
                .put("count", body.getCount());

        if(body.hasSortBy()) {
            jsonInput.put("sortBy", body.getSortBy()).toString();
        }

        String jsonInputString = jsonInput.toString();

        String response = getJSONResponse(url, jsonInputString);
        // TODO maybe check response

        return Response.good();
    }

    @PostMapping("/search")
    public Response searchPath(@RequestBody SearchRequest body) {
        log.info(body.toString());

        var maybeAgent = agentRepo.findById(body.getId());
        if(maybeAgent.isEmpty()) {
            return Response.bad("No such agent");
        }

        String ip = maybeAgent.get().getIp();
        String port = maybeAgent.get().getPort();

        String url = "http://" + ip + ":" + port + "/files" + "/search";
        String jsonInputString = new JSONObject()
                .put("pattern", body.getPattern())
                .toString();

        String response = getJSONResponse(url, jsonInputString);

        return new SearchResponse(response);
    }
}

