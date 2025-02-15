package by.koronatechshiftcourse.service;

import by.koronatechshiftcourse.exception.ParameterNotFoundException;
import by.koronatechshiftcourse.model.Manager;
import by.koronatechshiftcourse.util.Constants;
import by.koronatechshiftcourse.util.FileReaderUtils;
import by.koronatechshiftcourse.util.Property;
import by.koronatechshiftcourse.util.writer.Writer;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DataProcessService {

    private final PersonService personService;
    private final ManagerService managerService;
    private final DepartmentService departmentService;
    private final Writer writer;

    public void init() {
        writer.write(getText());
    }

    private String getText() {
        String pathToFile = Property.PROPERTIES.get(Constants.PATH_TO_JAR_FILE);

        if (pathToFile == null)
            throw new ParameterNotFoundException(Constants.PATH_TO_JAR_FILE);

        List<String> strings = personService.initDao(FileReaderUtils.readFromFile(pathToFile));

        String sortingParameter;
        String sortParam = Property.PROPERTIES.get(Constants.SORT);

        if (sortParam != null) {
            sortingParameter = sortParam;
        } else {
            sortingParameter = Property.PROPERTIES.get(Constants.SORT_S);
        }

        String order = Property.PROPERTIES.get(Constants.ORDER);
        if (order != null && sortingParameter == null)
            throw new ParameterNotFoundException(Constants.SORT);

        List<Manager> all;
        if (sortingParameter != null) {
            all = managerService.getAll(sortingParameter, order);
        } else {
            all = managerService.getAll();
        }

        String collect = all.stream()
                .map(manager ->
                {
                    Integer employeesByDepartmentCount = departmentService.getEmployeesByDepartmentCount(manager.getDepartment());
                    BigDecimal managerSalary = BigDecimal.valueOf(manager.getSalary()).setScale(2, RoundingMode.UP);
                    BigDecimal avgSalary = BigDecimal.valueOf(departmentService.getAvgSalary(manager.getDepartment())).setScale(2, RoundingMode.UP);

                    return manager.getDepartment() + "\n" +
                            "Manager, %s, %s, %s%n".formatted(manager.getId(), manager.getName(), managerSalary) +
                            manager.getEmployees().stream()
                                    .map(employee -> {
                                        BigDecimal employeeSalary = BigDecimal.valueOf(employee.getSalary()).setScale(2, RoundingMode.UP);
                                        return "Employee, %s, %s, %s".formatted(employee.getId(), employee.getName(), employeeSalary);
                                    })
                                    .collect(Collectors.joining("\n")) + "\n" +
                            employeesByDepartmentCount + ", " + avgSalary;
                })
                .collect(Collectors.joining("\n"));

        return collect + "\n\n" + "Некорректные данные:\n" + String.join("\n", strings);
    }

}