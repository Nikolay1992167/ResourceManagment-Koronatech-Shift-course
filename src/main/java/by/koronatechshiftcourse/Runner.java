package by.koronatechshiftcourse;

import by.koronatechshiftcourse.dao.EmployeeDao;
import by.koronatechshiftcourse.dao.ManagerDao;
import by.koronatechshiftcourse.exception.IOFileException;
import by.koronatechshiftcourse.exception.ParameterNotFoundException;
import by.koronatechshiftcourse.service.DataProcessService;
import by.koronatechshiftcourse.service.DepartmentService;
import by.koronatechshiftcourse.service.ManagerService;
import by.koronatechshiftcourse.service.PersonService;
import by.koronatechshiftcourse.util.Constants;
import by.koronatechshiftcourse.util.Property;
import by.koronatechshiftcourse.util.writer.ToConsoleWriter;
import by.koronatechshiftcourse.util.writer.ToFileWriter;
import by.koronatechshiftcourse.util.writer.Writer;

public class Runner {

    public static void main(String[] args) {
        Property.configProperties(args);

        EmployeeDao employeeDao = EmployeeDao.getInstance();
        ManagerDao managerDao = ManagerDao.getInstance();

        DepartmentService departmentService = new DepartmentService(managerDao);
        ManagerService managerService = new ManagerService(managerDao);
        PersonService personService = new PersonService(employeeDao, managerDao);

        try {
            String outputParam = Property.PROPERTIES.get(Constants.OUTPUT) != null
                    ? Property.PROPERTIES.get(Constants.OUTPUT)
                    : Property.PROPERTIES.get(Constants.OUTPUT_O);

            if (outputParam == null) {
                throw new ParameterNotFoundException("output or o");
            }

            Writer writer = switch (outputParam) {
                case Constants.OUTPUT_PARAM_CONSOLE -> new ToConsoleWriter();
                case Constants.OUTPUT_PARAM_FILE -> new ToFileWriter();
                default -> throw new ParameterNotFoundException(Constants.OUTPUT);
            };

            DataProcessService dataProcessService = new DataProcessService(personService, managerService, departmentService, writer);
            dataProcessService.init();

        } catch (IOFileException exception) {
            System.err.println(exception.getMessage());
        } catch (ParameterNotFoundException exception) {
            System.err.printf("Parameter %s not found.%n", exception.getMessage());
        } catch (RuntimeException exception) {
            System.err.println(exception.getMessage());
        }
    }

}