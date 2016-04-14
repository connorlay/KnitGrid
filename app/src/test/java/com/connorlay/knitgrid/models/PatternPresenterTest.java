package com.connorlay.knitgrid.models;

import com.connorlay.knitgrid.R;
import com.connorlay.knitgrid.presenters.PatternPresenter;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by connorlay on 2/28/16.
 */
public class PatternPresenterTest {

    private PatternPresenter mPatternPresenter;
    public static final int ROWS = 4;
    public static final int COLUMNS = 4;

    @Before
    public void setup() {
        mPatternPresenter = new PatternPresenter(ROWS, COLUMNS, false);
        mPatternPresenter.setStitch(0, 0, new Stitch("a", R.drawable.k, "", "", true));
        mPatternPresenter.setStitch(0, 1, new Stitch("b", R.drawable.k, "", "", true));
        mPatternPresenter.setStitch(0, 2, new Stitch("c", R.drawable.k, "", "", true));
        mPatternPresenter.setStitch(0, 3, new Stitch("d", R.drawable.k, "", "", true));
        mPatternPresenter.setStitch(1, 0, new Stitch("e", R.drawable.k, "", "", true));
        mPatternPresenter.setStitch(1, 1, new Stitch("f", R.drawable.k, "", "", true));
        mPatternPresenter.setStitch(1, 2, new Stitch("g", R.drawable.k, "", "", true));
        mPatternPresenter.setStitch(1, 3, new Stitch("h", R.drawable.k, "", "", true));
        mPatternPresenter.setStitch(2, 0, new Stitch("i", R.drawable.k, "", "", true));
        mPatternPresenter.setStitch(2, 1, new Stitch("j", R.drawable.k, "", "", true));
        mPatternPresenter.setStitch(2, 2, new Stitch("k", R.drawable.k, "", "", true));
        mPatternPresenter.setStitch(2, 3, new Stitch("l", R.drawable.k, "", "", true));
        mPatternPresenter.setStitch(3, 0, new Stitch("m", R.drawable.k, "", "", true));
        mPatternPresenter.setStitch(3, 1, new Stitch("n", R.drawable.k, "", "", true));
        mPatternPresenter.setStitch(3, 2, new Stitch("o", R.drawable.k, "", "", true));
        mPatternPresenter.setStitch(3, 3, new Stitch("p", R.drawable.k, "", "", true));
    }

    @Test
    public void testEmptyStitchGrid() {
        String expected = "_, _, _, _\n" +
                          "_, _, _, _\n" +
                          "_, _, _, _\n" +
                          "_, _, _, _\n";
        assert(new PatternPresenter(ROWS, COLUMNS, false).toString()).equals(expected);
    }

    @Test
    public void testWidthAndHeight() {
        int width = 5;
        int height = 6;
        PatternPresenter patternPresenter = new PatternPresenter(width, height, false);

        assert(patternPresenter.getRows() == width);
        assert(patternPresenter.getColumns() == height);
    }

    @Test
    public void testIndividualStitch() {
        String expected = "a, b, c, d\n" +
                          "e, f, g, h\n" +
                          "i, 0, k, l\n" +
                          "m, n, o, p\n";

        Stitch stitch = new Stitch("0", R.drawable.k, "", "", true);
        mPatternPresenter.setStitch(2, 1, stitch);

        assert(mPatternPresenter.toString()).equals(expected);
        assert(mPatternPresenter.getStitch(2, 1)).equals(stitch);
    }

    @Test
    public void testAddRowBefore() {
        String expected = "a, b, c, d\n" +
                          "e, f, g, h\n" +
                          "_, _, _, _\n" +
                          "i, j, k, l\n" +
                          "m, n, o, p\n";

        mPatternPresenter.addRowBefore(2);

        assert(mPatternPresenter.toString()).equals(expected);
        assert (mPatternPresenter.getRows() == ROWS + 1);
    }

    @Test
    public void testAddRowAfter() {
        String expected = "a, b, c, d\n" +
                          "e, f, g, h\n" +
                          "i, j, k, l\n" +
                          "_, _, _, _\n" +
                          "m, n, o, p\n";

        mPatternPresenter.addRowAfter(2);

        assert(mPatternPresenter.toString()).equals(expected);
        assert (mPatternPresenter.getRows() == ROWS + 1);
    }

    @Test
    public void testAddColumnBefore() {
        String expected = "a, _, b, c, d\n" +
                          "e, _, f, g, h\n" +
                          "i, _, j, k, l\n" +
                          "m, _, n, o, p\n";

        mPatternPresenter.addColumnBefore(1);

        assert(mPatternPresenter.toString()).equals(expected);
        assert(mPatternPresenter.getColumns() == COLUMNS + 1);
    }

    @Test
    public void testAddColumnAfter() {
        String expected = "a, b, _, c, d\n" +
                          "e, f, _, g, h\n" +
                          "i, j, _, k, l\n" +
                          "m, n, _, o, p\n";

        mPatternPresenter.addColumnAfter(1);

        assert(mPatternPresenter.toString()).equals(expected);
        assert(mPatternPresenter.getColumns() == COLUMNS + 1);
    }

    @Test
    public void testRemoveColumn() {
         String expected = "a, c, d\n" +
                           "e, g, h\n" +
                           "i, k, l\n" +
                           "m, o, p\n";

        mPatternPresenter.removeColumn(1);

        assert(mPatternPresenter.toString()).equals(expected);
        assert(mPatternPresenter.getColumns() == COLUMNS - 1);
    }

    @Test
    public void testRemoveRow() {
        String expected = "a, b, c, d\n" +
                          "i, j, k, l\n" +
                          "m, n, o, p\n";

        mPatternPresenter.removeRow(1);

        assert(mPatternPresenter.toString()).equals(expected);
        assert(mPatternPresenter.getRows() == ROWS - 1);
    }
}

