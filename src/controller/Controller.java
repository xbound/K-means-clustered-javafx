package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import model.ClusterSolver;

public class Controller
{
    @FXML
    private Slider groupsSlider;

    @FXML
    private Pane simPane;

    @FXML
    private Group simGroup;

    @FXML
    private Button pointsButton;

    @FXML
    private Button groupsButton;

    @FXML
    private ComboBox<ClusterSolver.FunctionType> metricBox;

    @FXML
    private Label groupsLabel;

    @FXML
    private Label pointsLabel;

    @FXML
    private Button startButton;

    @FXML
    private Slider pointsSlider;

    @FXML
    private Button stopButton;

    @FXML
    private Button resetButton;

    private ClusterSolver solver;

    @FXML
    void generatePointsClicked(ActionEvent event)
    {
        if (solver.getPoints() != null) this.simGroup.getChildren().removeAll(solver.getPoints());
        solver.generatePoints((int)pointsSlider.getValue(),simPane.getWidth()-10,simPane.getHeight()-10,1);
        simGroup.getChildren().addAll(solver.getPoints());
        pointsButton.setDisable(true);
        if (solver.getClusters() != null) startButton.setDisable(false);
    }

    @FXML
    void generateGroupsClicked(ActionEvent event)
    {
        if (solver.getClusters() != null) this.simGroup.getChildren().removeAll(solver.getClusters());
        solver.generateClusters((int)groupsSlider.getValue(),simPane.getWidth()-10,simPane.getHeight()-10,3.5);
        simGroup.getChildren().addAll(solver.getClusters());
        groupsButton.setDisable(true);
        if (solver.getPoints() != null) startButton.setDisable(false);
    }

    @FXML
    void startClicked(ActionEvent event)
    {
        if (solver.getClusters() != null && solver.getPoints() != null)
        {
            solver.setDistanceFunction(metricBox.getSelectionModel().getSelectedItem());
            solver.setRunning(true);
            solver.start();
        }
    }

    @FXML
    void stopClicked(ActionEvent event)
    {
        solver.setRunning(false);
    }

    @FXML
    void resetClicked(ActionEvent event)
    {
        groupsButton.setDisable(false);
        pointsButton.setDisable(false);
        this.simGroup.getChildren().clear();
        createNewSolverProcess();
    }

    @FXML
    void initialize()
    {
        assert groupsSlider != null : "fx:id=\"groupsSlider\" was not injected: check your FXML file 'sample.fxml'.";
        assert simPane != null : "fx:id=\"simPane\" was not injected: check your FXML file 'sample.fxml'.";
        assert simGroup != null : "fx:id=\"simGroup\" was not injected: check your FXML file 'sample.fxml'.";
        assert pointsButton != null : "fx:id=\"pointsButton\" was not injected: check your FXML file 'sample.fxml'.";
        assert groupsButton != null : "fx:id=\"groupsButton\" was not injected: check your FXML file 'sample.fxml'.";
        assert metricBox != null : "fx:id=\"metricBox\" was not injected: check your FXML file 'sample.fxml'.";
        assert groupsLabel != null : "fx:id=\"groupsLabel\" was not injected: check your FXML file 'sample.fxml'.";
        assert pointsLabel != null : "fx:id=\"pointsLabel\" was not injected: check your FXML file 'sample.fxml'.";
        assert startButton != null : "fx:id=\"startButton\" was not injected: check your FXML file 'sample.fxml'.";
        assert pointsSlider != null : "fx:id=\"pointsSlider\" was not injected: check your FXML file 'sample.fxml'.";
        assert stopButton != null : "fx:id=\"stopButton\" was not injected: check your FXML file 'sample.fxml'.";
        assert resetButton != null : "fx:id=\"resetButton\" was not injected: check your FXML file 'sample.fxml'.";
        pointsSlider.setMin(100);
        pointsSlider.setMax(100000);
        pointsSlider.setBlockIncrement(10);
        pointsSlider.valueProperty().addListener((observable, oldValue, newValue) -> pointsLabel.setText(String.valueOf(newValue.intValue())));
        groupsSlider.valueProperty().addListener((observable, oldValue, newValue) -> groupsLabel.setText(String.valueOf(newValue.intValue())));
        pointsLabel.setText((int)pointsSlider.getMin()+"");
        groupsLabel.setText((int)groupsSlider.getMin()+"");
        metricBox.getItems().addAll(ClusterSolver.FunctionType.values());
        metricBox.getSelectionModel().selectFirst();
        stopButton.setDisable(true);
        startButton.setDisable(true);
        createNewSolverProcess();
    }

    private void createNewSolverProcess()
    {
        this.solver = new ClusterSolver();
        this.solver.onStartEnd(evt ->
        {
            if (solver.isRunning())
            {
                stopButton.setDisable(!stopButton.isDisabled());
                pointsButton.setDisable(true);
                groupsButton.setDisable(true);
                startButton.setDisable(true);
                resetButton.setDisable(true);
            }
            else
            {
                stopButton.setDisable(true);
                pointsButton.setDisable(true);
                groupsButton.setDisable(true);
                startButton.setDisable(true);
                resetButton.setDisable(!stopButton.isDisabled());
            }
        });
    }
}
