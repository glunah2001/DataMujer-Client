package com.uned.clientedatamujer.controller.view.base;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface BaseCardController<T> {
    void setData( T content);
    void setParentController(BaseSubSceneController parent);
    String textFormatter(LocalDateTime date);
    String textFormatter(LocalDate date);
}
