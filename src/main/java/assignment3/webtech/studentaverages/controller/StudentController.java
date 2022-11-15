package assignment3.webtech.studentaverages.controller;

import assignment3.webtech.studentaverages.model.*;
import assignment3.webtech.studentaverages.service.StudentUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RequestMapping("/students")
@RestController
public class StudentController {

    @Autowired
    private StudentUtility studentUtility;

    @GetMapping(value = "/studentID/{id}")
   public ResultWithGradeAvg getStudentDetails(@PathVariable("id") String studentID) throws IOException {
        this.studentUtility.loadData();
       return this.studentUtility.getStudentById(studentID);
   }

   @GetMapping(value = "/failedStudentsCount")
   public int failingStudentCount() throws IOException {
       this.studentUtility.loadData();
       return this.studentUtility.countOfFailedStudents();
   }

   @GetMapping(value = "/failedStudentsList")
    public List<Student> failedStudents() throws IOException {
       this.studentUtility.loadData();
       return this.studentUtility.failedStudentsList();
   }

    @GetMapping(value = "/ninetyPlusAverageStudentsCount")
    public int ninetyPlus() throws IOException {
        this.studentUtility.loadData();
        return this.studentUtility.countOfNinetyPlusAvgStudents();
    }


    @GetMapping(value = "/ninetyPlusAverageStudentsList")
    public List<Student> ninetyPlusList() throws IOException {
        this.studentUtility.loadData();
        return this.studentUtility.ninetyPlusAvgStudentsList();
    }

    @GetMapping(value = "/studentsOrderByAge")
    public List<ResultModelIdDOB> orderByAge() throws IOException {
        this.studentUtility.loadData();
        return this.studentUtility.getStudentsOrderByAge();
    }

    @GetMapping(value = "/meanAge")
    public ResultModel getMeanAge() throws IOException {
        this.studentUtility.loadData();
        return this.studentUtility.getMeanAge();
    }

    @GetMapping(value = "/medianAge")
    public ResultModel getMedianAge() throws IOException {
        this.studentUtility.loadData();
        return this.studentUtility.getMedianAge();
    }

    @GetMapping(value = "/modeAge")
    public ResultModel getModeAge() throws IOException {
        this.studentUtility.loadData();
        return this.studentUtility.getModeAge();
    }

    @GetMapping(value = "/coursesCount")
    public Map<Integer, Long> countOfCourses() throws IOException {
        this.studentUtility.loadData();
        return this.studentUtility.countOfCourses();
    }

    @GetMapping(value = "/courseFrequency")
    public Map<String, FrequencyAvgModel> courseFrequency() throws IOException {
        this.studentUtility.loadData();
        return this.studentUtility.getCourseFrequencyAndAverage();
    }

    @GetMapping(value = "/getMeanAverage")
    public double meanAvg() throws IOException {
        this.studentUtility.loadData();
        return this.studentUtility.getMeanAverage();
    }


}
