package model;

import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.Objects;

/**
 * @Author liyong.liu
 * @Date 2019/3/11
 **/

@Document(collection = "patients")
@Data
public class Patient extends User {
    public Patient(String id, String id_number, String[] special_disease, String name, String area, Contacts contacts, Object extra_meta) {
        super(id, name, area, contacts, extra_meta);
        this.id_number = id_number;
        this.special_disease = special_disease;
    }

    public Patient() {
    }

    @Indexed
    private String[] special_disease;

    @Indexed
    private String id_number;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Patient patient = (Patient) o;
        return Objects.equals(id_number, patient.id_number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id_number);
    }

    @Override
    public String toString() {
        return "Patient{" + "special_disease=" + Arrays.toString(special_disease) + ", id='" + id + '\'' + ", name='" + name + '\'' + ", area='" + area + '\'' + ", contacts=" + contacts + ", extra_meta=" + extra_meta + '}';
    }
}
