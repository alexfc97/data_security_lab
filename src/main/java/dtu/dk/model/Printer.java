package dtu.dk.model;

import javax.persistence.*;
import java.util.Iterator;
import java.util.Objects;
import java.util.Stack;

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

    public Boolean topQueue(int jobNumber) {
        // Move to top
        // PrintQueueItem printQueueItemToMove = queue.

        //Iterator<PrintQueueItem> iter = stack.iterator();

        //while (iter.hasNext()){
        //    System.out.println(iter.next());
       //}

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