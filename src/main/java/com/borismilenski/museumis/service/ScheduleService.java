package com.borismilenski.museumis.service;

import com.borismilenski.museumis.dao.EmployeeDaoImpl;
import com.borismilenski.museumis.dao.ScheduleDao;
import com.borismilenski.museumis.model.Employee;
import com.borismilenski.museumis.model.Schedule;
import com.borismilenski.museumis.model.ScheduleSlot;
import com.google.ortools.sat.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ScheduleService extends GenericService<Schedule>{
    private final int shiftLength = 4; //Shift duration in hours
    private final int numDays = 7;
    private final int numShifts = 2; //Number of shifts per day

    public ScheduleService(ScheduleDao scheduleDao) {
        super(scheduleDao);
    }
    private Schedule createSchedules(LocalDate from, LocalDate to, int[][][] shiftRequests ) {
        //Get all employees
        List<Employee> allEmployees = new EmployeeDaoImpl(this.getGenericDao().getJdbcTemplate()).findAll();

        //Divide employees based on position
        Map<String, List<Employee>> employeesByPosition =
                allEmployees.stream().collect(
                        Collectors.groupingBy(employee -> employee.getPosition().getName())
                );

        List<Schedule> allSchedules = new ArrayList<>();
        employeesByPosition.forEach((positionName, employees)-> allSchedules.add(createScheduleForPosition(positionName, employees, shiftRequests)));
        Schedule finalSchedule = new Schedule(UUID.randomUUID(), from, to, new ArrayList<>());
        allSchedules.stream().forEach((schedule -> finalSchedule.getSlots().addAll(schedule.getSlots())));

        //TODO: Save created schedule
        return finalSchedule;
    }

    private Schedule createScheduleForPosition(String positionName, List<Employee> employeeList, int[][][] shiftRequests){
        final StringBuilder output = new StringBuilder();
        Schedule schedule = new Schedule(UUID.randomUUID(), LocalDate.now(), LocalDate.now(), new ArrayList<>());

        final int[] allEmployees = IntStream.range(0, employeeList.size()).toArray();
        final int[] allDays = IntStream.range(0, numDays).toArray();
        final int[] allShifts = IntStream.range(0, numShifts).toArray();

        int totalNumOfShifts = 0;

        //Map employees to unique ints (necessary for setting the model)
        Map<Integer, Employee> indexedEmployee = employeeList.stream()
                .collect(HashMap::new,
                            (map, employee) -> map.put(map.size(), employee),
                            Map::putAll);

        CpModel model = new CpModel();

        Literal[][][] shifts = new Literal[allEmployees.length][numDays][numShifts];
        for (int e : allEmployees) {
            for (int d : allDays) {
                for (int s : allShifts) {
                    shifts[e][d][s] = model.newBoolVar("shifts_n" + e + "d" + d + "s" + s);
                }
            }
        }

        // Each shift is assigned to at least one employee in the schedule period.
        for (int d : allDays) {
            for (int s : allShifts) {
                List<Literal> employees = new ArrayList<>();
                for (int e : allEmployees) {
                    employees.add(shifts[e][d][s]);
                }
                model.addAtLeastOne(employees);
            }
        }

        // Each employee works between 0 and 2 shifts per day.
        for (int e : allEmployees) {
            for (int d : allDays) {
                LinearExprBuilder work = LinearExpr.newBuilder();
                for (int s : allShifts) {
                    work.add(shifts[e][d][s]);
                }
                model.addLessOrEqual(work,  2);
            }
        }

        //Every day there are at most as many employees per shift as determined in their position description
        for (int d : allDays){
            for (int s : allShifts) {
                LinearExprBuilder maxEmployeesPerShift = LinearExpr.newBuilder();
                for (int e : allEmployees) {
                    maxEmployeesPerShift.add(shifts[e][d][s]);
                }
                model.addLessOrEqual(maxEmployeesPerShift, indexedEmployee.get(0).getPosition().getMaxOnShift());
            }
        }

        //Each employee works a number of shifts determined by their position
        //An employee can be assigned fewer shifts only if allowed by his contract
        for (int e : allEmployees) {
            LinearExprBuilder numShiftsWorked = LinearExpr.newBuilder();
            int expectedShiftsPerEmployee = (indexedEmployee.get(e).getPosition().getWorkHoursPerWeek() * numDays / 7) / shiftLength;
            totalNumOfShifts+=expectedShiftsPerEmployee;
            int minShiftsPerEmployee = (indexedEmployee.get(e).getPosition().isCanWorkLessHours())? 0: expectedShiftsPerEmployee;
            for (int d : allDays) {
                for (int s : allShifts) {
                    numShiftsWorked.add(shifts[e][d][s]);
                }
            }
            model.addLinearConstraint(numShiftsWorked, minShiftsPerEmployee, expectedShiftsPerEmployee);
        }

        //Optimize for fulfilling the highest number of shift requests
        LinearExprBuilder obj = LinearExpr.newBuilder();

        for (int e : allEmployees) {
            for (int d : allDays) {
                for (int s : allShifts) {
                    //If there are shift requests, optimize based on their values, otherwise assume all shifts are ok with the employees
                    if (shiftRequests.length > 0){
                        obj.addTerm(shifts[e][d][s], shiftRequests[e][d][s]);
                    }
                    else {
                        obj.addTerm(shifts[e][d][s], 1);
                    }

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
                for (int e : allEmployees) {
                    for (int s : allShifts) {
                        if (solver.booleanValue(shifts[e][d][s])) {
                            //Set the starting day and ending day to the start of the week and offset it by the day
                            //Set the starting and ending hour by determining which shift the employee is working, s==0 => first shift from 09:00:00 to 13:00:00, s == 1 => second shift 14:00:00 to 18:00:00
                            LocalDateTime from = LocalDate.now().with(WeekFields.of(Locale.UK).dayOfWeek(), d + 1).atTime(s==0? 9:14, 0, 0);
                            LocalDateTime to = LocalDate.now().with(WeekFields.of(Locale.UK).dayOfWeek(), d + 1).atTime(s==0? 13:18, 0, 0);
                            ScheduleSlot slot = new ScheduleSlot(UUID.randomUUID(), from, to, indexedEmployee.get(e));
                            schedule.getSlots().add(slot);
                            if (shiftRequests.length > 0) {
                                if (shiftRequests[e][d][s] == 1) {
                                    output.append(String.format("  %s %d works shift %d (requested).%n", positionName, e, s));
                                } else {
                                    output.append(String.format("  %s %d works shift %d (not requested).%n", positionName, e, s));
                                }
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

        System.out.println(output);
        return schedule;
    }

    public Optional<Schedule> getSchedule(LocalDate from, LocalDate to, int[][][] scheduleRequests){
        //Get the schedule from the DAO
        Optional<Schedule> schedule = ((ScheduleDao)this.getGenericDao()).findAll(from, to);
        //If the schedules is empty, aka it hasn't been generated for the selected period, create it, send it to the DAO for persistence and return it to the caller
        if (schedule.isEmpty()){
            schedule = Optional.of(this.getGenericDao().create(this.createSchedules(from, to, scheduleRequests)));
        }
        return schedule;
    }

    public Optional<Schedule> getScheduleForEmployee(UUID employeeId, LocalDate from, LocalDate to, int[][][] scheduleRequests){
        //Get the schedule from the DAO
        Optional<Schedule> schedule = ((ScheduleDao)this.getGenericDao()).find(employeeId, from, to);
        //If the schedules is empty, aka it hasn't been generated for the selected period, create it, send it to the DAO for persistence and return it to the caller
        if(schedule.isEmpty()) {
            this.getGenericDao().create(this.createSchedules(from, to, scheduleRequests));
            schedule = ((ScheduleDao) this.getGenericDao()).find(employeeId, from, to);
        }
        return schedule;
    }

    public Optional<Schedule> getScheduleForEmployee(String employeeWebNiceName,LocalDate from, LocalDate to, int[][][] scheduleRequests){
        //Get the schedule from the DAO
        Optional<Schedule> schedule = ((ScheduleDao)this.getGenericDao()).find(employeeWebNiceName, from, to);
        if(schedule.isEmpty()) {
            this.getGenericDao().create(this.createSchedules(from, to, scheduleRequests));
            schedule = ((ScheduleDao) this.getGenericDao()).find(employeeWebNiceName, from, to);
        }
        return schedule;
    }


}
