package model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.io.Serializable;

@Data
public abstract class User implements Serializable {
    public User(String id, String name, String area, Contacts contacts, Object extra_meta) {
        this.id = id;
        this.name = name;
        this.area = area;
        this.contacts = contacts;
        this.extra_meta = extra_meta;
    }

    public User() {
    }

    @Id
    protected String id;
    protected String name;
    @Indexed
    protected String area;
    protected Contacts contacts;
    protected Object extra_meta;

    @Override
    public String toString() {
        return String.format("User[name='%s', area='%s']", name, area);
    }
}
