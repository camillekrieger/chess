package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class KingMovesCalculator {
    private ChessPosition startpos;
    private ChessBoard current;
    public  KingMovesCalculator(ChessPosition pos, ChessBoard currBoard){
        this.startpos = pos;
        this.current = currBoard;
    }

    public Collection<ChessMove> possibleMoves(){
        Collection<ChessMove> posMoves = new ArrayList<>();
        //return a possible collection of moves
        int row = startpos.getRow();
        int col = startpos.getColumn();
        ChessPosition down = new ChessPosition(row - 1, col);
        if (current.getPiece(down) == null){
            ChessMove newDown = new ChessMove(startpos, down, null);
            posMoves.add(newDown);
        }
        ChessPosition upright = new ChessPosition(row + 1, col + 1);
        if (current.getPiece(upright) == null){
            ChessMove newUpRight = new ChessMove(startpos, upright, null);
            posMoves.add(newUpRight);
        }
        ChessPosition Right = new ChessPosition(row, col + 1);
        if (current.getPiece(Right) == null){
            ChessMove newRight = new ChessMove(startpos, Right, null);
            posMoves.add(newRight);
        }
        ChessPosition downright = new ChessPosition(row - 1, col + 1);
        if (current.getPiece(downright) == null){
            ChessMove newDownRight = new ChessMove(startpos, downright, null);
            posMoves.add(newDownRight);
        }
        ChessPosition downleft = new ChessPosition(row - 1, col - 1);
        if (current.getPiece(downleft) == null){
            ChessMove newDownLeft = new ChessMove(startpos, downleft, null);
            posMoves.add(newDownLeft);
        }
        ChessPosition up = new ChessPosition(row + 1, col);
        if (current.getPiece(up) == null) {
            ChessMove newUp = new ChessMove(startpos, up, null);
            posMoves.add(newUp);
        }
        ChessPosition left = new ChessPosition(row, col - 1);
        if (current.getPiece(left) == null){
            ChessMove newLeft = new ChessMove(startpos, left, null);
            posMoves.add(newLeft);
        }
        ChessPosition upLeft = new ChessPosition(row + 1, col - 1);
        if (current.getPiece(upLeft) == null) {
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
