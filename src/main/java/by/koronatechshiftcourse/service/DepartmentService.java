package by.koronatechshiftcourse.service;

import by.koronatechshiftcourse.dao.ManagerDao;
import by.koronatechshiftcourse.model.Employee;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DepartmentService {

    private final ManagerDao managerDao;

    public Double getAvgSalary(String department) {
        return managerDao.findByDepartmentName(department)
                .map(manager -> {

                    double totalSalary = manager.getEmployees().stream()
                            .mapToDouble(Employee::getSalary)
                            .sum() + manager.getSalary();

                    int totalCount = manager.getEmployees().size() + 1;

                    return totalSalary / totalCount;
                })
                .orElse(0.0);
    }

    public Integer getEmployeesByDepartmentCount(String department) {
        return managerDao.findByDepartmentName(department)
                .map(manager -> manager.getEmployees().size())
                .orElse(0);
    }

}