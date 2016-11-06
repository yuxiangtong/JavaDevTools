package com.yutong.business.bjson;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.yutong.framework.utils.FileUtils;
import com.yutong.framework.utils.PropertiesUtils;
import com.yutong.framework.utils.VelocityUtils;
import com.yutong.framework.widget.CellFactory;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;


public class BJsonController
    implements Initializable {

    private static final Logger logger =
            LogManager.getLogger(BJsonController.class);

    @FXML
    private TableView<JSONObject> tableView;

    @FXML
    private VBox buttonsVBox;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        /* setPlaceholder方法来指定没有数据时的显示内容 */
        Label label = new Label("唉，怎么会没有数据！");
        tableView.setPlaceholder(label);

        tableView.setEditable(true);

        String b_table_json =
                FileUtils.contentToString("resources/bjson/tableJson.json");
        JSONObject jsonObject = JSONObject.fromObject(b_table_json);
        String tableTitle = jsonObject.getString("columnName");
        String[] columnNames = tableTitle.split(",");

        addDocumentColumns(tableView, columnNames);

        ObservableList<JSONObject> observableList =
                FXCollections.observableArrayList();

        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject dataJsonObject = jsonArray.getJSONObject(i);
            dataJsonObject.put("_index", (i + 1));
            dataJsonObject.put("_select", true);
        }
        observableList.addAll(jsonArray);

        tableView.setItems(observableList);

        /* 2.生成对应button */
        Map<String, String> buttonMap = new HashMap<String, String>();

        EventHandler handler = new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                Button button = (Button) event.getSource();

                ObservableList<JSONObject> observableList =
                        tableView.getItems();

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("list", observableList);
                String vmString = VelocityUtils
                        .contentToString(buttonMap.get(button.getText()), map);

                /* 复制到剪切板 */
                // 获取系统剪切板
                Clipboard clipboard =
                        Toolkit.getDefaultToolkit().getSystemClipboard();
                // 构建String数据类型
                StringSelection selection = new StringSelection(vmString);
                // 添加文本到系统剪切板
                clipboard.setContents(selection, null);
            }
        };

        String buttonJsonString = PropertiesUtils
                .getString("resources/bjson/bjson.properties", "bjson.buttons");
        JSONArray buttonJsonArray = JSONArray.fromObject(buttonJsonString);

        for (int i = 0; i < buttonJsonArray.size(); i++) {
            JSONObject buttonJsonObject = buttonJsonArray.getJSONObject(i);
            String buttonName = buttonJsonObject.getString("name");
            String vmpath = buttonJsonObject.getString("vmpath");
            buttonMap.put(buttonName, vmpath);

            Button button = new Button(buttonName);
            button.addEventHandler(ActionEvent.ACTION, handler);
            buttonsVBox.getChildren().add(button);
        }

    }


    private void addDocumentColumns(TableView<JSONObject> tableView,
        String[] columnNames) {
        TableColumn<JSONObject, Boolean> checkBoxColumn =
                new TableColumn<JSONObject, Boolean>("选择");
        checkBoxColumn.setSortable(false);
        checkBoxColumn.setCellValueFactory(
                new PropertyValueFactory<JSONObject, Boolean>("_select"));

        checkBoxColumn.setCellFactory(CellFactory.tableCheckBoxColumn(
                new Callback<Integer, ObservableValue<Boolean>>() {
                    @Override
                    public ObservableValue<Boolean> call(Integer index) {
                        final JSONObject jsonObject =
                                tableView.getItems().get(index);
                        ObservableValue<Boolean> retval =
                                new SimpleBooleanProperty(jsonObject, "_select",
                                        jsonObject.getBoolean("_select"));
                        retval.addListener(new ChangeListener<Boolean>() {
                            @Override
                            public void changed(
                                ObservableValue<? extends Boolean> observable,
                                Boolean oldValue, Boolean newValue) {
                                jsonObject.put("_select", newValue);
                            }
                        });
                        return retval;
                    }
                }));
        tableView.getColumns().add(checkBoxColumn);

        TableColumn<JSONObject, String> indexColumn =
                new TableColumn<JSONObject, String>("序号");
        indexColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<JSONObject, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(
                        CellDataFeatures<JSONObject, String> arg0) {
                        // //bean, bean的名称，值
                        return new SimpleStringProperty(
                                arg0.getValue().getString("_index"));
                    }
                });
        tableView.getColumns().add(indexColumn);

        for (int i = 0; i < columnNames.length; i++) {
            String columnName = columnNames[i];
            TableColumn tableColumn = new TableColumn(columnName);
            tableColumn.setCellValueFactory(
                    new Callback<TableColumn.CellDataFeatures<JSONObject, String>, ObservableValue<String>>() {
                        @Override
                        public ObservableValue<String> call(
                            CellDataFeatures<JSONObject, String> arg0) {
                            JSONObject jsonObject = arg0.getValue();
                            return new SimpleStringProperty((jsonObject == null
                                    || jsonObject
                                            .containsKey(columnName) == false)
                                                    ? "" : jsonObject.getString(
                                                            columnName));
                        }
                    });
            tableView.getColumns().add(tableColumn);
        }

    }

}
