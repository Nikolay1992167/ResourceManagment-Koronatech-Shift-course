package by.koronatechshiftcourse.service;

import by.koronatechshiftcourse.dao.EmployeeDao;
import by.koronatechshiftcourse.dao.ManagerDao;
import by.koronatechshiftcourse.model.Employee;
import by.koronatechshiftcourse.model.Manager;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class PersonService {

    private static final String STRING = "\\s*([^,]+(?:\\s+[^,]+)?)\\s*,\\s*(\\d+)\\s*,\\s*([^,]+(?:\\s+[^,]+)?)\\s*,\\s*(\\d+[.]?\\d*)\\s*,\\s*([^,]+(?:\\s+[^,]+)?)\\s*";
    private static final String MANAGER = "Manager";
    private static final String EMPLOYEE = "Employee";

    private final EmployeeDao employeeDao;
    private final ManagerDao managerDao;

    public List<String> initDao(List<String> stringList) {
        List<String> unprocessedStrings = new ArrayList<>(stringList);
        List<String> createdManagers = createManagers(unprocessedStrings);
        unprocessedStrings.removeAll(createdManagers);
        List<String> createdEmployees = createEmployees(unprocessedStrings);
        unprocessedStrings.removeAll(createdEmployees);
        return unprocessedStrings;
    }

    private List<String> createManagers(List<String> stringList) {
        List<String> createdManagers = new ArrayList<>();
        stringList.forEach(string -> {

            Matcher matcher = Pattern.compile(STRING).matcher(string);

            if (matcher.find()) {
                String position = matcher.group(1);

                if (position.equals(MANAGER)) {
                    Long id = Long.parseLong(matcher.group(2));
                    String name = matcher.group(3);
                    Double salary = Double.parseDouble(matcher.group(4));
                    String department = matcher.group(5);

                    Manager manager = Manager.builder()
                        .id(id)
                        .name(name)
                        .salary(salary)
                        .employees(new ArrayList<>())
                        .department(department)
                        .build();

                    managerDao.save(manager);
                    createdManagers.add(string);
                }
            }
        });
        return createdManagers;
    }

    private List<String> createEmployees(List<String> stringList) {
        List<String> createdEmployees = new ArrayList<>();

        stringList.forEach(string -> {

            Matcher matcher = Pattern.compile(STRING).matcher(string);

            if (matcher.find()) {
                String position = matcher.group(1);

                if (position.equals(EMPLOYEE)) {
                    Long id = Long.parseLong(matcher.group(2));
                    String name = matcher.group(3);
                    Double salary = Double.parseDouble(matcher.group(4));
                    Long departmentId = Long.parseLong(matcher.group(5));

                    Employee employee = Employee.builder()
                        .id(id)
                        .name(name)
                        .salary(salary)
                        .build();
                    managerDao.findById(departmentId).ifPresent(manager -> {
                        employeeDao.save(employee);
                        manager.getEmployees().add(employee);
                        managerDao.save(manager);
                        createdEmployees.add(string);
                    });
                }
            }
        });
        return createdEmployees;
    }

}