package com.yutong.business.file;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;


public class FileOperController
    implements Initializable {

    @FXML
    private TextField filePath;

    @FXML
    private TextField extNames;

    @FXML
    private TableView tableView;

    @FXML
    private TableColumn oriTextFieldColumn;

    @FXML
    private TableColumn destTextFieldColumn;

    @FXML
    private Label messageLabel;

    @FXML
    private Button executeButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Label label = new Label("");
        tableView.setPlaceholder(label);

        // oriTextFieldColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        // destTextFieldColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        tableView.setEditable(true);

        Callback<TableColumn, TableCell> cellFactory =
                new Callback<TableColumn, TableCell>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        return new EditingCell();
                    }
                };

        oriTextFieldColumn.setCellValueFactory(
                new PropertyValueFactory<FileReplaceTableResult, String>(
                        "ori"));
        oriTextFieldColumn.setCellFactory(cellFactory);
        oriTextFieldColumn.setOnEditCommit(
                new EventHandler<CellEditEvent<FileReplaceTableResult, String>>() {
                    @Override
                    public void handle(
                        CellEditEvent<FileReplaceTableResult, String> t) {
                        t.getTableView().getItems()
                                .get(t.getTablePosition().getRow())
                                .setOri(t.getNewValue());
                    }
                });

        // oriTextFieldColumn.setCellFactory(new Callback<TableColumn,
        // TableCell>() {
        // @Override
        // public TableCell call(
        // TableColumn p) {
        // return new CheckBoxTableCell();
        // }
        // });

        destTextFieldColumn
                .setCellValueFactory(new PropertyValueFactory("dest"));

        ObservableList<FileReplaceTableResult> resultList =
                FXCollections.observableArrayList();
        resultList.add(new FileReplaceTableResult("1", "1"));
        resultList.add(new FileReplaceTableResult("", ""));
        resultList.add(new FileReplaceTableResult("", ""));
        resultList.add(new FileReplaceTableResult("", ""));
        resultList.add(new FileReplaceTableResult("", ""));
        resultList.add(new FileReplaceTableResult("", ""));
        resultList.add(new FileReplaceTableResult("", ""));
        resultList.add(new FileReplaceTableResult("", ""));
        resultList.add(new FileReplaceTableResult("", ""));
        resultList.add(new FileReplaceTableResult("", ""));

        tableView.setItems(resultList);

        EventHandler handler = new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                // System.out.println("Handling event " + event.getEventType());
                outResult();
            }
        };

        executeButton.addEventHandler(KeyEvent.KEY_RELEASED, handler);

    }


    private void outResult() {
        executeButton.setDisable(true);
        messageLabel.setText("执行中...");

        messageLabel.setText("执行完成。");
        executeButton.setDisable(false);

        try {

        }
        catch (Exception exception) {
            String errorMessage = exception.toString();
            messageLabel.setText(errorMessage);
            messageLabel.setTooltip(new Tooltip(errorMessage));
        }
    }

    class EditingCell extends TableCell<FileReplaceTableResult, String> {

        private TextField textField;


        public EditingCell() {
        }


        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                createTextField();
                setText(null);
                setGraphic(textField);
                textField.selectAll();
            }
        }


        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setText(getItem());
            setGraphic(null);
        }


        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            }
            else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                }
                else {
                    setText(getString());
                    setGraphic(null);
                }
            }
        }


        private void createTextField() {
            textField = new TextField(getString());
            textField.setMinWidth(
                    this.getWidth() - this.getGraphicTextGap() * 2);
            textField.focusedProperty()
                    .addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(
                            ObservableValue<? extends Boolean> arg0,
                            Boolean arg1, Boolean arg2) {
                            if (!arg2) {
                                commitEdit(textField.getText());
                            }
                        }
                    });
        }


        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }

}
