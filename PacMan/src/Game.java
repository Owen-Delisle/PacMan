import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import javafx.scene.shape.*;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Game extends Application {
	private String moveDirection = "";
	private boolean gameOver = false;
	private boolean fear = false;
	long currentTime = System.currentTimeMillis();
	long endTime = currentTime + 15;
	long lastTime = System.nanoTime();
	double amountOfTicks = 60.0;
	double ns = 1000000000 / amountOfTicks;
	double delta = 0;
	long timer = System.currentTimeMillis();
	int updates = 0;
	int frames = 0;
	boolean running = true;

	public static void main(String[] args) {
		System.out.println("Launch");
		launch(args);
	}

	public void start(Stage theStage) {
		Pane pane = new Pane();
		int collisionSize = 25;
		Image image = new Image("/res/pacMap.png");
		String direction = "";
		BackgroundSize bgSize = new BackgroundSize(545, 585, true, true, true, false);
		BackgroundImage bgImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.CENTER, bgSize);

		PacMan pm = new PacMan();
		PowerPellet pwr1 = new PowerPellet(100, 100);

		Rectangle collisionBox = new Rectangle();
		collisionBox.setX(pm.getX());
		collisionBox.setY(pm.getY());
		collisionBox.setWidth(collisionSize);
		collisionBox.setHeight(collisionSize);
		collisionBox.setArcWidth(20);
		collisionBox.setArcHeight(20);
		collisionBox.setFill(Color.TRANSPARENT);

		Ghost red = new Ghost("red");

		Rectangle redCollisionBox = new Rectangle();
		redCollisionBox.setX(red.getX());
		redCollisionBox.setY(red.getY());
		redCollisionBox.setWidth(collisionSize);
		redCollisionBox.setHeight(collisionSize);
		redCollisionBox.setArcWidth(20);
		redCollisionBox.setArcHeight(20);
		redCollisionBox.setFill(Color.TRANSPARENT);

		Ghost blue = new Ghost("blue");

		Rectangle blueCollisionBox = new Rectangle();
		blueCollisionBox.setX(blue.getX());
		blueCollisionBox.setY(blue.getY());
		blueCollisionBox.setWidth(collisionSize);
		blueCollisionBox.setHeight(collisionSize);
		blueCollisionBox.setArcWidth(20);
		blueCollisionBox.setArcHeight(20);
		blueCollisionBox.setFill(Color.TRANSPARENT);

		Ghost pink = new Ghost("pink");

		Rectangle pinkCollisionBox = new Rectangle();
		pinkCollisionBox.setX(pink.getX());
		pinkCollisionBox.setY(pink.getY());
		pinkCollisionBox.setWidth(collisionSize);
		pinkCollisionBox.setHeight(collisionSize);
		pinkCollisionBox.setArcWidth(20);
		pinkCollisionBox.setArcHeight(20);
		pinkCollisionBox.setFill(Color.TRANSPARENT);

		Ghost orange = new Ghost("orange");

		Rectangle orangeCollisionBox = new Rectangle();
		orangeCollisionBox.setX(orange.getX());
		orangeCollisionBox.setY(orange.getY());
		orangeCollisionBox.setWidth(collisionSize);
		orangeCollisionBox.setHeight(collisionSize);
		orangeCollisionBox.setArcWidth(20);
		orangeCollisionBox.setArcHeight(20);
		orangeCollisionBox.setFill(Color.TRANSPARENT);

		Scene scene = new Scene(pane, 545, 623);
		// Scene clearScene = new Scene(pane, 545, 585);

		pane.setBackground(new Background(bgImage));
		pane.getChildren().add(pm.getImage());
		pane.getChildren().add(red.getRedImage());
		pane.getChildren().add(blue.getRedImage());
		pane.getChildren().add(pink.getRedImage());
		pane.getChildren().add(orange.getRedImage());
		pane.getChildren().add(collisionBox);
		pane.getChildren().add(redCollisionBox);
		pane.getChildren().add(blueCollisionBox);
		pane.getChildren().add(pinkCollisionBox);
		pane.getChildren().add(orangeCollisionBox);
		pane.getChildren().add(pwr1);

		pane.setOnKeyPressed(e -> {
			PacPellet stopDot;
			if (e.getCode() == KeyCode.UP && pm.getUp() == true) {
				moveDirection = "up";
				stopDot = new PacPellet(pm.getX() + 15, pm.getY() - 15);
				// pane.getChildren().add(stopDot);
			}
			if (e.getCode() == KeyCode.LEFT && pm.getLeft() == true) {
				moveDirection = "left";
				stopDot = new PacPellet(pm.getX() - 5, pm.getY() + 15);
				// pane.getChildren().add(stopDot);
			}
			if (e.getCode() == KeyCode.DOWN && pm.getDown() == true) {
				moveDirection = "down";
				stopDot = new PacPellet(pm.getX() + 15, pm.getY() + 35);
				// pane.getChildren().add(stopDot);
			}
			if (e.getCode() == KeyCode.RIGHT && pm.getRight() == true) {
				moveDirection = "right";
				stopDot = new PacPellet(pm.getX() + 35, pm.getY() + 15);
				// pane.getChildren().add(stopDot);
			}
			if (e.getCode() == KeyCode.SPACE) {
				System.out.println(pm.printPosition());
				System.out.println(pm.callB());
			}
		});

		pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
			// @Override
			int count = 0;

			public void handle(MouseEvent event) {
				System.out.println("X : " + event.getSceneX() + " Y : " + event.getSceneY() + "Count : " + count);
				PacPellet marker = new PacPellet((int) event.getSceneX(), (int) event.getSceneY());
				pane.getChildren().add(marker);
				count++;
			}
		});
		AnimationTimer animator = new AnimationTimer() {

			@Override
			public void handle(long arg0) {
				pm.move(moveDirection);
				red.move(red.getCurrent());
				blue.move(blue.getCurrent());
				pink.move(pink.getCurrent());
				orange.move(orange.getCurrent());

				collisionBox.setX(pm.getX());
				collisionBox.setY(pm.getY());

				redCollisionBox.setX(red.getX());
				redCollisionBox.setY(red.getY());

				blueCollisionBox.setX(blue.getX());
				blueCollisionBox.setY(blue.getY());

				pinkCollisionBox.setX(pink.getX());
				pinkCollisionBox.setY(pink.getY());

				orangeCollisionBox.setX(orange.getX());
				orangeCollisionBox.setY(orange.getY());

				if (redCollisionBox.getBoundsInLocal().intersects(collisionBox.getBoundsInLocal())
						|| blueCollisionBox.getBoundsInLocal().intersects(collisionBox.getBoundsInLocal())
						|| pinkCollisionBox.getBoundsInLocal().intersects(collisionBox.getBoundsInLocal())
						|| orangeCollisionBox.getBoundsInLocal().intersects(collisionBox.getBoundsInLocal())) {

					pm.deathSequence();
					red.deathSequence();
					blue.deathSequence();
					pink.deathSequence();
					orange.deathSequence();
				}

				/*
				 * if (collisionBox.getBoundsInLocal().intersects(pwr1.
				 * getBoundsInLocal())) { fear = true; }
				 */

				if (fear == true) {

					long end = currentTime + 15000;

					if (System.currentTimeMillis() < end) {
						red.blueSetImage();
						blue.blueSetImage();
						pink.blueSetImage();
						orange.blueSetImage();
						// pane.getChildren().remove(pwr1);

					}
					if (System.currentTimeMillis() > end) {
						red.setImage("red");
						blue.setImage("blue");
						pink.setImage("pink");
						orange.setImage("orange");
						fear = false;
					}
				}
			}
		};

		animator.start();
		theStage.setScene(scene);
		theStage.show();

		pane.requestFocus();
	}

	public boolean checkBounds() {

		return true;
	}
}

class PacPellet extends Pane {
	private Circle pellet;

	public PacPellet(int x, int y) {
		pellet = new Circle();
		pellet.setFill(Color.WHITE);
		pellet.setCenterX(x);
		pellet.setCenterY(y);
		pellet.setRadius(5);
		getChildren().add(pellet);
	}
}

class PowerPellet extends Circle {
	private Circle powerPellet;

	public PowerPellet(int x, int y) {
		powerPellet = new Circle();
		powerPellet.setFill(Color.WHITE);
		powerPellet.setCenterX(x);
		powerPellet.setCenterY(y);
		powerPellet.setRadius(10);
	}
}

class Ghost extends Pane {
	private ImageView redGhost;
	// private ImageView blueGhost;
	private String redImage = "/res/ghost_red.png";
	// private String blueImage = "/res/ghost_blue.png";
	private int xPosition = 234;
	private int yPosition = 237;
	private int width = 30;
	private int height = 30;
	private boolean up = true;
	private boolean left = true;
	private boolean down = true;
	private boolean right = true;
	private boolean stopped = false;
	private String currentDirection = "left";
	public BufferedImage img;
	private int b;
	private int speed = 2;
	String temp;

	public Ghost(String color) {
		redGhost = new ImageView(new Image("/res/ghost_" + color + ".png"));
		redGhost.setFitHeight(height);
		redGhost.setFitWidth(width);
		redGhost.setLayoutX(xPosition);
		redGhost.setLayoutY(yPosition);

		try {
			File base = new File("src/res/pacMap.png");
			img = ImageIO.read(base);

			// Blue is RGB 25, 42, 134
			int pixelCol = img.getRGB(xPosition, yPosition);
			int r = (pixelCol >> 16) & 0xff;
			b = pixelCol & 0xff;
			int g = (pixelCol >> 8) & 0xff;
			// Color pixel = new Color();//new Color(img.getRGB(50, 50));
			// int red = pixel.getRed();
			// int green = pixel.getGreen();
			// int blue = pixel.getBlue();

			System.out.println("Hey: " + r + " " + g + " " + b);
		} catch (IOException e) {
			System.out.println("Failed making buffered image");
		}
	}

	public void move(String direction) {

		int pixelCol = 0;
		int rightCol = 0;
		int leftCol = 0;
		int upCol = 0;
		int downCol = 0;

		if (direction == "left") {
			leftCol = img.getRGB(xPosition - 5, yPosition + 15);
		}
		if (direction == "right") {
			rightCol = img.getRGB(xPosition + 35, yPosition + 20);
		}
		if (direction == "up") {
			upCol = img.getRGB(xPosition + 15, yPosition - 5);
		}
		if (direction == "down") {
			downCol = img.getRGB(xPosition + 15, yPosition + 35);
		}
		// int r = (pixelCol>>16) & 0xff;
		b = pixelCol & 0xff;
		leftCol = leftCol & 0xff;
		rightCol = rightCol & 0xff;
		upCol = upCol & 0xff;
		downCol = downCol & 0xff;
		// int g = (pixelCol>>8) & 0xff;
		// System.out.println(b);
		if (leftCol > 50) {
			right = true;
			up = true;
			down = true;
			left = false;
			pickDirection();
		}

		if (upCol > 50) {
			left = true;
			up = false;
			down = true;
			right = true;
			pickDirection();
		}

		if (rightCol > 50) {
			left = true;
			up = true;
			down = true;
			right = false;
			pickDirection();
		}

		if (downCol > 50) {
			left = true;
			up = true;
			down = false;
			right = true;
			pickDirection();
		}

		if (leftCol < 50) {
			left = true;
		}

		if (rightCol < 50) {
			right = true;
		}

		if (downCol < 50) {
			down = true;
		}

		if (upCol < 50) {
			up = true;
		}

		if (direction == "up" && up == true) {
			yPosition -= speed;
			// setImage("up");
			currentDirection = "up";
		}
		if (direction == "left" && left == true) {
			xPosition -= speed;
			// setImage("Left");
			currentDirection = "left";
		}
		if (direction == "down" && down == true) {
			yPosition += speed;
			// setImage("down");
			currentDirection = "down";
		}
		if (direction == "right" && right == true) {
			xPosition += speed;
			// setImage("right");
			currentDirection = "right";
		}

		redGhost.setLayoutX(xPosition);
		redGhost.setLayoutY(yPosition);
	}

	public void pickDirection() {
		int number = (int) (Math.random() * 4);
		if (up == true && number == 0) {
			currentDirection = "up";
		}
		if (down == true && number == 1) {
			currentDirection = "down";
		}
		if (left == true && number == 2) {
			currentDirection = "left";
		}
		if (right == true && number == 3) {
			currentDirection = "right";
		}
	}

	public void fear(long currentTime) {
		long end = currentTime + 15000;
		if (System.currentTimeMillis() < end) {
			redGhost.setImage(new Image("/res/ghost_scared.png"));
		}
		if (System.currentTimeMillis() > end) {
			redGhost.setImage(new Image("/res/ghost_blue.png"));
		}
	}

	public void deathSequence() {
		xPosition = 234;
		yPosition = 237;
		redGhost.setLayoutX(234);
		redGhost.setLayoutY(237);
	}

	public String getCurrent() {
		return currentDirection;
	}

	public ImageView getRedImage() {
		return redGhost;
	}

	public int getX() {
		return xPosition;
	}

	public int getY() {
		return yPosition;
	}

	public void setImage(String color) {
		redGhost.setImage(new Image("/res/ghost_" + color + ".png"));
	}

	public void blueSetImage() {
		redGhost.setImage(new Image("/res/ghost_scared.png"));
	}

	public void scaredGhost() {
		redGhost.setImage(new Image("/res/ghost_scared.png"));
	}
}

class PacMan extends Pane {
	private Timeline animation;
	private int xPosition = 267;
	private int yPosition = 347;
	private int width = 30;
	private int height = 30;
	private ImageView pacMan;
	private String image = "/res/pacMan_left.png";
	private boolean up = true;
	private boolean left = true;
	private boolean down = true;
	private boolean right = true;
	private boolean stopped = false;
	private String currentDirection;
	private int b;
	private int speed = 2;

	public BufferedImage img;

	public PacMan() {
		animation = new Timeline(new KeyFrame(Duration.millis(50)));
		animation.setCycleCount(Timeline.INDEFINITE);
		pacMan = new ImageView(new Image(getimage()));
		pacMan.setFitHeight(height);
		pacMan.setFitWidth(width);
		pacMan.setLayoutX(xPosition);
		pacMan.setLayoutY(yPosition);

		try {
			File base = new File("src/res/pacMap.png");
			img = ImageIO.read(base);

			// Blue is RGB 25, 42, 134
			int pixelCol = img.getRGB(xPosition, yPosition);
			int r = (pixelCol >> 16) & 0xff;
			int b = pixelCol & 0xff;
			int g = (pixelCol >> 8) & 0xff;
			// Color pixel = new Color();//new Color(img.getRGB(50, 50));
			// int red = pixel.getRed();
			// int green = pixel.getGreen();
			// int blue = pixel.getBlue();

			System.out.println("Hey: " + r + " " + g + " " + b);
		} catch (IOException e) {
			System.out.println("Failed making buffered image");
		}
	}

	public void play() {
		animation.play();
	}

	public void pause() {
		animation.pause();
	}

	public void move(String direction) {
		int pixelCol = 0;
		if (direction == "left") {
			pixelCol = img.getRGB(xPosition - 5, yPosition + 15);
		}
		if (direction == "right") {
			pixelCol = img.getRGB(xPosition + 35, yPosition + 20);
		}
		if (direction == "up") {
			pixelCol = img.getRGB(xPosition + 15, yPosition - 5);
		}
		if (direction == "down") {
			pixelCol = img.getRGB(xPosition + 15, yPosition + 35);
		}

		b = pixelCol & 0xff;

		if (b > 50) {
			if (currentDirection == "up")
				up = false;
			if (currentDirection == "down")
				down = false;
			if (currentDirection == "right")
				right = false;
			if (currentDirection == "left")
				left = false;
		}

		if (b < 50) {
			up = true;
			down = true;
			left = true;
			right = true;
		}

		if (direction == "up" && up == true) {
			yPosition -= speed;
			setImage("up");
			currentDirection = "up";
		}
		if (direction == "left" && left == true) {
			xPosition -= speed;
			setImage("Left");
			currentDirection = "left";
		}
		if (direction == "down" && down == true) {
			yPosition += speed;
			setImage("down");
			currentDirection = "down";
		}
		if (direction == "right" && right == true) {
			xPosition += speed;
			setImage("right");
			currentDirection = "right";
		}

		if (xPosition > 510 && yPosition > 281 && yPosition < 321 && direction == "right") {
			xPosition = 25;
			yPosition = 290;
		}

		if (xPosition < 50 && yPosition > 281 && yPosition < 321 && direction == "left") {
			xPosition = 510;
			yPosition = 290;
		}

		pacMan.setLayoutX(xPosition);
		pacMan.setLayoutY(yPosition);
	}

	public Rectangle2D getBoundary() {
		return new Rectangle2D(xPosition, yPosition, width, height);
	}

	public void deathSequence() {
		xPosition = 267;
		yPosition = 347;
		pacMan.setLayoutX(267);
		pacMan.setLayoutY(347);
	}

	public String callB() {
		return "Blue is " + b;
	}

	public void setImage(String direction) {
		// pacMan.setImage(new Image("/res/pacMan_left.png"));
		pacMan.setImage(new Image("/res/pacMan_" + direction + ".png"));
	}

	public ImageView getImage() {
		return pacMan;
	}

	public String getimage() {
		return image;
	}

	public String printPosition() {
		return "X: " + xPosition + " Y: " + yPosition;
	}

	public int getX() {
		return xPosition;
	}

	public int getY() {
		return yPosition;
	}

	public boolean getUp() {
		return up;
	}

	public void up() {
		up = true;
	}

	public void notUp() {
		up = false;
	}

	public boolean getDown() {
		return down;
	}

	public void notDown() {
		down = false;
	}

	public void down() {
		down = true;
	}

	public boolean getLeft() {
		return left;
	}

	public void left() {
		left = true;
	}

	public void notLeft() {
		left = false;
	}

	public boolean getRight() {
		return right;
	}

	public void right() {
		right = true;
	}

	public void notRight() {
		right = false;
	}
}