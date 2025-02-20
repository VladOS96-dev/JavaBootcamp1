import java.util.*;

public class Program {

    public static final int MAX_STUDENTS = 10;
    public static final int MAX_CLASSES_PER_WEEK = 10;
    public static final int MAX_NAME_LENGTH = 10;
    public static final String[] WEEK_DAYS = {"MO", "TU", "WE", "TH", "FR", "SA", "SU"};
    public static final int START_DAY_INDEX = 1; 

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<String> students = collectStudentNames(scanner);
        List<String> schedule = collectSchedule(scanner);
        Map<String, Map<String, String>> attendance = collectAttendance(scanner);

        printSchedule(students, schedule, attendance);
    }

    
    private static List<String> collectStudentNames(Scanner scanner) {
        List<String> students = new ArrayList<>();
        String line;
        while (!(line = scanner.nextLine()).equals(".")) {
            if (line.length() <= MAX_NAME_LENGTH && students.size() < MAX_STUDENTS) {
                students.add(line);
            }
        }
        return students;
    }

    
    private static List<String> collectSchedule(Scanner scanner) {
        List<String> schedule = new ArrayList<>();
        String line;
        while (!(line = scanner.nextLine()).equals(".")) {
            schedule.add(line);
        }
        return schedule;
    }

    
    private static Map<String, Map<String, String>> collectAttendance(Scanner scanner) {
        Map<String, Map<String, String>> attendance = new HashMap<>();
        String line;
        while (!(line = scanner.nextLine()).equals(".")) {
            String[] parts = line.split(" ");
            String studentName = parts[0];
            String hour = parts[1];
            String day = parts[2];
            String status = parts[3];
            String key = day + " " + hour;
            attendance.computeIfAbsent(studentName, k -> new HashMap<>()).put(key, status);
        }
        return attendance;
    }

    
    public static void printSchedule(List<String> students, List<String> schedule,
                                     Map<String, Map<String, String>> attendance) {
        
        System.out.printf("%-10s", "");
        for (int day = 1; day <= 30; day++) {
            int weekIndex = (START_DAY_INDEX + (day - 1)) % 7;
            for (String task : schedule) {
                int hour = Integer.parseInt(task.split(" ")[0]);
                String weekDay = task.split(" ")[1];
                if (WEEK_DAYS[weekIndex].equals(weekDay)) {
                    System.out.printf("%4d:00 %-2s %2d|", hour, weekDay, day);
                }
            }
        }
        System.out.println();


        for (String student : students) {
            System.out.printf("%-10s", student); 
            for (int day = 1; day <= 30; day++) {
                int weekIndex = (START_DAY_INDEX + (day - 1)) % 7; 
                for (String task : schedule) {
                    int hour = Integer.parseInt(task.split(" ")[0]);
                    String weekDay = task.split(" ")[1];
                    if (WEEK_DAYS[weekIndex].equals(weekDay)) {
                        String key = day + " " + hour;
                        String status = attendance.getOrDefault(student, new HashMap<>()).getOrDefault(key, "");
                        if ("HERE".equals(status)) {
                            System.out.printf("%14s", "1|");
                        } else if ("NOT_HERE".equals(status)) {
                            System.out.printf("%14s", "-1|");
                        } else {
                            System.out.printf("%14s", " |");
                        }
                    }
                }
            }
            System.out.println();
        }
    }
}