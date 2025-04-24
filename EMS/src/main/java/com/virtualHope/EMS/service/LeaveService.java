package com.virtualHope.EMS.service;

import com.virtualHope.EMS.model.*;
import com.virtualHope.EMS.repository.EmployeeRepository;
import com.virtualHope.EMS.repository.LeaveRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class LeaveService {

    @Autowired
    private LeaveRequestRepository leaveRepo;

    @Autowired
    private EmployeeRepository empRepo;

    public LeaveRequest applyLeave(String username, LeaveRequest request) {
        Employee employee = empRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        long daysRequested = ChronoUnit.DAYS.between(request.getFromDate(), request.getToDate()) + 1;

        if (employee.getUsedPaidLeaves() + daysRequested <= employee.getTotalPaidLeaves()) {
            // Still within paid leave limit
            employee.setUsedPaidLeaves(employee.getUsedPaidLeaves() + (int) daysRequested);
            request.setLeaveType(LeaveType.PAID);
        } else {
            // Crossed paid leave limit → unpaid leave
            request.setLeaveType(LeaveType.UNPAID);
        }

        request.setStatus(LeaveStatus.PENDING);
        request.setEmployee(employee);

        return leaveRepo.save(request);
    }


    public LeaveRequest approveLeave(String username, Long leaveId) {
        LeaveRequest request = leaveRepo.findById(leaveId).orElseThrow();
        Employee approver = empRepo.findByUsername(username).orElseThrow();

        if (!isApprover(request.getEmployee(), approver)) {
            throw new RuntimeException("Unauthorized");
        }

        request.setStatus(LeaveStatus.APPROVED);
        request.setApprover(approver);
        return leaveRepo.save(request);
    }

    public LeaveRequest rejectLeave(String username, Long leaveId) {
        LeaveRequest request = leaveRepo.findById(leaveId).orElseThrow();
        Employee approver = empRepo.findByUsername(username).orElseThrow();

        if (!isApprover(request.getEmployee(), approver)) {
            throw new RuntimeException("Unauthorized");
        }

        request.setStatus(LeaveStatus.REJECTED);
        request.setApprover(approver);
        return leaveRepo.save(request);
    }

    public List<LeaveRequest> getMyLeaves(String username) {
        Employee emp = empRepo.findByUsername(username).orElseThrow();
        return leaveRepo.findByEmployee(emp);
    }

    public List<LeaveRequest> getTeamLeaves(String username) {
        Employee manager = empRepo.findByUsername(username).orElseThrow();
        List<Employee> subordinates = getAllSubordinates(manager);

        List<LeaveRequest> teamLeaves = new ArrayList<>();
        for (Employee e : subordinates) {
            teamLeaves.addAll(leaveRepo.findByEmployee(e));
        }
        return teamLeaves;
    }

    // Utility method to get subordinates recursively
    private List<Employee> getAllSubordinates(Employee manager) {
        List<Employee> result = new ArrayList<>();
        Queue<Employee> queue = new LinkedList<>();
        queue.add(manager);

        while (!queue.isEmpty()) {
            Employee current = queue.poll();
            List<Employee> subs = empRepo.findByManager(current);
            result.addAll(subs);
            queue.addAll(subs);
        }

        return result;
    }

    private boolean isApprover(Employee employee, Employee approver) {
        // Check if approver is a manager of employee (or in chain above)
        Employee current = employee.getManager();
        while (current != null) {
            if (current.getId().equals(approver.getId())) return true;
            current = current.getManager();
        }
        return false;
    }

    @Scheduled(cron = "0 0 0 1 1 *") // Runs on Jan 1st every year
    public void resetYearlyLeaves() {
        List<Employee> employees = empRepo.findAll();
        for (Employee emp : employees) {
            emp.setUsedPaidLeaves(0);
        }
        empRepo.saveAll(employees);
    }

}
