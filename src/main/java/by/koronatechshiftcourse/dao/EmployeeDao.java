package by.koronatechshiftcourse.dao;

import by.koronatechshiftcourse.model.Employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeDao {

    private final List<Employee> employees = new ArrayList<>();

    private EmployeeDao() {
    }

    private static final class InstanceHolder {
        private static final EmployeeDao instance = new EmployeeDao();
    }

    public static EmployeeDao getInstance() {
        return InstanceHolder.instance;
    }

    public void save(Employee employee) {
        findById(employee.getId())
                .ifPresent(emp -> deleteById(emp.getId()));
        employees.add(employee);
    }

    public Optional<Employee> findById(Long id) {
        return employees.stream()
                .filter(emp -> emp.getId().equals(id))
                .findFirst();
    }

    public void deleteById(Long id) {
        employees.removeIf(emp -> emp.getId().equals(id));
    }

}