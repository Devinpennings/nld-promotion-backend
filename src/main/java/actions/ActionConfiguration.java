package actions;

import model.Model;
import model.fluid.FluidKeyValueModel;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class ActionConfiguration extends Model {

    private boolean enabled;

    public ActionConfiguration(boolean enabled) {
        this.enabled = enabled;
    }

    protected ActionConfiguration() {
    }

    public abstract String getTitle();

    public abstract String getDescription();

    public abstract String getType();

    public boolean getEnabled() {
        return this.enabled;
    }

    @Transient
    public abstract FluidKeyValueModel getInputType();

}
