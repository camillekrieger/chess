package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class KnightMovesCalculator {
    private ChessPosition startpos;
    private ChessBoard current;
    private ChessGame.TeamColor color;
    public KnightMovesCalculator(ChessPosition pos, ChessBoard currBoard, ChessGame.TeamColor color){
        this.startpos = pos;
        this.current = currBoard;
        this.color = color;
    }

    public Collection<ChessMove> possibleMoves() {
        Collection<ChessMove> posMoves = new ArrayList<>();
        int row = startpos.getRow();
        int col = startpos.getColumn();
        //up left
        int upLRow = row + 2;
        int upLCol = col - 1;
        if (upLRow <= 8 && upLCol > 0){
            ChessPosition upLeft = new ChessPosition(upLRow, upLCol);
            if (current.getPiece(upLeft) == null || current.getPiece(upLeft).getTeamColor() != color){
                ChessMove newupLeft = new ChessMove(startpos, upLeft, null);
                posMoves.add(newupLeft);
            }
        }
        //up Right
        int upRRow = row + 2;
        int upRCol = col + 1;
        if (upRRow <= 8 && upRCol <= 8){
            ChessPosition upRight = new ChessPosition(upRRow, upRCol);
            if (current.getPiece(upRight) == null || current.getPiece(upRight).getTeamColor() != color){
                ChessMove newupRight = new ChessMove(startpos, upRight, null);
                posMoves.add(newupRight);
            }
        }
        //right up
        int rightURow = row + 1;
        int rightUCol = col + 2;
        if (rightURow <= 8 && rightUCol <= 8){
            ChessPosition rightUp = new ChessPosition(rightURow, rightUCol);
            if (current.getPiece(rightUp) == null || current.getPiece(rightUp).getTeamColor() != color){
                ChessMove newRightUp = new ChessMove(startpos, rightUp, null);
                posMoves.add(newRightUp);
            }
        }
        //right down
        int rightDRow = row - 1;
        int rightDCol = col + 2;
        if (rightDRow > 0 && rightDCol <= 8){
            ChessPosition rightDown = new ChessPosition(rightDRow, rightDCol);
            if (current.getPiece(rightDown) == null || current.getPiece(rightDown).getTeamColor() != color){
                ChessMove newRightDown = new ChessMove(startpos, rightDown, null);
                posMoves.add(newRightDown);
            }
        }
        //down right
        int downRRow = row - 2;
        int downRCol = col + 1;
        if (downRRow > 0 && downRCol <= 8){
            ChessPosition downRight = new ChessPosition(downRRow, downRCol);
            if (current.getPiece(downRight) == null || current.getPiece(downRight).getTeamColor() != color){
                ChessMove newDownRight = new ChessMove(startpos, downRight, null);
                posMoves.add(newDownRight);
            }
        }
        //down left
        int downLRow = row - 2;
        int downLCol = col - 1;
        if (downLRow > 0 && downLCol > 0){
            ChessPosition downLeft = new ChessPosition(downLRow, downLCol);
            if (current.getPiece(downLeft) == null || current.getPiece(downLeft).getTeamColor() != color){
                ChessMove newdownLeft = new ChessMove(startpos, downLeft, null);
                posMoves.add(newdownLeft);
            }
        }
        //right down
        int rightDownRow = row - 1;
        int rightDownCol = col - 2;
        if (rightDownRow > 0 && rightDownCol > 0){
            ChessPosition rightDown = new ChessPosition(rightDownRow, rightDownCol);
            if (current.getPiece(rightDown) == null || current.getPiece(rightDown).getTeamColor() != color){
                ChessMove newRightDown = new ChessMove(startpos, rightDown, null);
                posMoves.add(newRightDown);
            }
        }
        //right up
        int rightUpRow = row + 1;
        int rightUpCol = col - 2;
        if (rightUpRow <= 8 && rightUpCol > 0){
            ChessPosition rightUp = new ChessPosition(rightUpRow, rightUpCol);
            if (current.getPiece(rightUp) == null || current.getPiece(rightUp).getTeamColor() != color){
                ChessMove newRightUp = new ChessMove(startpos, rightUp, null);
                posMoves.add(newRightUp);
            }
        }
        return posMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KnightMovesCalculator that = (KnightMovesCalculator) o;
        return Objects.equals(startpos, that.startpos) && Objects.equals(current, that.current) && color == that.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startpos, current, color);
    }
}
