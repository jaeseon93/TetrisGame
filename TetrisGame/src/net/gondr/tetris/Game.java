package net.gondr.tetris;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import net.gondr.domain.Block;
import net.gondr.domain.Player;

public class Game {
	private GraphicsContext gc;
	public Block[][] board; //2차원 배열로 테트리스의 게임판 역할
	 
	private double width;
	private double height;
	
	private AnimationTimer mainLoop; //메인루프
	private long before; //이전시간 기록변수저장
	
	private Player player;
	private double blockDownTime = 0; //블럭이 자동으로 내려올때까지 걸리는 시간
	
	private int score = 0; //점수변수. 테트리스에서 한 줄을 없앨때마다 score가 1점씩 오르게됨
	
	
	public Game(Canvas canvas) {
		//캔버스의 너비와 높이를 가져온다.
		width = canvas.getWidth();
		height = canvas.getHeight();
		
		double size = (width - 4) / 10; //각각의 사이즈가 40으로 잡힌것
		
		board = new Block[20][10]; //게임 판 만들고 [width][height]
		
		for(int i = 0; i < 20; i++) {
			for(int j = 0; j < 10; j++) {
				board[i][j] = new Block(j * size + 2, i * size + 2, size);
			}
		}
		
		this.gc = canvas.getGraphicsContext2D();
		mainLoop = new AnimationTimer() { //각프레임마다 호출될 수있는 클래스. 
			@Override
			public void handle(long now) { //now는 나노초 단위로 들어옴.
				update( (now - before) / 1000000000d );
				before = now;
				render();
			}
		};
		
		before = System.nanoTime();		
		
		//플레이어 모양 설정
		player = new Player(board);
		mainLoop.start();
	}
	
	//업데이트 매서드
	public void update(double delta) {
		//매 프레임마다 실행되는 update매서드 블럭의 자동하강 로직을 담당.(시간이 지날 때마다 블럭이 내려오도록)
		//이제 0.5초의 시간마다 블럭이 아래로 하강하게 됩니다. 
		//delta시간은 위에서 AnimationTimer를 통해 넣었기 때문에 정상적으로 작동
		blockDownTime += delta; //0.5초마다 블럭을 아래로 내린다. 이 수치는 난이도 조절기능에서 조절 가능.
		if(blockDownTime >= 0.5) {
			player.down();
			blockDownTime = 0;
		}
	}
	
	public void checkLineStatus() {
		//라인이 꽉 찼는지 체크해주는 매서드(해당 라인을 지우고 스코어를 올려주도록 함)
		// 테트리스 스테이지의 가장 아랫쪽 라인부터 라인이 꽉차있는지를 검사하고(첫번째 for문) 해당줄이 꽉차 있다면 해당 라인을 전부 지운후에 위에 
		//칸부터 해당라인까지 한칸씩 내려주는 역할을 합니다.


		for(int i = 19; i >= 0; i--) { //맨 밑칸부터 검사하면서 올라간다.
			boolean clear = true;
			for(int j = 0; j < 10; j++) {
				if(!board[i][j].getFill()) {
					clear = false; //한칸이라도 비어 있다면 클리어되지 않은 것으로.
					break;
				}
			}
			if(clear) {//해당 줄이 꽉차 있다면
				score++;
				for(int j = 0; j < 10; j++) {
					board[i][j].setData(false, Color.WHITE); //해당 줄 지우고
				}
				//그 위로 한칸씩 다 내린다.
				for(int k = i - 1;  k >= 0; k--) {
					for(int j = 0; j < 10; j++) {
						board[k+1][j].copyData(board[k][j]);
					}
				}
				//첫번째 줄은 비운다.
				for(int j = 0; j < 10; j++) {
					board[0][j].setData(false, Color.WHITE);
				}
				i++;//그리고 한번더 이번줄을 검사하기 위해 i값을 하나 증가시켜 준다.
			}
		}
	}
	
	//렌더 메서드
	public void render() {
		//매 프레임마다 화면을 그려주는 메서드
		//스테이지 그리기( for문으로 순회하며 전부 그려줄 것)
		gc.clearRect(0, 0, width, height); //전부 지우고 새로 그리기
		gc.setStroke(Color.rgb(0, 0, 0)); //검은색으로 외곽선 그리고
		gc.setLineWidth(2);
		gc.strokeRect(0, 0, width, height);
		
		for(int i = 0; i < 20; i++) {
			for(int j = 0; j < 10; j++) {
				board[i][j].render(gc);
			}
		}
	}
	
	public void keyHandler(KeyEvent e) {
		player.keyHandler(e); //키보드 핸들링을 담당하는 매서드
	}
	
}
