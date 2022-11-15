package assignment3.webtech.studentaverages.model;

import ch.qos.logback.core.joran.spi.NoAutoStart;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoAutoStart
public class ResultWithGradeAvg {
    private Student student;
    private double average;
    private String status;
}
