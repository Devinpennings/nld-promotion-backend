package model;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.util.UUID;

@MappedSuperclass
public class Model {

    @Id
    private String id;

    @PrePersist
    public void onCreate(){
        if (this.id == null) this.id = UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Model)
            if(((Model) o).getId() != null && this.id != null)
                return this.id.equals(((Model) o).getId());

        return this.hashCode() == o.hashCode();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
