package dtu.dk.model;

import javax.persistence.*;
import java.util.*;

public class Printer {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    private String name;
    private Stack<PrintQueueItem> queue = new Stack<PrintQueueItem>();

    public Printer(String name) {
        this.name = name;

        // Pre-populate some print items
        for (int i = 0; i < 10; i++) {
            queue.push(new PrintQueueItem(i, "FileName" + i));
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean print(String fileName) {
        Iterator<PrintQueueItem> iter = queue.iterator();

        while (iter.hasNext()){
            PrintQueueItem current = iter.next();
            if (current.getFileName().equals(fileName)) {
                queue.remove(current);
                return true;
            }
        }

        return false;
    }

    public ArrayList<PrintQueueItem> getQueueItems() {
        return new ArrayList<PrintQueueItem>(queue);
    }

    public String getStatus() {
        return "Is running...";
    }

    public Boolean topQueue(int jobNumber) {
        // Move to top
        Iterator<PrintQueueItem> iter = queue.iterator();
        PrintQueueItem current = null;
        while (iter.hasNext()){
            current = iter.next();
            if (current.getJobNumber() == jobNumber) {
                queue.remove(current);
                break;
            }
       }

        queue.push(current);

        return true;
    }

    public Boolean clearQueue() {
        queue.clear();
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Printer printer = (Printer) o;
        return Objects.equals(name, printer.name) &&
                Objects.equals(id, printer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}