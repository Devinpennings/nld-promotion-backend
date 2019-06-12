package events;

import model.Model;
import model.fluid.FluidModel;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy= InheritanceType.TABLE_PER_CLASS)
public abstract class ConsumerConfiguration extends Model {

    public abstract String getTitle();

    public abstract String getDescription();

    public abstract String getEventName();

    public abstract FluidModel getExpectedResponse();

}
