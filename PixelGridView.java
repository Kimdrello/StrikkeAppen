package no.hiof.kimandre.strikkeappen;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Canvas;

public class PixelGridView extends View {
    private int numColumns, numRows;
    private int cellWidth, cellHeight;
    private Paint colorPaint = new Paint();
    private Paint linePaint = new Paint();
    private boolean[][] cellChecked;
    private int[][] pixels;

    public PixelGridView(Context context) {
        this(context, null);
    }

    public PixelGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        colorPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void setNumColumns(int numColumns) {
        this.numColumns = numColumns;
        calculateDimensions();
    }

    public void setPixels(int pixelArray[][]){
        pixels = pixelArray;
    }

    public int getNumColumns() {
        return numColumns;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
        calculateDimensions();
    }

    public int getNumRows() {
        return numRows;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calculateDimensions();
    }

    private void calculateDimensions() {
        if (numColumns < 1 || numRows < 1) {
            return;
        }

        cellChecked = new boolean[numColumns][numRows];

        invalidate();
    }

    public void setDimensions(int dimension){
        cellWidth = dimension;
        cellHeight = cellWidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (numColumns == 0 || numRows == 0) {
            return;
        }
        int width = getWidth();
        int height = getHeight();

        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numRows; j++) {
                colorPaint.setColor(pixels[j][i]);
                canvas.drawRect((i + 1) * cellWidth, (j + 1) * cellHeight,
                        i * cellWidth, j * cellHeight,
                        colorPaint);
            }
        }
        for (int i = 1; i < numColumns; i++) {
            canvas.drawLine(i * cellWidth, 0, i * cellWidth, numRows*cellHeight, linePaint);
        }
        for (int i = 1; i < numRows; i++) {
            canvas.drawLine(0, i * cellHeight, numColumns*cellWidth, i * cellHeight, linePaint);
        }
        for (int i = 0; i < numColumns; i++) {
            linePaint.setTextSize(cellWidth/2);
            int space = (i * cellWidth) + ((cellWidth/3));
            if(i < 10){
                space = (i * cellWidth) + ((cellWidth/2)/2);
            }
            canvas.drawText((numColumns - (i+1)) + "", space, (numRows * cellHeight - (cellHeight/2)) + cellHeight, linePaint);
        }
        for (int i = 1; i <= numRows; i++) {
            linePaint.setTextSize(cellHeight/2);
            int space = numColumns * cellWidth;
            if((numRows - i) < 10){
                space = numColumns * cellWidth + (cellWidth/5);
            }
            canvas.drawText((numRows - i) + "", space, i * cellHeight - (cellHeight/3), linePaint);
        }
    }
    //Kilde:
    //https://stackoverflow.com/questions/24842550/2d-array-grid-on-drawing-canvas
}
