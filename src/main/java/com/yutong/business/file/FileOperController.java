package com.yutong.business.file;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import com.yutong.framework.widget.CellFactory;
import javafx.beans.property.SimpleBooleanProperty;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;


public class FileOperController
    implements Initializable {

    @FXML
    private TextField filePathTextField;

    @FXML
    private TextField extNamesTextField;

    @FXML
    private TableView tableView;

    @FXML
    private TableColumn oriTableColumn;

    @FXML
    private TableColumn destTableColumn;

    @FXML
    private TextField oriTextField;

    @FXML
    private TextField destTextField;

    @FXML
    private Button editButton;

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Label messageLabel;

    @FXML
    private Button executeButton;

    private ObservableList<FileReplaceTableResult> resultList =
            FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Label label = new Label("");
        tableView.setPlaceholder(label);
        
        tableView.setEditable(true);

        oriTableColumn.setCellValueFactory(new PropertyValueFactory("ori"));
        destTableColumn.setCellValueFactory(new PropertyValueFactory("dest"));
        
        TableColumn checkBoxColumn = new TableColumn("正则匹配");
        checkBoxColumn.setSortable(false);
        checkBoxColumn.setCellValueFactory(
                new PropertyValueFactory<FileReplaceTableResult, Boolean>("regex"));

        checkBoxColumn.setCellFactory(CellFactory.tableCheckBoxColumn(
                new Callback<Integer, ObservableValue<Boolean>>() {
                    @Override
                    public ObservableValue<Boolean> call(Integer index) {
                        final FileReplaceTableResult tableResult =
                                (FileReplaceTableResult) tableView.getItems().get(index);
                        ObservableValue<Boolean> retval =
                                new SimpleBooleanProperty(tableResult, "regex",
                                        tableResult.getRegex());
                        retval.addListener(new ChangeListener<Boolean>() {
                            @Override
                            public void changed(
                                ObservableValue<? extends Boolean> observable,
                                Boolean oldValue, Boolean newValue) {
                                tableResult.setRegex(newValue);;
                            }
                        });
                        return retval;
                    }
                }));
        tableView.getColumns().add(checkBoxColumn);

        tableView.setItems(resultList);

        ChangeListener<FileReplaceTableResult> changeListener =
                new ChangeListener<FileReplaceTableResult>() {
                    @Override
                    public void changed(
                        ObservableValue<? extends FileReplaceTableResult> observable,
                        FileReplaceTableResult oldValue,
                        FileReplaceTableResult newValue) {

                        if (newValue == null) {
                            return;
                        }

                        /* 表中表格后重置内容，启用按钮 */
                        oriTextField.setText(newValue.getOri());
                        destTextField.setText(newValue.getDest());
                        editButton.setDisable(false);

                        /* 启用 删除按钮 */
                        deleteButton.setDisable(false);
                    }
                };

        /* 监听表格数据选中的行改变 */
        tableView.getSelectionModel().selectedItemProperty()
                .addListener(changeListener);

        editButton.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<Event>() {
                    @Override
                    public void handle(Event event) {
                        int selectedIndex = tableView.getSelectionModel()
                                .getSelectedIndex();
                        if (selectedIndex >= 0) {
                            /* 先删除,在增加 */
                            FileReplaceTableResult tableResult =
                                    new FileReplaceTableResult(
                                            oriTextField.getText(),
                                            destTextField.getText());
                            tableView.getItems().remove(selectedIndex);
                            tableView.getItems().add(selectedIndex,
                                    tableResult);
                        }
                        else {
                            editButton.setDisable(true);
                        }
                    }
                });
        addButton.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<Event>() {
                    @Override
                    public void handle(Event event) {
                        String oriStr = oriTextField.getText();
                        String destStr = destTextField.getText();
                        if ("".equals(oriStr) && "".equals(destStr)) {
                            return;
                        }
                        resultList.add(
                                new FileReplaceTableResult(oriStr, destStr));
                        oriTextField.setText("");
                        destTextField.setText("");
                    }
                });
        deleteButton.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<Event>() {
                    @Override
                    public void handle(Event event) {
                        int selectedIndex = tableView.getSelectionModel()
                                .getSelectedIndex();
                        if (selectedIndex >= 0) {
                            tableView.getItems().remove(selectedIndex);
                        }
                        else {
                            deleteButton.setDisable(true);
                        }
                    }
                });

        EventHandler handler = new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                messageLabel.setText("");
                System.out.println("Handling event " + event.getEventType());
                if (resultList.size() == 0) {
                    return;
                }
                outResult();
            }
        };
        executeButton.addEventHandler(MouseEvent.MOUSE_RELEASED, handler);
    }

    boolean executeFlag = false;


    private void outResult() {
        executeButton.setDisable(true);
        messageLabel.setText("执行中...");
        try {
            /* 文件路径或文件名称 */
            String pathName = filePathTextField.getText();

            /* 替换的文件扩展名集合 */
            List<String> fileExtNameList = new ArrayList<String>();
            String[] extNameArray = extNamesTextField.getText().split(";");
            for (int i = 0; i < extNameArray.length; i++) {
                fileExtNameList.add(extNameArray[i]);
            }

            /* 替换数据信息集合 */
            List<ReplaceData> replaceDataList = new ArrayList<ReplaceData>();
            for (int i = 0; i < resultList.size(); i++) {
                FileReplaceTableResult tableResult = resultList.get(i);
                replaceDataList.add(new ReplaceData(tableResult.getOri(),
                        tableResult.getDest(),tableResult.getRegex()));
            }

            ReplaceModle replaceModle = new ReplaceModle(pathName,
                    fileExtNameList, replaceDataList);
            FileHandle fileHandle = new FileHandle(replaceModle);
            fileHandle.replaceHandle();
            executeFlag = true;
        }
        catch (Exception exception) {
            executeFlag = false;
            String errorMessage = exception.toString();
            messageLabel.setText(errorMessage);
            messageLabel.setTooltip(new Tooltip(errorMessage));
        }
        finally {
            if (executeFlag) {
                messageLabel.setText("执行完成");
            }
            executeButton.setDisable(false);
        }
    }

}
