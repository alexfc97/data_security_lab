package dtu.dk.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class PrintQueueItem {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int jobNumber;
    private String fileName;

    public PrintQueueItem(int jobNumber, String fileName) {
        this.jobNumber = jobNumber;
        this.fileName = fileName;
    }


    public int getJobNumber() {
        return jobNumber;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrintQueueItem prineQueueItem = (PrintQueueItem) o;
        return Objects.equals(jobNumber, prineQueueItem.jobNumber) &&
                Objects.equals(fileName, prineQueueItem.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobNumber, fileName);
    }
}