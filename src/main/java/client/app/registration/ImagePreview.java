package client.app.registration;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;


public class ImagePreview {
    private int minH;


    private double maxHeight = 300, maxWidth = 300;
    private double currHeight = maxHeight, currWidth = maxWidth;


    private Group root;
    //Rectangles
    private Rectangle mainRect;
    private Rectangle topRect;
    private Rectangle botRect;
    private Rectangle leftRect;
    private Rectangle rightRect;
    private Rectangle background;
    private Anchor[] anchors;

    private double xRight = maxWidth, xLeft = 0, yTop = 0, yBot = maxHeight;
    private double coef;


    private ImageView preview;

    public ImagePreview(int minH)
    {
        this.minH = minH;
        root = new Group();
        root.setAutoSizeChildren(false);

        mainRect = new Rectangle(minH, minH, new Color(1, 1, 1, 0));
        mainRect.setX((currHeight - minH) / 2);
        mainRect.setY((currWidth - minH) / 2);
        mainRect.setCursor(Cursor.MOVE);

        initRectangles();

        anchors = initAnchors();
        MainRectDragListener l = new MainRectDragListener(anchors);
        mainRect.setOnMousePressed(l::startDragging);
        mainRect.setOnMouseDragged(l::dragging);

        preview = new ImageView();

        root.getChildren().addAll(background,preview, leftRect, topRect, rightRect, botRect, mainRect);
        root.getChildren().addAll(anchors);

    }
    public void setPreview(Image image)
    {
        currHeight = image.getHeight(); currWidth = image.getWidth();
        if (currHeight> currWidth)
        {
            coef = currHeight/ maxHeight;
            currHeight = maxHeight;
            currWidth = currWidth / coef;
            preview.setX((maxWidth - currWidth) / 2);
            preview.setY(0);
            xLeft = preview.getX();
            xRight = maxWidth - xLeft;
            yTop = 0;
            yBot = maxHeight;
        }
        else
        {
            coef = currWidth / maxWidth;
            currWidth = maxWidth;
            currHeight /= coef;
            preview.setX(0);
            preview.setY((maxHeight - currHeight) / 2);
            xLeft = 0;
            xRight = maxWidth;
            yTop = preview.getY();
            yBot = maxHeight - yTop;
        }
        preview.setImage(image);
        preview.setFitWidth(currWidth);
        preview.setFitHeight(currHeight);
    }

    Image getImage()
    {
        return preview.getImage();
    }
    double[] getBounds()
    {
        return new double[]{coef*(anchors[0].getCenterX() - xLeft), coef*(anchors[0].getCenterY() - yTop),
                coef*(anchors[1].getCenterY() - anchors[0].getCenterY())};
    }

    private void initRectangles()
    {
        //Setting the left rectangle
        Color c = new Color(0.4706, 0.4706, 0.4706, 0.4196);
        leftRect = new Rectangle(0,0, mainRect.getX(), currHeight);
        leftRect.setFill(c);
        leftRect.setStrokeWidth(0);
        //Setting the right rectangle
        rightRect = new Rectangle(currHeight - (currHeight - mainRect.getWidth() - mainRect.getX()), 0,
                currHeight - mainRect.getWidth() - mainRect.getX(), currHeight);
        rightRect.setFill(c);
        //Setting the upper rectangle
        topRect = new Rectangle(leftRect.getWidth(),0,
                mainRect.getWidth(), mainRect.getY());
        topRect.setFill(c);
        //Setting the bottom rectangle
        botRect = new Rectangle(leftRect.getWidth(), mainRect.getY()+mainRect.getHeight(),
                mainRect.getWidth(), currHeight - mainRect.getHeight() - topRect.getHeight());
        botRect.setFill(c);
        //Setting the background rectangle
        background = new Rectangle(0,0, currHeight, currHeight);
        background.setFill(new Color(0.0431, 0.6392, 0.6235, 1));
    }
    private Anchor[] initAnchors()
    {
        Anchor[] anchors = new Anchor[2];
        DoubleProperty posX = new SimpleDoubleProperty(mainRect.getX());
        posX.addListener((observable, oldValue, newValue) -> {
            mainRect.setX(newValue.doubleValue());
            mainRect.setWidth(mainRect.getWidth() + (oldValue.doubleValue() - newValue.doubleValue()));

            leftRect.setWidth(newValue.doubleValue());
            //Upper rectangle
            topRect.setX(leftRect.getWidth());
            topRect.setWidth(mainRect.getWidth());
            //bottom rectangle
            botRect.setWidth(mainRect.getWidth());
            botRect.setX(newValue.doubleValue());
        });
        DoubleProperty posY = new SimpleDoubleProperty(mainRect.getY());
        posY.addListener(((observable, oldValue, newValue) -> {
            mainRect.setY(newValue.doubleValue());
            mainRect.setHeight(mainRect.getHeight() + (oldValue.doubleValue() - newValue.doubleValue()));
            topRect.setHeight(mainRect.getY());

        }));
        anchors[0] = new Anchor(posX, posY);

        DoubleProperty posX1 = new SimpleDoubleProperty(mainRect.getX() + mainRect.getWidth());
        posX1.addListener(((observable, oldValue, newValue) -> {
            mainRect.setWidth(mainRect.getWidth() - (oldValue.doubleValue()-newValue.doubleValue()));

            //Right rectangle
            rightRect.setWidth(maxHeight - newValue.doubleValue());
            rightRect.setX(maxHeight - rightRect.getWidth());
            //Upper rectangle
            topRect.setWidth(mainRect.getWidth());
            //Bottom rectangle
            botRect.setWidth(mainRect.getWidth());

        }));

        DoubleProperty posY1 = new SimpleDoubleProperty(mainRect.getY() + mainRect.getHeight());
        posY1.addListener(((observable, oldValue, newValue) -> {
            mainRect.setHeight(mainRect.getHeight() - (oldValue.doubleValue()-newValue.doubleValue()));

            botRect.setY(newValue.doubleValue());
            botRect.setHeight(maxWidth -newValue.doubleValue());
        }));
        anchors[1] = new Anchor(posX1, posY1);

        return anchors;
    }



    public Group getRoot() {
        return root;
    }
    private boolean canResize(double newPosX, double newPosY, Anchor thisAnchor)
    {
        Anchor anotherAnchor = anchors[0] == thisAnchor? anchors[1] : anchors[0];
        return (newPosX> xLeft && newPosX < xRight) && (newPosY>yTop&& newPosY< yBot)&&
                Math.abs(newPosX - anotherAnchor.getCenterX()) > minH;
    }
    private boolean canMove(double dx, double dy)
    {
        return (anchors[0].getCenterX() + dx > xLeft) &&
                (anchors[0].getCenterY() + dy > yTop) &&
                (anchors[1].getCenterY() + dy < yBot) &&
                (anchors[1].getCenterX() + dx < xRight);
    }
    private class Anchor extends Circle
    {
        private final DoubleProperty x,y;
        private double xVal, yVal;
        Anchor(DoubleProperty x, DoubleProperty y)
        {
            super(x.get(), y.get(), 8, new Color(1, 1, 1, 1));
            this.setCursor(Cursor.SE_RESIZE);

            this.x = x;
            this.y = y;

            this.x.bind(centerXProperty());
            this.y.bind(centerYProperty());

            this.setOnMousePressed(this::startDrag);
            this.setOnMouseDragged(this::onDrag);
        }


        private void startDrag(MouseEvent e)
        {
            //System.out.println("x = "+e.getX()+" , y = " + e.getY());
            this.xVal = e.getX();
            this.yVal = e.getY();
        }

        private void onDrag(MouseEvent e)
        {

            double dx = e.getX() - xVal, dy = e.getY() - yVal;
            double addition = Math.abs(dx)>Math.abs(dy)? dx:dy;
            if (canResize(getCenterX()+addition, getCenterY()+addition, this)) {
                moveOn(addition, addition);
            }
            xVal = e.getX(); yVal = e.getY();
        }
        void moveOn(double dx, double dy)
        {
            this.setCenterX(getCenterX() + dx);
            this.setCenterY(getCenterY() + dy);
        }
    }
    private class MainRectDragListener
    {
        private double x, y;
        private Anchor[] anchors;
        MainRectDragListener(Anchor[] anchors)
        {
            this.anchors = anchors;
        }
        void startDragging(MouseEvent e)
        {
            x = e.getX(); y = e.getY();
        }
        void dragging(MouseEvent e)
        {
            double dx = e.getX() - x, dy = e.getY() - y;

            if (canMove(dx, dy))
            {
                for (Anchor anchor : anchors) {
                    anchor.moveOn(dx, dy);
                }
            }
            x = e.getX(); y = e.getY();
        }
    }
}
