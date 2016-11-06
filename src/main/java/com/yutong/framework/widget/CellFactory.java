package com.yutong.framework.widget;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/*
 * CellFactory.java 1.0 2010-2-2
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

public class CellFactory {

    ////// table check box

    public static <S> Callback<TableColumn<S, Boolean>, TableCell<S, Boolean>> tableCheckBoxColumn() {
        return tableCheckBoxColumn(null, null);
    }


    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> tableCheckBoxColumn(
        Callback<Integer, ObservableValue<Boolean>> paramCallback) {
        return tableCheckBoxColumn(paramCallback, null);
    }


    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> tableCheckBoxColumn(
        Callback<Integer, ObservableValue<Boolean>> paramCallback,
        boolean paramBoolean) {
        Callback<T, String> callback = new Callback<T, String>() {
            @Override
            public String call(T t) {
                return ((t == null) ? "" : t.toString());
            }
        };
        return tableCheckBoxColumn(paramCallback, callback);
    }


    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> tableCheckBoxColumn(
        final Callback<Integer, ObservableValue<Boolean>> getSelectedProperty,
        final Callback<T, String> toString) {
        return new Callback<TableColumn<S, T>, TableCell<S, T>>() {
            @Override
            public TableCell<S, T> call(TableColumn<S, T> paramTableColumn) {
                return new CheckBoxTableCell<S, T>(getSelectedProperty,
                        toString);
            }
        };
    }

}
