package com.uned.clientedatamujer.controller.view;

public interface BaseCardController<T> {
    void setData( T content);
    void setParentController(BaseSubSceneController parent);
}
