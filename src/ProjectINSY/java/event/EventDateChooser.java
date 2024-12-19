package ProjectINSY.java.event;

import ProjectINSY.java.swing.Date.SelectedAction;
import ProjectINSY.java.swing.Date.SelectedDate;

public interface EventDateChooser {

    public void dateSelected(SelectedAction action, SelectedDate date);
}
