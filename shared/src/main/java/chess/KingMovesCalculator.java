package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class KingMovesCalculator {
    private ChessPosition startpos;
    private ChessBoard current;
    private ChessGame.TeamColor color;
    public  KingMovesCalculator(ChessPosition pos, ChessBoard currBoard, ChessGame.TeamColor color){
        this.startpos = pos;
        this.current = currBoard;
        this.color = color;
    }

    public Collection<ChessMove> possibleMoves(){
        Collection<ChessMove> posMoves = new ArrayList<>();
        //return a possible collection of moves
        int row = startpos.getRow();
        int col = startpos.getColumn();
        //down
        int downRow = row - 1;
        if (downRow > 0){
            ChessPosition down = new ChessPosition(downRow, col);
            if (current.getPiece(down) == null || current.getPiece(down).getTeamColor() != color){
                ChessMove newDown = new ChessMove(startpos, down, null);
                posMoves.add(newDown);
            }
        }
        //up right
        int upRRow = row + 1;
        int upRCol = col + 1;
        if (upRRow <= 8 && upRCol <= 8){
            ChessPosition upright = new ChessPosition(upRRow, upRCol);
            if (current.getPiece(upright) == null || current.getPiece(upright).getTeamColor() != color){
                ChessMove newUpRight = new ChessMove(startpos, upright, null);
                posMoves.add(newUpRight);
            }
        }
        //right
        int rCol = col + 1;
        if (rCol <= 8) {
            ChessPosition right = new ChessPosition(row, rCol);
            if (current.getPiece(right) == null || current.getPiece(right).getTeamColor() != color) {
                ChessMove newRight = new ChessMove(startpos, right, null);
                posMoves.add(newRight);
            }
        }
        //down right
        int downRRow = row - 1;
        int downRCol = col + 1;
        if (downRRow > 0 && downRCol <= 8) {
            ChessPosition downright = new ChessPosition(downRRow, downRCol);
            if (current.getPiece(downright) == null || current.getPiece(downright).getTeamColor() != color) {
                ChessMove newDownRight = new ChessMove(startpos, downright, null);
                posMoves.add(newDownRight);
            }
        }
        //down left
        int downLRow = row - 1;
        int downLCol = col - 1;
        if (downLRow > 0 && downLCol > 0) {
            ChessPosition downleft = new ChessPosition(downLRow, downLCol);
            if (current.getPiece(downleft) == null || current.getPiece(downleft).getTeamColor() != color) {
                ChessMove newDownLeft = new ChessMove(startpos, downleft, null);
                posMoves.add(newDownLeft);
            }
        }
        //up
        int upRow = row + 1;
        if (upRow <= 8) {
            ChessPosition up = new ChessPosition(upRow, col);
            if (current.getPiece(up) == null || current.getPiece(up).getTeamColor() != color) {
                ChessMove newUp = new ChessMove(startpos, up, null);
                posMoves.add(newUp);
            }
        }
        //left
        int leftCol = col - 1;
        if (leftCol > 0) {
            ChessPosition left = new ChessPosition(row, leftCol);
            if (current.getPiece(left) == null || current.getPiece(left).getTeamColor() != color) {
                ChessMove newLeft = new ChessMove(startpos, left, null);
                posMoves.add(newLeft);
            }
        }
        //up left
        int upLRow = row + 1;
        int upLCol = col - 1;
        if (upLRow <= 8 && upLCol > 0) {
            ChessPosition upLeft = new ChessPosition(upLRow, upLCol);
            if (current.getPiece(upLeft) == null || current.getPiece(upLeft).getTeamColor() != color) {
                ChessMove newUpLeft = new ChessMove(startpos, upLeft, null);
                posMoves.add(newUpLeft);
            }
        }
        return posMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        KingMovesCalculator that = (KingMovesCalculator) o;
        return Objects.equals(startpos, that.startpos) && Objects.equals(current, that.current);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startpos, current);
    }

    @Override
    public String toString() {
        return "KingMovesCalculator{" +
                "startpos=" + startpos.toString() +
                ", current=" + current.toString() +
                '}';
    }
}
