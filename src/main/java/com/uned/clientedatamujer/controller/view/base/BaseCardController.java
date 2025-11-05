package com.uned.clientedatamujer.controller.view.base;

public interface BaseCardController<T> {
    void setData( T content);
    void setParentController(BaseSubSceneController parent);
}
