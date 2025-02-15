package by.koronatechshiftcourse.dao;

import by.koronatechshiftcourse.model.Manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ManagerDao {

    private final List<Manager> managers = new ArrayList<>();

    private ManagerDao() {
    }

    private static final class InstanceHolder {
        private static final ManagerDao instance = new ManagerDao();
    }

    public static ManagerDao getInstance() {
        return InstanceHolder.instance;
    }

    public void save(Manager manager) {
        findById(manager.getId())
                .ifPresent(mgr -> deleteById(mgr.getId()));
        managers.add(manager);
    }

    public Optional<Manager> findById(Long id) {
        return managers.stream()
                .filter(mgr -> mgr.getId().equals(id))
                .findFirst();
    }

    public void deleteById(Long id) {
        managers.removeIf(mgr -> mgr.getId().equals(id));
    }

    public List<Manager> findAll() {
        return new ArrayList<>(managers);
    }

    public Optional<Manager> findByDepartmentName(String departmentName) {
        return managers.stream()
                .filter(manager -> manager.getDepartment().equals(departmentName))
                .findFirst();
    }

}