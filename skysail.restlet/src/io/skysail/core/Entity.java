package io.skysail.core;

/**
 * used as common interface for (potentially persistable) entities (with a single
 * identity field called "id").
 * 
 */
public interface Entity {

    String getId();

    //void setId(String id);

}
