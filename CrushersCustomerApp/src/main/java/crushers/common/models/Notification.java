package crushers.common.models;

import java.time.LocalDateTime;

import crushers.common.utils.Storable;

public class Notification implements Storable, Comparable<Notification> {
    private int id = -1;
    private String content;
    private LocalDateTime time;
    private Bank sender;

    public Notification() {
        // empty for jackson
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Bank getSender() {
        return sender;
    }

    public void setSender(Bank sender) {
        this.sender = sender;
    }

    @Override
    public String toString() {
        return sender + " sent at " + time + ":\n" + content;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Notification other = (Notification) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public int compareTo(Notification other) {
        return this.time.compareTo(other.time);
    }
}
