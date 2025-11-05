package com.uned.clientedatamujer.controller.view.base;

import javafx.scene.layout.HBox;

public interface BaseFormController<T> {
    void setParentController(BaseSubSceneController parent);
    void setRoot(HBox node);
    T sendData();
}
