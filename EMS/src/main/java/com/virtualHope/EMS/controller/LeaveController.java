package com.virtualHope.EMS.controller;

import com.virtualHope.EMS.model.LeaveRequest;
import com.virtualHope.EMS.service.JwtService;
import com.virtualHope.EMS.service.LeaveService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
@CrossOrigin(origins = "*")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private JwtService jwtService;

    private String getUsernameFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;
        String token = authHeader.substring(7);
        return jwtService.extractUsername(token);
    }

    @PostMapping("/apply")
    public ResponseEntity<LeaveRequest> applyLeave(@RequestBody LeaveRequest request, HttpServletRequest httpReq) {
        String username = getUsernameFromToken(httpReq);
        return ResponseEntity.ok(leaveService.applyLeave(username, request));
    }

    @PostMapping("/approve/{id}")
    public ResponseEntity<LeaveRequest> approveLeave(@PathVariable Long id, HttpServletRequest httpReq) {
        String username = getUsernameFromToken(httpReq);
        return ResponseEntity.ok(leaveService.approveLeave(username, id));
    }

    @PostMapping("/reject/{id}")
    public ResponseEntity<LeaveRequest> rejectLeave(@PathVariable Long id, HttpServletRequest httpReq) {
        String username = getUsernameFromToken(httpReq);
        return ResponseEntity.ok(leaveService.rejectLeave(username, id));
    }

    @GetMapping("/my-leaves")
    public ResponseEntity<List<LeaveRequest>> getMyLeaves(HttpServletRequest httpReq) {
        String username = getUsernameFromToken(httpReq);
        return ResponseEntity.ok(leaveService.getMyLeaves(username));
    }

    @GetMapping("/team-leaves")
    public ResponseEntity<List<LeaveRequest>> getTeamLeaves(HttpServletRequest httpReq) {
        String username = getUsernameFromToken(httpReq);
        return ResponseEntity.ok(leaveService.getTeamLeaves(username));
    }
}

