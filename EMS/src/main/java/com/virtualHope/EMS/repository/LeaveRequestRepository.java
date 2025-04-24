package com.virtualHope.EMS.repository;

import com.virtualHope.EMS.model.Employee;
import com.virtualHope.EMS.model.LeaveRequest;
import com.virtualHope.EMS.model.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByEmployee(Employee employee);
    List<LeaveRequest> findByApprover(Employee approver);
    List<LeaveRequest> findByStatusAndApprover(LeaveStatus status, Employee approver);
}