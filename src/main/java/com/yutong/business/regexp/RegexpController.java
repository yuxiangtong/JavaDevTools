package com.yutong.business.regexp;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import com.yutong.business.regexp.datamodel.RegexpResult;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;


public class RegexpController
    implements Initializable {

    @FXML
    private TextArea cusTextArea;

    @FXML
    private TextArea regexpTextArea;

    @FXML
    private CheckBox pattern_UNIX_LINES;

    @FXML
    private CheckBox pattern_CASE_INSENSITIVE;

    @FXML
    private CheckBox pattern_COMMENTS;

    @FXML
    private CheckBox pattern_MULTILINE;

    @FXML
    private CheckBox pattern_LITERAL;

    @FXML
    private CheckBox pattern_DOTALL;

    @FXML
    private CheckBox pattern_UNICODE_CASE;

    @FXML
    private CheckBox pattern_CANON_EQ;

    @FXML
    private CheckBox quickMatchCheckBox;

    @FXML
    private TableView regexpResultTableView;

    @FXML
    private TableColumn tableColumnIndex;

    @FXML
    private TableColumn tableColumnResult;

    @FXML
    private TableColumn tableColumnStart;

    @FXML
    private TableColumn tableColumnEnd;

    @FXML
    private Label errorLabel;

    /**
     * <p>
     * Hack TooltipBehavior
     */
    static {
        try {
            Tooltip obj = new Tooltip();
            Class<?> clazz = obj.getClass().getDeclaredClasses()[1];
            Constructor<?> constructor =
                    clazz.getDeclaredConstructor(Duration.class, Duration.class,
                            Duration.class, boolean.class);
            constructor.setAccessible(true);
            Object tooltipBehavior = constructor.newInstance(new Duration(250), // open
                    new Duration(5000), // visible
                    new Duration(200), // close
                    false);
            Field fieldBehavior = obj.getClass().getDeclaredField("BEHAVIOR");
            fieldBehavior.setAccessible(true);
            fieldBehavior.set(obj, tooltipBehavior);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableColumnIndex.setCellValueFactory(new PropertyValueFactory("index"));

        tableColumnResult
                .setCellValueFactory(new PropertyValueFactory("result"));

        tableColumnStart.setCellValueFactory(new PropertyValueFactory("start"));

        tableColumnEnd.setCellValueFactory(new PropertyValueFactory("end"));

        // regexpTextArea.setText("^([a-z]{1,})((\\d{1})|(\\d{2})|(\\d{3}))$");
        

    }


    @FXML
    private void quickMatchCheckBoxAction(ActionEvent event) {
        errorLabel.setText("");
        errorLabel.setVisible(false);

        boolean quickMatchCheckBoxFlag = quickMatchCheckBox.isSelected();
        System.out.println("You clicked me!" + quickMatchCheckBoxFlag);

        ObservableList<RegexpResult> regexpResultList =
                FXCollections.observableArrayList();

        try {
            String cusTextAreaText = cusTextArea.getText();
            String regexpTextAreaText = regexpTextArea.getText();

            if (StringUtils.isEmpty(StringUtils.trimToEmpty(cusTextAreaText))) {
                return;
            }

            if (StringUtils
                    .isEmpty(StringUtils.trimToEmpty(regexpTextAreaText))) {
                return;
            }

            Pattern pattern =
                    Pattern.compile(regexpTextAreaText, getRegexFlag());
            Matcher matcher = pattern.matcher(cusTextAreaText);
            int count = 0;
            while (matcher.find()) {
                count++;
                regexpResultList.add(new RegexpResult(count, matcher.group(),
                        matcher.start(), matcher.end()));
            }
        }
        catch (Exception e) {
            String errorMessage = e.getMessage();
            errorLabel.setText(errorMessage);
            errorLabel.setTooltip(new Tooltip(errorMessage));
            errorLabel.setVisible(true);
        }
        regexpResultTableView.setItems(regexpResultList);
    }


    private int getRegexFlag() {
        int flag = 0;
        if (pattern_UNIX_LINES.isSelected()) {
            flag |= 0x1;
        }
        if (pattern_CASE_INSENSITIVE.isSelected()) {
            flag |= 0x2;
        }
        if (pattern_COMMENTS.isSelected()) {
            flag |= 0x4;
        }
        if (pattern_MULTILINE.isSelected()) {
            flag |= 0x8;
        }
        if (pattern_LITERAL.isSelected()) {
            flag |= 0x10;
        }
        if (pattern_DOTALL.isSelected()) {
            flag |= 0x20;
        }
        if (pattern_UNICODE_CASE.isSelected()) {
            flag |= 0x40;
        }
        if (pattern_CANON_EQ.isSelected()) {
            flag |= 0x80;
        }
        return flag;
    }

}
