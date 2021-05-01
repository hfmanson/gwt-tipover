package nl.mansoft.tipover.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.Image;

class CratePuzzlePiece {
	public CratePuzzlePiece flip(int i, int j) {
		if (i > 1 || i < -1 || j > 1 || j < -1)
			return null;
		if (i == 0 && j == 0)
			return null;
		CratePuzzlePiece cratepuzzlepiece = new CratePuzzlePiece(this);
		cratepuzzlepiece.upright = false;
		if (i == 1) {
			cratepuzzlepiece.x++;
			int k = cratepuzzlepiece.thickness;
			cratepuzzlepiece.thickness = cratepuzzlepiece.width;
			cratepuzzlepiece.width = k;
		}
		if (i == -1) {
			cratepuzzlepiece.x -= cratepuzzlepiece.thickness;
			int l = cratepuzzlepiece.thickness;
			cratepuzzlepiece.thickness = cratepuzzlepiece.width;
			cratepuzzlepiece.width = l;
		}
		if (j == 1) {
			cratepuzzlepiece.y++;
			int i1 = cratepuzzlepiece.thickness;
			cratepuzzlepiece.thickness = cratepuzzlepiece.height;
			cratepuzzlepiece.height = i1;
		}
		if (j == -1) {
			cratepuzzlepiece.y -= cratepuzzlepiece.thickness;
			int j1 = cratepuzzlepiece.thickness;
			cratepuzzlepiece.thickness = cratepuzzlepiece.height;
			cratepuzzlepiece.height = j1;
		}
		cratepuzzlepiece.baseX += i;
		cratepuzzlepiece.baseY += j;
		return cratepuzzlepiece;
	}

	public boolean blockAt(int i, int j) {
		if (i < x || j < y)
			return false;
		if (i > (x + width) - 1)
			return false;
		return j <= (y + height) - 1;
	}

	public int x;
	public int y;
	public int width;
	public int height;
	public int thickness;
	public boolean upright;
	public int baseX;
	public int baseY;

	CratePuzzlePiece(int i, int j, int k, int l, int i1, boolean flag) {
		x = i;
		y = j;
		baseX = i;
		baseY = j;
		width = k;
		height = l;
		thickness = i1;
		upright = flag;
	}

	CratePuzzlePiece(int i, int j, int k) {
		x = i;
		y = j;
		baseX = i;
		baseY = j;
		width = 1;
		height = 1;
		thickness = k;
		upright = true;
	}

	CratePuzzlePiece(CratePuzzlePiece cratepuzzlepiece) {
		x = cratepuzzlepiece.x;
		y = cratepuzzlepiece.y;
		baseX = cratepuzzlepiece.baseX;
		baseY = cratepuzzlepiece.baseY;
		width = cratepuzzlepiece.width;
		height = cratepuzzlepiece.height;
		thickness = cratepuzzlepiece.thickness;
		upright = cratepuzzlepiece.upright;
	}
}

class CratePuzzleState {

	public boolean blockAt(int i, int j) {
		for (int k = 0; k < nPieces; k++)
			if (pieces[k].blockAt(i, j))
				return true;

		return false;
	}

	public boolean blockAt(int i, int j, int k, int l) {
		for (int i1 = i; i1 < i + k; i1++) {
			for (int j1 = j; j1 < j + l; j1++)
				if (blockAt(i1, j1))
					return true;

		}

		return false;
	}

	public boolean pieceOverlaps(CratePuzzlePiece cratepuzzlepiece) {
		return blockAt(cratepuzzlepiece.x, cratepuzzlepiece.y,
				cratepuzzlepiece.width, cratepuzzlepiece.height);
	}

	public int pieceWithBlockAt(int i, int j) {
		for (int k = 0; k < nPieces; k++)
			if (pieces[k].blockAt(i, j))
				return k;

		return -1;
	}

	public int getCurBlockI() {
		return pieceWithBlockAt(moverX, moverY);
	}

	public void setPiece(int i, CratePuzzlePiece cratepuzzlepiece) {
		pieces[i] = new CratePuzzlePiece(cratepuzzlepiece);
	}

	public void addPiece(CratePuzzlePiece cratepuzzlepiece) {
		CratePuzzlePiece acratepuzzlepiece[] = new CratePuzzlePiece[nPieces + 1];
		System.arraycopy(pieces, 0, acratepuzzlepiece, 0, nPieces);
		acratepuzzlepiece[nPieces] = new CratePuzzlePiece(cratepuzzlepiece);
		pieces = acratepuzzlepiece;
		nPieces++;
	}

	public boolean equals(Object obj) {
		CratePuzzleState cratepuzzlestate = (CratePuzzleState) obj;
		if (cratepuzzlestate.nPieces != nPieces)
			return false;
		if (cratepuzzlestate.moverX != moverX)
			return false;
		if (cratepuzzlestate.moverY != moverY)
			return false;
		for (int i = 0; i < nPieces; i++)
			if (!cratepuzzlestate.pieces[i].equals(pieces[i]))
				return false;

		return true;
	}

	public CratePuzzlePiece pieces[];
	public int nPieces;
	public int moverX;
	public int moverY;

	CratePuzzleState(CratePuzzleState cratepuzzlestate) {
		moverX = cratepuzzlestate.moverX;
		moverY = cratepuzzlestate.moverY;
		nPieces = cratepuzzlestate.nPieces;
		pieces = new CratePuzzlePiece[nPieces];
		for (int i = 0; i < nPieces; i++)
			pieces[i] = new CratePuzzlePiece(cratepuzzlestate.pieces[i]);

	}

	CratePuzzleState(int i, int j, CratePuzzlePiece cratepuzzlepiece) {
		nPieces = 1;
		pieces = new CratePuzzlePiece[1];
		pieces[0] = new CratePuzzlePiece(cratepuzzlepiece);
		moverX = i;
		moverY = j;
	}

	CratePuzzleState(CratePuzzleState cratepuzzlestate,
			CratePuzzlePiece cratepuzzlepiece) {
		nPieces = cratepuzzlestate.nPieces + 1;
		pieces = new CratePuzzlePiece[nPieces];
		for (int i = 0; i < nPieces - 1; i++)
			pieces[i] = new CratePuzzlePiece(cratepuzzlestate.pieces[i]);

		pieces[nPieces - 1] = new CratePuzzlePiece(cratepuzzlepiece);
		moverX = cratepuzzlestate.moverX;
		moverY = cratepuzzlestate.moverY;
	}
}

public class TipOver implements EntryPoint, KeyDownHandler, ClickHandler {

	Canvas canvas;
	Context2d context;
	static final int canvasHeight = 330;
	static final int canvasWidth = 370;
	static final String divTagId = "canvasExample"; // must match div tag in
													// html file
	private String titleStr;
	private String encStr;
	private String solnStr;
	private String allTitleStr[] = 
	{
		"Beginner #1",
		"Beginner #2",
		"Beginner #3",
		"Beginner #4",
		"Beginner #5",
		"Beginner #6",
		"Beginner #7",
		"Beginner #8",
		"Beginner #9",
		"Beginner #10",
		"Intermediate #1",
		"Intermediate #2",
		"Intermediate #3",
		"Intermediate #4",
		"Intermediate #5",
		"Intermediate #6",
		"Intermediate #7",
		"Intermediate #8",
		"Intermediate #9",
		"Intermediate #10",
		"Advanced #1",
		"Advanced #2",
		"Advanced #3",
		"Advanced #4",
		"Advanced #5",
		"Advanced #6",
		"Advanced #7",
		"Advanced #8",
		"Advanced #9",
		"Advanced #10",
		"Expert #1",
		"Expert #2",
		"Expert #3",
		"Expert #4",
		"Expert #5",
		"Expert #6",
		"Expert #7",
		"Expert #8",
		"Expert #9",
		"Expert #10"	
	};
	private String allEncStr[] =
	{
		"C664214422143303013",
		"C660033004403212512331054553",
		"C664114103503414141554",
		"C665553554054042003532103403",
		"C661055102504312123332444253551",
		"C663155312123044252354551",
		"C663530301112232043242342352",
		"C660055003113213223522532044551",
		"C662244222133232334043244441052",
		"C660541411123222323332342054",
		"C663151314122323152223212514",
		"C665345403114512123523232533451",
		"C664105302313414322142242051",
		"C665005504054142132113322422",
		"C662310101503123422132232332242052",
		"C660244003013112212023441153254",
		"C664214202302213223422522532141343252",
		"C665205012514022522033532051252353453",
		"C660054002102503012412422133044142541",
		"C663350501113412422332542153452",
		"C665215004302403014022522033332432151",
		"C660310101012213022123034132044052252",
		"C660550501312412222132232042142052",
		"C664053003403112312222422332531043442153552",
		"C662254102502014112213222132433142541052153352452",
		"C665504002102402503112513041542154452553",
		"C661100001202303403504112022032432443153554",
		"C661440401313122322422532142243053453",
		"C660245003404504212412023122132232432052451",
		"C664100001202302403504112412342443152352",
		"C663253312122222322423332531252453",
		"C665322003402112313512221132332532043443152",
		"C662055003104202013112022533342442052353551",
		"C664352002102202013112413322521432442053253352452",
		"C660150103203501014112212313412322522242352452552",
		"C661250202303501412122322132332043142243052552",
		"C663153102402212312412022322422132531442454",
		"C663125002302502012112212312322422332043343251453553",
		"C664203004502013122222322422031232333432142243053152552",
		"C663215302403312222322232042242443151"	
	};
	private String allSolnStr[] =
	{
		"17N 4W 7S",
		"x",
		"x",
		"36W 25E 31N 2S 1E 5S",
		"x",
		"x",
		"x",
		"x",
		"x",
		"x",
		"15S 32N 14N 16W 10S 9E",
		"x",
		"x",
		"6S 16W 8E 17S 26E 20S",
		"x",
		"x",
		"x",
		"x",
		"x",
		"x",
		"x",
		"x",
		"x",
		"x",
		"x",
		"x",
		"x",
		"x",
		"x",
		"x",
		"x",
		"x",
		"x",
		"x",
		"x",
		"x",
		"x",
		"x",
		"x",
		"x"		
	};
	private String rankShort[] = { "New", "Beg", "Int", "Adv", "Exp" };
	private String rankLong[] = { "New", "Beginner", "Intermediate",
			"Advanced", "Expert" };
	private int currentPuzNum;
	private int nPuzzles;

    private int spacing;
    private int ex;
    private int ey;
    private int exBoard;
    private int eyBoard;
    private static final int maxSqsz = 35;
	private int width;
	private int height;
	private ImageElement imgBoard;
	private ImageElement imgLeftArrow;
	private ImageElement imgRightArrow;
	private ImageElement imgRedoButton;
	private ImageElement imgUndoButton;
	private ImageElement imgHelpButton;
	private ImageElement imgRestartButton;
	private ImageElement imgRedoButtonGreyed;
	private ImageElement imgUndoButtonGreyed;
	private ImageElement imgRestartButtonGreyed;
	private ImageElement imgUpright[];
	private ImageElement imgBase[];
	private ImageElement imgSide[];
    private Rectangle hotSpot[];
	private int clickX;
	private int clickY;
	private CratePuzzleState start;
	private Point end;
	private Point location;
	private char puzzleType;
	private CratePuzzleState saveLocation[];
	private int savePieceMoved[];
	private int moveI;
	private int maxRedoI;
    private static CssColor moverColor = CssColor.make(192, 0, 0);
    private String statsFont;
    private String actionFont;
    
    public static final Resources resources = GWT.create(Resources.class);

    public TipOver() {
		imgUpright = new ImageElement[5];
		imgBase = new ImageElement[5];
		imgSide = new ImageElement[5];
        saveLocation = new CratePuzzleState[2000];
        savePieceMoved = new int[2000];
        hotSpot = new Rectangle[6];

        //statsFont = new Font("SansSerif", 1, 12);
        statsFont = "12pt sans-serif";
        actionFont = "italic 36pt sans-serif";
        nPuzzles = allSolnStr.length;
	}
    
	public static ImageElement getImageResource(ImageResource imageResource) {
		return ImageElement.as(new Image(imageResource).getElement());
	}

	private void loadImages() {
		imgLeftArrow = getImageResource(resources.leftArrow());
		imgRightArrow = getImageResource(resources.rightArrow());
		imgUndoButton = getImageResource(resources.undoButton());
		imgUndoButtonGreyed = getImageResource(resources.undoButtonGreyed());
		imgRedoButton = getImageResource(resources.redoButton());
		imgRedoButtonGreyed = getImageResource(resources.redoButtonGreyed());
		imgHelpButton = getImageResource(resources.helpButton());
		imgRestartButton = getImageResource(resources.restartButton());
		imgRestartButtonGreyed = getImageResource(resources.restartButtonGreyed());
		imgBoard = getImageResource(resources.board());
		imgUpright[1] = getImageResource(resources.red_upright());
		imgUpright[2] = getImageResource(resources.yellow_upright());
		imgBase[2] = getImageResource(resources.yellow_base());
		imgSide[2] = getImageResource(resources.yellow_side());
		imgUpright[3] = getImageResource(resources.green_upright());
		imgBase[3] = getImageResource(resources.green_base());
		imgSide[3] = getImageResource(resources.green_side());
		imgUpright[4] = getImageResource(resources.blue_upright());
		imgBase[4] = getImageResource(resources.blue_base());
		imgSide[4] = getImageResource(resources.blue_side());
	}

	@Override
	public void onModuleLoad() {
		canvas = Canvas.createIfSupported();

		if (canvas == null) {
			RootPanel
					.get()
					.add(new Label(
							"Sorry, your browser doesn't support the HTML5 Canvas element"));
			return;
		}
		canvas.addKeyDownHandler(this);
		canvas.addClickHandler(this);
		canvas.setStyleName("mainCanvas"); // *** must match the div tag in
											// CanvasExample.html ***
		canvas.setWidth(canvasWidth + "px");
		canvas.setCoordinateSpaceWidth(canvasWidth);

		canvas.setHeight(canvasHeight + "px");
		canvas.setCoordinateSpaceHeight(canvasHeight);

		RootPanel.get(divTagId).add(canvas);
		canvas.setFocus(true);
		context = canvas.getContext2d();
		loadImages();
        currentPuzNum = 0;
        encStr = allEncStr[currentPuzNum];
        titleStr = allTitleStr[currentPuzNum];
        solnStr = allSolnStr[currentPuzNum];
		loadPuzz();
		update();
	}

	private boolean loadPuzz() {
		int i = 0;
		puzzleType = encStr.charAt(i++);
		char c = encStr.charAt(i++);
		char c1 = encStr.charAt(i++);
		int j = Character.digit(c, 16);
		int k = Character.digit(c1, 16);
		char c2 = encStr.charAt(i++);
		char c3 = encStr.charAt(i++);
		char c4 = encStr.charAt(i++);
		char c5 = encStr.charAt(i++);
		int l = Character.digit(c2, 16);
		int i1 = Character.digit(c3, 16);
		int j1 = Character.digit(c4, 16);
		int k1 = Character.digit(c5, 16);
		CratePuzzleState cratepuzzlestate = null;
		while (i < encStr.length() - 1) {
			char c6 = encStr.charAt(i++);
			char c7 = encStr.charAt(i++);
			char c8 = encStr.charAt(i++);
			int l1 = Character.digit(c6, 16);
			int i2 = Character.digit(c7, 16);
			int j2 = Character.digit(c8, 16);
			if (cratepuzzlestate == null)
				cratepuzzlestate = new CratePuzzleState(l, i1,
						new CratePuzzlePiece(l1, i2, j2));
			else
				cratepuzzlestate = new CratePuzzleState(cratepuzzlestate,
						new CratePuzzlePiece(l1, i2, j2));
		}
		Point point = new Point(j1, k1);
		start = cratepuzzlestate;
		end = point;
		location = new Point(l, i1);
		width = j;
		height = k;
		saveLocation[0] = new CratePuzzleState(start);
		savePieceMoved[0] = -1;
		moveI = 0;
		maxRedoI = 0;
		return true;
	}
	
    public void calcSqSz()
    {
        spacing = 35;
        exBoard = (canvasWidth - 364) / 2;
        eyBoard = (canvasHeight - 274) / 2;
        ex = exBoard + 76;
        ey = eyBoard + 29;
        hotSpot[0] = new Rectangle(0, 0, 19, 19);
        hotSpot[1] = new Rectangle(canvasWidth - 19, 0, 19, 19);
        hotSpot[2] = new Rectangle(canvasWidth - 110, canvasHeight - 19, 51, 19);
        hotSpot[3] = new Rectangle(canvasWidth - 51, canvasHeight - 19, 51, 19);
        hotSpot[4] = new Rectangle(0, canvasHeight - 19, 45, 19);
        hotSpot[5] = new Rectangle(canvasWidth - 200, canvasHeight - 19, 84, 19);
    }
    
	public void update() {
        boolean flag = false;
        if(checkForWin())
            flag = true;
        context.setFillStyle(CssColor.make("white"));
        context.fillRect(0, 0, canvasWidth, canvasHeight);
        if(currentPuzNum > 0)
            context.drawImage(imgLeftArrow, 2, 2);
        if(currentPuzNum < nPuzzles - 1)
            context.drawImage(imgRightArrow, canvasWidth - 17, 2);
        if(moveI > 0)
            context.drawImage(imgUndoButton, canvasWidth - 110, canvasHeight - 17);
        else
            context.drawImage(imgUndoButtonGreyed, canvasWidth - 110, canvasHeight - 17);
        if(moveI > 0)
        	context.drawImage(imgRestartButton, canvasWidth - 200, canvasHeight - 17);
        else
        	context.drawImage(imgRestartButtonGreyed, canvasWidth - 200, canvasHeight - 17);
        if(moveI < maxRedoI)
        	context.drawImage(imgRedoButton, canvasWidth - 51, canvasHeight - 17);
        else
        	context.drawImage(imgRedoButtonGreyed, canvasWidth - 51, canvasHeight - 17);
        context.drawImage(imgHelpButton, 0, canvasHeight - 17);
        CratePuzzleState cratepuzzlestate = saveLocation[moveI];
/*        
        FontMetrics fontmetrics = g2.getFontMetrics(labelFont);
        for(int l = 0; l < 4; l++)
            labelWidth[l] = fontmetrics.stringWidth(labels[l]);

        labelHeight = fontmetrics.getAscent();
*/
        calcSqSz();        
		context.drawImage(imgBoard, exBoard, eyBoard);
        for(int i1 = 0; i1 < cratepuzzlestate.nPieces; i1++)
        {
            CratePuzzlePiece cratepuzzlepiece = cratepuzzlestate.pieces[i1];
            int k1 = cratepuzzlepiece.x;
            int i2 = cratepuzzlepiece.y;
            int k2 = cratepuzzlepiece.thickness;
            int i3 = cratepuzzlepiece.width;
            int k3 = cratepuzzlepiece.height;
            int l3 = ex + k1 * spacing;
            int i4 = ey + i2 * spacing;
            if(i3 == 1 && k3 == 1)
            {
                if(k1 == end.x && i2 == end.y)
                    context.drawImage(imgUpright[1], l3, i4);
                else
                    context.drawImage(imgUpright[k2], l3, i4);
            } else
            if(i3 > k3)
            {
                for(int k4 = 0; k4 < i3; k4++)
                    if(cratepuzzlepiece.baseX == k1 + k4)
                        context.drawImage(imgBase[i3], l3 + k4 * spacing, i4);
                    else
                        context.drawImage(imgSide[i3], l3 + k4 * spacing, i4);

            } else
            {
                for(int l4 = 0; l4 < k3; l4++)
                    if(cratepuzzlepiece.baseY == i2 + l4)
                        context.drawImage(imgBase[k3], l3, i4 + l4 * spacing);
                    else
                        context.drawImage(imgSide[k3], l3, i4 + l4 * spacing);

            }
        }
        int j1 = cratepuzzlestate.moverX;
        int l1 = cratepuzzlestate.moverY;
        context.setFillStyle(moverColor);
        int j2 = ex + j1 * spacing;
        int l2 = ey + l1 * spacing;
        context.fillRect(j2 + 3, l2 + 3, spacing - 5, 3);
        context.fillRect(j2 + 3, l2 + 3, 3, spacing - 5);
        context.fillRect((j2 + spacing) - 5, l2 + 3, 3, spacing - 5);
        context.fillRect(j2 + 3, (l2 + spacing) - 5, spacing - 5, 3);
        context.setFillStyle(CssColor.make("black"));
        context.setFont(statsFont);
        int j3 = currentPuzNum + 1;
        String s = "" + titleStr;
        //FontMetrics fontmetrics1 = g2.getFontMetrics(statsFont);
        //int j4 = fontmetrics1.stringWidth(s);
        int j4 = (int) context.measureText(s).getWidth();
        //int i5 = fontmetrics1.getHeight();
        int i5 = 12;
        //g2.setColor(fontColor);
        context.fillText(s, (canvasWidth - j4) / 2, i5);
        if(flag)
        {
            String s1 = "Success!!";
            //FontMetrics fontmetrics2 = g2.getFontMetrics(actionFont);
            //int j5 = fontmetrics2.stringWidth(s1);
            context.setFont(actionFont);
            int j5 = (int) context.measureText(s1).getWidth();
            context.setFillStyle(CssColor.make("black"));
            context.fillText(s1, 2 + (canvasWidth - j5) / 2, ey + 102);
            context.setFillStyle(CssColor.make("red"));
            context.fillText(s1, 2 + (canvasWidth - j5) / 2, ey + 100);
        }
	}

    public boolean makeMove(int i, int j)
    {
        CratePuzzleState cratepuzzlestate = saveLocation[moveI];
        int k = cratepuzzlestate.getCurBlockI();
        int l = i;
        int i1 = j;
        int j1 = cratepuzzlestate.moverX;
        int k1 = cratepuzzlestate.moverY;
        int l1 = l - j1;
        int i2 = i1 - k1;
        if(l1 > 1 || l1 < -1 || i2 > 1 || i2 < -1)
            return false;
        if(l1 == 0 && i2 == 0)
            return false;
        if(l1 != 0 && i2 != 0)
            return false;
        if(!isClearSpot(i, j))
            return false;
        CratePuzzleState cratepuzzlestate1 = null;
        if(cratepuzzlestate.blockAt(l, i1))
        {
            cratepuzzlestate1 = new CratePuzzleState(cratepuzzlestate);
            cratepuzzlestate1.moverX = l;
            cratepuzzlestate1.moverY = i1;
        } else
        {
            CratePuzzlePiece cratepuzzlepiece = cratepuzzlestate.pieces[k];
            if(cratepuzzlepiece.upright && (j1 != end.x || k1 != end.y))
            {
                CratePuzzlePiece cratepuzzlepiece1 = cratepuzzlepiece.flip(l1, i2);
                if(pieceIsClearSpot(cratepuzzlepiece1) && !cratepuzzlestate.pieceOverlaps(cratepuzzlepiece1))
                {
                    cratepuzzlestate1 = new CratePuzzleState(cratepuzzlestate);
                    cratepuzzlestate1.moverX = l;
                    cratepuzzlestate1.moverY = i1;
                    cratepuzzlestate1.pieces[k] = cratepuzzlepiece1;
                }
            }
        }
        if(cratepuzzlestate1 != null)
        {
            moveI++;
            maxRedoI = moveI;
            saveLocation[moveI] = cratepuzzlestate1;
            location = new Point(cratepuzzlestate1.moverX, cratepuzzlestate1.moverY);
            update();
            return true;
        } else
        {
            return false;
        }
    }

    private boolean isClearSpot(int i, int j)
    {
        if(i < 0)
            return false;
        if(i >= width)
            return false;
        if(j < 0)
            return false;
        return j < height;
    }

    private boolean pieceIsClearSpot(CratePuzzlePiece cratepuzzlepiece)
    {
        for(int i = cratepuzzlepiece.x; i < cratepuzzlepiece.x + cratepuzzlepiece.width; i++)
        {
            for(int j = cratepuzzlepiece.y; j < cratepuzzlepiece.y + cratepuzzlepiece.height; j++)
                if(!isClearSpot(i, j))
                    return false;

        }

        return true;
    }

    public boolean checkForWin()
    {
        CratePuzzleState cratepuzzlestate = saveLocation[moveI];
        if(puzzleType == 'A')
        {
            for(int i = 0; i < cratepuzzlestate.nPieces; i++)
                if((cratepuzzlestate.pieces[i].x != end.x || cratepuzzlestate.pieces[i].y != end.y) && cratepuzzlestate.pieces[i].upright)
                    return false;

        }
        return location.equals(end);
    }

    private void goToPrevPuzzle()
    {
        if(currentPuzNum > 0)
        {
            currentPuzNum--;
            encStr = allEncStr[currentPuzNum];
            titleStr = allTitleStr[currentPuzNum];
            solnStr = allSolnStr[currentPuzNum];
            loadPuzz();
            update();
        }
    }

    private void goToNextPuzzle()
    {
        if(currentPuzNum < nPuzzles - 1)
        {
            currentPuzNum++;
            encStr = allEncStr[currentPuzNum];
            titleStr = allTitleStr[currentPuzNum];
            solnStr = allSolnStr[currentPuzNum];
            loadPuzz();
            update();
        }
    }

    private void undoMove()
    {
        moveI--;
        if(moveI < 0)
        {
            moveI = 0;
        } else
        {
            location = new Point(saveLocation[moveI].moverX, saveLocation[moveI].moverY);
            update();
        }
    }

    private void restartChallenge()
    {
        if(moveI > 0)
        {
            moveI = 0;
            location = new Point(saveLocation[moveI].moverX, saveLocation[moveI].moverY);
            update();
        }
    }

    private void redoMove()
    {
        moveI++;
        if(moveI > maxRedoI)
        {
            moveI = maxRedoI;
        } else
        {
            location = new Point(saveLocation[moveI].moverX, saveLocation[moveI].moverY);
            update();
        }
    }

	@Override
	public void onKeyDown(KeyDownEvent event) {
		int keyCode = event.getNativeKeyCode();
		event.preventDefault();
	    //GWT.log("onKeyDown:" + event.getNativeKeyCode());
        switch(keyCode)
        {
        case KeyCodes.KEY_N:
            goToNextPuzzle();
            break;
            
        case KeyCodes.KEY_P:
            goToPrevPuzzle();
            break;
            
        case KeyCodes.KEY_R:
            redoMove();
            break;
            
        case KeyCodes.KEY_S:
            restartChallenge();
            break;
            
        case KeyCodes.KEY_U:
            undoMove();
            break;
        }		
        if(checkForWin())
            return;
        switch(keyCode)
        {
        case KeyCodes.KEY_LEFT:
            makeMove(location.x - 1, location.y);
            break;

        case KeyCodes.KEY_UP:
            makeMove(location.x, location.y - 1);
            break;

        case KeyCodes.KEY_RIGHT:
            makeMove(location.x + 1, location.y);
            break;

        case KeyCodes.KEY_DOWN:
            makeMove(location.x, location.y + 1);
            break;
        }
	}

	@Override
	public void onClick(ClickEvent event) {
		event.preventDefault();
		clickX = event.getX();
		clickY = event.getY();
        int k1 = (clickX - ex) / spacing;
        int l1 = (clickY - ey) / spacing;
        if(hotSpot[0].contains(clickX, clickY))
        {
            goToPrevPuzzle();
            return;
        }
        if(hotSpot[1].contains(clickX, clickY))
        {
            goToNextPuzzle();
            return;
        }
        if(hotSpot[2].contains(clickX, clickY))
        {
            undoMove();
            return;
        }
        if(hotSpot[3].contains(clickX, clickY))
        {
            redoMove();
            return;
        }
        if(hotSpot[5].contains(clickX, clickY))
        {
            restartChallenge();
            return;
        }
        if(!checkForWin())
        {
            makeMove(k1, l1);
            update();
        }
	}
}
