package top.liuliyong.publicservice.common.model;

import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @Author liyong.liu
 * @Date 2019/3/11
 **/

@Document(collection = "patients")
//@EqualsAndHashCode(callSuper = false)
@Component
@Data
public class Resident extends User {
    public Resident(String id, String id_number, String name, String area, Contacts contacts, Object extra_meta) {
        super(id, name, area, contacts, extra_meta);
        this.id_number = id_number;
    }

    public Resident() {
    }

    @Indexed
    private String id_number;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Resident resident = (Resident) o;
        return Objects.equals(id_number, resident.id_number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id_number);
    }

    @Override
    public String toString() {
        return "Resident{" + ", id='" + id + '\'' + ", name='" + name + '\'' + ", area='" + area + '\'' + ", contacts=" + contacts + ", extra_meta=" + extra_meta + '}';
    }
}
