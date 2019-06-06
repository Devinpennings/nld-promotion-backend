package model.fluid;

public interface FluidModel {

    void seed(String raw) throws Exception;

    boolean matches(FluidModel other);

}
