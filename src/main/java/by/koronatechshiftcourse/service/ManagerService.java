package by.koronatechshiftcourse.service;

import by.koronatechshiftcourse.dao.ManagerDao;
import by.koronatechshiftcourse.exception.ParameterNotFoundException;
import by.koronatechshiftcourse.model.Employee;
import by.koronatechshiftcourse.model.Manager;
import by.koronatechshiftcourse.util.Constants;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
public class ManagerService {

    private final ManagerDao managerDao;

    public List<Manager> getAll() {
        return getAll(Constants.SORT_PARAM_ID, Constants.ORDER_PARAM_ASC);
    }

    public List<Manager> getAll(String sortParam, String sortOrder) {
        List<Manager> managerList = managerDao.findAll();
        managerList.sort(Comparator.comparing(Manager::getDepartment));

        Comparator<Employee> employeeComparator = switch (sortParam) {
            case Constants.SORT_PARAM_ID -> Comparator.comparing(Employee::getId);
            case Constants.SORT_PARAM_NAME -> Comparator.comparing(Employee::getName);
            case Constants.SORT_PARAM_SALARY -> Comparator.comparing(Employee::getSalary);
            default -> throw new ParameterNotFoundException(sortParam);
        };

        if (!sortOrder.equals(Constants.ORDER_PARAM_ASC)) {
            if (sortOrder.equals(Constants.ORDER_PARAM_DESC)) {
                employeeComparator = employeeComparator.reversed();
            } else {
                throw new ParameterNotFoundException(sortOrder);
            }
        }

        for (Manager manager : managerList) {
            manager.getEmployees().sort(employeeComparator);
        }

        return managerList;
    }

}