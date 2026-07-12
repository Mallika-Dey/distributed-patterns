package com.example.workflow;

import com.example.workflow.dto.StartWorkflowRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/workflow")
@RequiredArgsConstructor
public class WorkflowController {
    private final WorkflowService workflowService;

    @PostMapping("/start")
    public String start(@RequestBody StartWorkflowRequest request) {
        workflowService.start(request);
        return "Workflow Started";
    }
}
