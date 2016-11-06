package com.yutong.framework.widget;

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.util.Callback;


/*
 * CheckBoxTableCell.java 1.0 2010-2-2
 * 
 * Copyright (c) 2012 by Chen Zhiwu
 * All rights reserved.
 * 
 * The copyright of this software is own by the authors.
 * You may not use, copy or modify this software, except
 * in accordance with the license agreement you entered into
 * with the copyright holders. For details see accompanying license
 * terms.
 */
public class CheckBoxTableCell<S, T> extends TableCell<S, T> {
    private final CheckBox checkBox;

    private final boolean showText;

    private final Callback<T, String> toString;

    private final Callback<Integer, ObservableValue<Boolean>> getSelectedProperty;

    private ObservableValue<Boolean> booleanProperty;


    public CheckBoxTableCell() {
        this(null, null);
    }


    public CheckBoxTableCell(
        Callback<Integer, ObservableValue<Boolean>> toString) {
        this(toString, null);
    }


    public CheckBoxTableCell(
        Callback<Integer, ObservableValue<Boolean>> getSelectedProperty,
        Callback<T, String> toString) {
        this.getSelectedProperty = getSelectedProperty;
        this.toString = toString;
        this.showText = toString != null;
        this.checkBox = new CheckBox();
        setAlignment(Pos.CENTER);
        setGraphic(checkBox);

        if (showText) {
            checkBox.setAlignment(Pos.CENTER_LEFT);
        }
    }


    public CheckBoxTableCell(Callback<T, ObservableValue<Boolean>> callback,
        Callback<Integer, ObservableValue<Boolean>> getSelectedProperty,
        Callback<T, String> toString) {
        this.getSelectedProperty = getSelectedProperty;
        this.toString = toString;
        this.showText = toString != null;
        this.checkBox = new CheckBox();
        setAlignment(Pos.CENTER);
        setGraphic(checkBox);

        if (showText) {
            checkBox.setAlignment(Pos.CENTER_LEFT);
        }
    }


    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
            return;
        }
        if (this.showText) {
            setText(this.toString.call(item));
        }
        setGraphic(this.checkBox);
        if (this.booleanProperty instanceof BooleanProperty)
            this.checkBox.selectedProperty().unbindBidirectional(
                    (BooleanProperty) this.booleanProperty);
        ObservableValue localObservableValue = getSelectedProperty();
        if (localObservableValue instanceof BooleanProperty) {
            this.booleanProperty = localObservableValue;
            this.checkBox.selectedProperty()
                    .bindBidirectional((BooleanProperty) this.booleanProperty);
        }
        this.checkBox.visibleProperty()
                .bind(getTableView().editableProperty()
                        .and(getTableColumn().editableProperty())
                        .and(editableProperty()));
    };


    private ObservableValue getSelectedProperty() {
        return ((this.getSelectedProperty != null)
                ? (ObservableValue) this.getSelectedProperty
                        .call(Integer.valueOf(getIndex()))
                : getTableColumn().getCellObservableValue(getIndex()));
    }
}
