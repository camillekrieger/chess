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
        ChessPosition down = new ChessPosition(row - 1, col);
        if (current.getPiece(down) == null || current.getPiece(down).getTeamColor() != color){
            ChessMove newDown = new ChessMove(startpos, down, null);
            posMoves.add(newDown);
        }
        ChessPosition upright = new ChessPosition(row + 1, col + 1);
        if (current.getPiece(upright) == null || current.getPiece(upright).getTeamColor() != color){
            ChessMove newUpRight = new ChessMove(startpos, upright, null);
            posMoves.add(newUpRight);
        }
        ChessPosition Right = new ChessPosition(row, col + 1);
        if (current.getPiece(Right) == null || current.getPiece(Right).getTeamColor() != color){
            ChessMove newRight = new ChessMove(startpos, Right, null);
            posMoves.add(newRight);
        }
        ChessPosition downright = new ChessPosition(row - 1, col + 1);
        if (current.getPiece(downright) == null || current.getPiece(downright).getTeamColor() != color){
            ChessMove newDownRight = new ChessMove(startpos, downright, null);
            posMoves.add(newDownRight);
        }
        ChessPosition downleft = new ChessPosition(row - 1, col - 1);
        if (current.getPiece(downleft) == null || current.getPiece(downleft).getTeamColor() != color){
            ChessMove newDownLeft = new ChessMove(startpos, downleft, null);
            posMoves.add(newDownLeft);
        }
        ChessPosition up = new ChessPosition(row + 1, col);
        if (current.getPiece(up) == null || current.getPiece(up).getTeamColor() != color) {
            ChessMove newUp = new ChessMove(startpos, up, null);
            posMoves.add(newUp);
        }
        ChessPosition left = new ChessPosition(row, col - 1);
        if (current.getPiece(left) == null || current.getPiece(left).getTeamColor() != color){
            ChessMove newLeft = new ChessMove(startpos, left, null);
            posMoves.add(newLeft);
        }
        ChessPosition upLeft = new ChessPosition(row + 1, col - 1);
        if (current.getPiece(upLeft) == null || current.getPiece(upLeft).getTeamColor() != color) {
            ChessMove newUpLeft = new ChessMove(startpos, upLeft, null);
            posMoves.add(newUpLeft);
        }
        return posMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
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
