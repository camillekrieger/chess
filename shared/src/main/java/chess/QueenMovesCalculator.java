package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class QueenMovesCalculator {
    private ChessPosition startpos;
    private ChessBoard current;
    private ChessGame.TeamColor color;
    public QueenMovesCalculator(ChessPosition pos, ChessBoard currBoard, ChessGame.TeamColor color){
        this.startpos = pos;
        this.current = currBoard;
        this.color = color;
    }

    public Collection<ChessMove> possibleMoves(){
        BishopMovesCalculator bmc = new BishopMovesCalculator(startpos, current, color);
        Collection<ChessMove> diagonal = bmc.possibleMoves();
        RookMovesCalculator rmc = new RookMovesCalculator(startpos, current, color);
        Collection<ChessMove> rightToLeft = rmc.possibleMoves();
        diagonal.addAll(rightToLeft);
        return diagonal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        QueenMovesCalculator that = (QueenMovesCalculator) o;
        return Objects.equals(startpos, that.startpos) && Objects.equals(current, that.current) && color == that.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startpos, current, color);
    }
}
