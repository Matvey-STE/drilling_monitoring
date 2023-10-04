package org.matveyvs.mapper;

public interface Mapper <T, F> {

    T mapFrom (F object);

}
