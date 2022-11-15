package assignment3.webtech.studentaverages.service;

import assignment3.webtech.studentaverages.model.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.testng.annotations.Test;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.time.Period;

//import static sun.jvm.hotspot.runtime.BasicObjectLock.size;

@Service
public class StudentUtility {
    public ArrayList<Student> studentsList;

    public static ObjectMapper objectMapper = new ObjectMapper();

    public static ArrayList<Student> getStudentDetails() throws IOException {
        InputStream inputStream = new FileInputStream(new File("src/main/resources/studentsList.json"));
        TypeReference<List<Student>> typeReference = new TypeReference<List<Student>>() {
        };
        ArrayList<Student> students = objectMapper.readValue(inputStream, new TypeReference<ArrayList<Student>>() {
        });
        return students;
        //return objectMapper.readValue(inputStream, typeReference);
    }


    public void loadData() throws IOException {
        studentsList = getStudentDetails();
        //Collections.sort(studentsList);
    }

    public ResultWithGradeAvg getStudentById(String studentId) throws IOException {

        double average = 0;
        for(int i =0; i< studentsList.size(); i++){
            if(studentId.equals(studentsList.get(i).getId()))
            {
                for (int j=0; j< studentsList.get(i).getCourses().length; j++){
                    average += studentsList.get(i).getCourses()[j].getGrade();
                }
                average = average/studentsList.get(i).getCourses().length;
                return new ResultWithGradeAvg(studentsList.get(i), average, "Record Found");
            }

        }

            return new ResultWithGradeAvg(null, -1, "No Record Found");



    }


    //Practice --- Ignore
    class CustomPredicate implements Predicate<Student> {
        @Override
        public boolean test(Student student) {
            return Arrays.stream(student.getCourses())
                    .mapToDouble(Student.Course::getGrade)
                    .average().getAsDouble() < 55;
        }
    }
    CustomPredicate customPredicate = new CustomPredicate();


    public int countOfFailedStudents() {

        return studentsList.stream()
                .filter(student -> Arrays.stream(student.getCourses())
                        .mapToDouble(Student.Course::getGrade)
                        .average().getAsDouble() < 55)
                .collect(Collectors.toList()).size();

    }

    public List<Student> failedStudentsList() {
        return studentsList.stream()
                .filter(student -> Arrays.stream(student.getCourses())
                        .mapToDouble(Student.Course::getGrade)
                        .average().getAsDouble() < 55)
                .collect(Collectors.toList());
    }

    public int countOfNinetyPlusAvgStudents() {

        return studentsList.stream()
                .filter(student -> Arrays.stream(student.getCourses())
                        .mapToDouble(Student.Course::getGrade)
                        .average().getAsDouble() > 90)
                .collect(Collectors.toList()).size();

    }

    public List<Student> ninetyPlusAvgStudentsList() {
        return studentsList.stream()
                .filter(student -> Arrays.stream(student.getCourses())
                        .mapToDouble(Student.Course::getGrade)
                        .average().getAsDouble() > 90)
                .collect(Collectors.toList());
    }

    public List<ResultModelIdDOB> getStudentsOrderByAge()
    {
        return studentsList.stream()
                .sorted((s1, s2) -> -1 * (s1.getDob().compareTo(s2.getDob())))
                .map(obj -> new ResultModelIdDOB(obj.getId(),obj.getDob()))
                .collect(Collectors.toList());


    }

    public ResultModel getMeanAge()
    {
        double meanAge =  studentsList.stream()
                        .mapToDouble(student ->
                                Period.between(LocalDate.parse(student.getDob()),
                                        LocalDate.now()).getYears()).average().getAsDouble();
        return new ResultModel("Mean of student's age : ",Double.toString(meanAge));
    }

    public ResultModel getMedianAge()
    {
        int medianIndex = (studentsList.size()+1)/2;
        int studentAge = Period.between(LocalDate.parse(studentsList.get(medianIndex).getDob()), LocalDate.now()).getYears();
        return new ResultModel("Median age is :", Integer.toString(studentAge));
    }

    public ResultModel getModeAge()
    {
        //List of ages for all student
        List<Integer> ageList = studentsList.stream()
                .map(student -> Period.between(LocalDate.parse(student.getDob()), LocalDate.now()).getYears())
                .collect(Collectors.toList());

        //Map contains age with count Where Key (Integer) is age and Value (Long) is count for same age
        Map<Integer, Long> ageCountMap = ageList.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        //First Entry in Map which has max Count
        Optional<Map.Entry<Integer, Long>> maxEntry = ageCountMap.entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue));

        return new ResultModel("Mod Age","Age: "+maxEntry.get().getKey()+" Count: "+maxEntry.get().getValue());

    }

    public Map<Integer, Long> countOfCourses()
    {
        List<Integer> courseCount = studentsList.stream()
                .map(student -> student.getCourses().length)
                .collect(Collectors.toList());

        Map<Integer, Long> courseCountMap = courseCount.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return courseCountMap;
    }

    public Map<String, FrequencyAvgModel> getCourseFrequencyAndAverage()
    {


        Map<String, FrequencyAvgModel> faMap = new HashMap<>();
        for (Student student: studentsList) {
            for (int i = 0; i< student.getCourses().length; i++)
            {
                if (faMap.containsKey(student.getCourses()[i].getName()))
                {
                    FrequencyAvgModel modelObject = faMap.get(student.getCourses()[i].getName());
                    modelObject.setFrequency(modelObject.getFrequency()+1);
                    //Actually setting the sum of grades of each student in particular subject
                    modelObject.setAverage(modelObject.getAverage() + student.getCourses()[i].getGrade());

                }
                else {
                    faMap.put(student.getCourses()[i].getName(), new FrequencyAvgModel(1, student.getCourses()[i].getGrade()));
                }
            }
        }

        for (Map.Entry<String,FrequencyAvgModel> mapElement : faMap.entrySet()) {
            String key = mapElement.getKey();
            //Get average is actually total
            mapElement.getValue().setAverage(mapElement.getValue().getAverage()/mapElement.getValue().getFrequency());
        }

        return new TreeMap<String, FrequencyAvgModel>(faMap);
    }

    //Calculate average of each student
    public double calculateAverage(Student.Course courses[])
    {
        double avg = 0;
        for (int i = 0; i< courses.length; i++)
        {
            avg += courses[i].getGrade();
        }
        return avg/ courses.length;
    }

    public double getMeanAverage()
    {
        double mean = 0;
        for (Student student:studentsList) {
            mean += calculateAverage(student.getCourses());
        }
        return mean/studentsList.size();
    }


}
