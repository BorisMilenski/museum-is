package com.borismilenski.museumis.service;

import com.borismilenski.museumis.dao.EmployeeDaoImpl;
import com.borismilenski.museumis.dao.PositionDaoImpl;
import com.borismilenski.museumis.model.Employee;
import com.borismilenski.museumis.model.Position;
import com.google.ortools.sat.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ScheduleService {
    private final JdbcTemplate jdbcTemplate;

    private final int shiftLength = 4; //Shift duration in hours
    final int numDays = 7;
    final int numShifts = 2; //Number of shifts per day

    public ScheduleService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    final StringBuilder output = new StringBuilder();

    public String createSchedule() {



        //Get all employees
        List<Employee> allEmployees = new EmployeeDaoImpl(jdbcTemplate).findAll();

        //Divide employees based on position
        //Map employees to unique ints (necessary for setting the model)
        Map<String, List<Employee>> employeesByPosition =
                allEmployees.stream().collect(
                        Collectors.groupingBy(employee -> employee.getPosition().getName())
                );

//        for(String positionName : allPositionsByName.keySet()){
//            List<Employee> sorted = new ArrayList<>();
//            employeesByPosition.add(
//                    allEmployees.stream()
//                    .filter((employee -> employee.getPosition().equals(positionName)))
//                    .collect(HashMap::new,
//                            (map, employee) -> {
//                                map.put(map.size(), employee);
//                                sorted.add(employee);
//                                },
//                            Map::putAll));
//            allEmployees.removeAll(sorted);
//        }
        employeesByPosition.forEach(this::createScheduleForPosition);

        return output.toString();

    }

    private void createScheduleForPosition(String positionName, List<Employee> employeeList){
        final int[] allEmployees = IntStream.range(0, employeeList.size()).toArray();
        final int[] allDays = IntStream.range(0, numDays).toArray();
        final int[] allShifts = IntStream.range(0, numShifts).toArray();

        int totalNumOfShifts = 0;

        final int[][][] shiftRequests = new int[][][] {
                {
                        {0, 0},
                        {0, 0},
                        {0, 0},
                        {0, 0},
                        {0, 0},
                        {0, 1},
                        {0, 0},
                },
                {
                        {0, 0},
                        {0, 0},
                        {0, 1},
                        {0, 1},
                        {1, 0},
                        {0, 0},
                        {0, 0},
                },
                {
                        {0, 1},
                        {0, 1},
                        {0, 0},
                        {1, 0},
                        {0, 0},
                        {0, 1},
                        {0, 0},
                },
                {
                        {0, 0},
                        {0, 0},
                        {1, 0},
                        {0, 1},
                        {0, 0},
                        {1, 0},
                        {0, 0},
                }
        };

        Map<Integer, Employee> indexedEmployee = employeeList.stream()
                .collect(HashMap::new,
                            (map, employee) -> map.put(map.size(), employee),
                            Map::putAll);

        CpModel model = new CpModel();

        Literal[][][] shifts = new Literal[allEmployees.length][numDays][numShifts];
        for (int n : allEmployees) {
            for (int d : allDays) {
                for (int s : allShifts) {
                    shifts[n][d][s] = model.newBoolVar("shifts_n" + n + "d" + d + "s" + s);
                }
            }
        }

        // Each shift is assigned to at least one employee in the schedule period.
        for (int d : allDays) {
            for (int s : allShifts) {
                List<Literal> employees = new ArrayList<>();
                for (int n : allEmployees) {
                    employees.add(shifts[n][d][s]);
                }
                model.addAtLeastOne(employees);
            }
        }

        // Each employee works between 0 and 2 shifts per day.
        for (int n : allEmployees) {
            for (int d : allDays) {
                LinearExprBuilder work = LinearExpr.newBuilder();
                for (int s : allShifts) {
                    work.add(shifts[n][d][s]);
                }
                model.addLessOrEqual(work,  2);
            }
        }

        //Every day there are at most 2 employees per shift
        for (int d : allDays){
            for (int s : allShifts) {
                LinearExprBuilder maxEmployeesPerShift = LinearExpr.newBuilder();
                for (int n : allEmployees) {
                    maxEmployeesPerShift.add(shifts[n][d][s]);
                }
                model.addLessOrEqual(maxEmployeesPerShift, 2);
            }
        }

        //Each employee works a number of shifts determined by their position
        //An employee can be assigned at most one less shift than specified in their position
        for (int n : allEmployees) {
            LinearExprBuilder numShiftsWorked = LinearExpr.newBuilder();
            int expectedShiftsPerEmployee = (indexedEmployee.get(n).getPosition().getWorkHoursPerWeek() * numDays / 7) / shiftLength;
            totalNumOfShifts+=expectedShiftsPerEmployee;
            int minShiftsPerEmployee = expectedShiftsPerEmployee - 1;
            for (int d : allDays) {
                for (int s : allShifts) {
                    numShiftsWorked.add(shifts[n][d][s]);
                }
            }
            model.addLinearConstraint(numShiftsWorked, minShiftsPerEmployee, expectedShiftsPerEmployee);
        }

        //Optimize for fulfilling the highest number of shift requests
        LinearExprBuilder obj = LinearExpr.newBuilder();
        for (int n : allEmployees) {
            for (int d : allDays) {
                for (int s : allShifts) {
                    obj.addTerm(shifts[n][d][s], shiftRequests[n][d][s]);
                }
            }
        }
        model.maximize(obj);

        CpSolver solver = new CpSolver();
        solver.getParameters().setLinearizationLevel(0);
        solver.getParameters().setMaxTimeInSeconds(30.0);
        CpSolverStatus status = solver.solve(model);

        if (status == CpSolverStatus.OPTIMAL || status == CpSolverStatus.FEASIBLE) {
            output.append(String.format("Solution:%n"));
            for (int d : allDays) {
                output.append(String.format("Day %d%n", d));
                for (int n : allEmployees) {
                    for (int s : allShifts) {
                        if (solver.booleanValue(shifts[n][d][s])) {
                            if (shiftRequests[n][d][s] == 1) {
                                output.append(String.format("  Employee %d works shift %d (requested).%n", n, s));
                            } else {
                                output.append(String.format("  Employee %d works shift %d (not requested).%n", n, s));
                            }
                        }
                    }
                }
            }
            output.append(String.format("Number of shift requests met = %f (out of %d)%n", solver.objectiveValue(), totalNumOfShifts));
        } else {
            output.append("No optimal solution found !");
        }
        // Statistics.
        output.append("Statistics");
        output.append(String.format("conflicts: %d%n", solver.numConflicts()));
        output.append(String.format("  branches : %d%n", solver.numBranches()));
        output.append(String.format("  wall time: %f s%n", solver.wallTime()));


    }



}
