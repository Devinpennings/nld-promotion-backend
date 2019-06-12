package model.fluid;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.primitives.Primitives;
import model.Model;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Transient;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Entity
public class FluidKeyValueModel extends Model implements FluidModel {

    @ElementCollection(fetch = FetchType.EAGER)
    private Collection<String> fields;

    @Transient
    private Map<String, Object> values;

    public FluidKeyValueModel() {
    }

    public FluidKeyValueModel(Collection<String> keys) {
        this.fields = keys;
        this.values = new HashMap<>();
        keys.forEach(k -> this.values.put(k, null));
    }

    public Map<String, Object> get() {
        return this.values;
    }

    public Object get(String key, Class<?> type) {

        if (this.values.containsKey(key)) {
            Object value = this.values.get(key);
            if (value != null && value.getClass() == type)  {
                return value;
            }
        }
        return null;
    }

    @Override
    public void seed(String raw) throws Exception {

        try {

            HashMap read = new ObjectMapper().readValue(raw, HashMap.class);

            if (!read.keySet().stream().allMatch(k -> k instanceof  String || Primitives.isWrapperType(k.getClass()) || Primitives.allPrimitiveTypes().contains(k.getClass()))) {
                throw new Exception("Failed to parse incoming data, incoming data types are not of expected type.");
            }

            if (!this.values.keySet().stream().allMatch(k -> read.keySet().stream().anyMatch(ok -> ok.equals(k)))) {
                throw new Exception("Failed to parse incoming data, not all keys present.");
            }

            for (String key : this.values.keySet()) {

                this.values.replace(key, read.get(key));

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean matches(FluidModel other) {

        if (other.getClass() != this.getClass()) {
            return false;
        }

        FluidKeyValueModel otherKvm = (FluidKeyValueModel) other;
        return otherKvm.get().keySet().stream().allMatch(ok -> this.fields.stream().anyMatch(tk -> tk.equals(ok)));

    }

    public Collection<String> getFields() {
        return this.fields;
    }
}
