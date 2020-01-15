package net.gondr.views;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ScorePopupController {
	@FXML
	private TextField txtName;
	
	@FXML
	private Label lblScore;
	@FXML
	private Label lblError;
	
	private Stage me;
	private int score = 0;
	
	public void setDialogStage(Stage me) {
		this.me = me;
	}
	
	public void setScore(int score) {
		this.score = score;
		lblScore.setText("당신의 점수는 " + score + "입니다.");
		lblError.setText("");
	}
	
	public void record() {

	}
}
