package model.fluid;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.primitives.Primitives;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FluidKeyValueModel implements FluidModel {

    private Map<String, Object> map;

    public FluidKeyValueModel(Collection<String> keys) {
        this.map = new HashMap<>();
        keys.forEach(k -> this.map.put(k, null));
    }

    public Map<String, Object> get() {
        return this.map;
    }

    public Object get(String key, Class<?> type) {

        if (this.map.containsKey(key)) {
            Object value = this.map.get(key);
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

            if (!this.map.keySet().stream().allMatch(k -> read.keySet().stream().anyMatch(ok -> ok.equals(k)))) {
                throw new Exception("Failed to parse incoming data, not all keys present.");
            }

            for (String key : this.map.keySet()) {

                this.map.replace(key, read.get(key));

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
        return otherKvm.get().keySet().stream().allMatch(ok -> this.map.keySet().stream().anyMatch(tk -> tk.equals(ok)));

    }
}
