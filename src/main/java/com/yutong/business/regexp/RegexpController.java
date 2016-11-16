package com.yutong.business.regexp;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.apache.commons.lang3.StringUtils;
import com.yutong.business.regexp.datamodel.RegexpResult;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;


public class RegexpController
    implements Initializable {

    @FXML
    private TextArea cusTextArea;

    @FXML
    private TextField regexpTextField;

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
        Label label = new Label("");
        regexpResultTableView.setPlaceholder(label);
        
        tableColumnIndex.setCellValueFactory(new PropertyValueFactory("index"));

        tableColumnResult
                .setCellValueFactory(new PropertyValueFactory("result"));

        tableColumnStart.setCellValueFactory(new PropertyValueFactory("start"));

        tableColumnEnd.setCellValueFactory(new PropertyValueFactory("end"));

        // regexpTextArea.setText("^([a-z]{1,})((\\d{1})|(\\d{2})|(\\d{3}))$");

        EventHandler handler = new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                // System.out.println("Handling event " + event.getEventType());
                outResult();
            }
        };

        cusTextArea.addEventHandler(KeyEvent.KEY_RELEASED, handler);
        regexpTextField.addEventHandler(KeyEvent.KEY_RELEASED, handler);
        pattern_CANON_EQ.addEventHandler(MouseEvent.MOUSE_CLICKED, handler);
        pattern_CASE_INSENSITIVE.addEventHandler(MouseEvent.MOUSE_CLICKED,
                handler);
        pattern_COMMENTS.addEventHandler(MouseEvent.MOUSE_CLICKED, handler);
        pattern_DOTALL.addEventHandler(MouseEvent.MOUSE_CLICKED, handler);
        pattern_LITERAL.addEventHandler(MouseEvent.MOUSE_CLICKED, handler);
        pattern_MULTILINE.addEventHandler(MouseEvent.MOUSE_CLICKED, handler);
        pattern_UNICODE_CASE.addEventHandler(MouseEvent.MOUSE_CLICKED, handler);
        pattern_UNIX_LINES.addEventHandler(MouseEvent.MOUSE_CLICKED, handler);

    }


    private void outResult() {
        errorLabel.setText("");
        errorLabel.setVisible(false);

        ObservableList<RegexpResult> regexpResultList =
                FXCollections.observableArrayList();

        try {
            String cusTextAreaText = cusTextArea.getText();
            String regexpTextFieldText = regexpTextField.getText();

            if (StringUtils.isEmpty(StringUtils.trimToEmpty(cusTextAreaText))) {
                return;
            }

            if (StringUtils
                    .isEmpty(StringUtils.trimToEmpty(regexpTextFieldText))) {
                return;
            }

            Pattern pattern =
                    Pattern.compile(regexpTextFieldText, getRegexFlag());
            Matcher matcher = pattern.matcher(cusTextAreaText);
            int count = 0;
            while (matcher.find()) {
                count++;
                regexpResultList.add(new RegexpResult(count, matcher.group(),
                        matcher.start(), matcher.end()));
            }
        }
        catch (PatternSyntaxException patternSyntaxException) {
            String errorMessage = patternSyntaxException.toString();
            errorLabel.setText(errorMessage);
            errorLabel.setTooltip(new Tooltip(errorMessage));
            errorLabel.setVisible(true);
        }
        catch (Exception exception) {
            String errorMessage = exception.toString();
            System.out.println(errorMessage);
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
