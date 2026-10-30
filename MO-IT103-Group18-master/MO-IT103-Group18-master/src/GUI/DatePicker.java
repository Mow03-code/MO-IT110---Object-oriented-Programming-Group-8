package GUI;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DatePicker extends JTextField {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public DatePicker() {
        super(10);
        setEditable(false);
        setHorizontalAlignment(JTextField.CENTER);
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        setEditable(b);
    }

    public void setDate(LocalDate date) {
        if (date != null) {
            setText(date.format(formatter));
        } else {
            setText("");
        }
    }

    public LocalDate getDate() {
        try {
            return LocalDate.parse(getText(), formatter);
        } catch (Exception e) {
            return null;
        }
    }

    // Overloaded methods to support addDateChangeListener calls without compilation errors
    public void addDateChangeListener(Object listener) {
        // Dummy implementation for compatibility
    }

    public void addDateChangeListener(java.util.function.Consumer<?> listener) {
        // Dummy implementation for lambda compatibility
    }
}