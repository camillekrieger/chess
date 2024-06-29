package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class RookMovesCalculator {
    private ChessPosition startpos;
    private ChessBoard current;
    private ChessGame.TeamColor color;
    public RookMovesCalculator(ChessPosition pos, ChessBoard currBoard, ChessGame.TeamColor color){
        this.startpos = pos;
        this.current = currBoard;
        this.color = color;
    }

    public Collection<ChessMove> possibleMoves(){
        Collection<ChessMove> posMoves = new ArrayList<>();
        int row = startpos.getRow();
        int col = startpos.getColumn();
        //going up
        for (int i = row + 1; i <= 8; i++){
            ChessPosition up = new ChessPosition(i, col);
            if (current.getPiece(up) == null){
                ChessMove newUp = new ChessMove(startpos, up, null);
                posMoves.add(newUp);
            }
            else if(current.getPiece(up).getTeamColor() != color){
                ChessMove newUp = new ChessMove(startpos, up, null);
                posMoves.add(newUp);
                break;
            }
            else{
                break;
            }
        }
        //going down
        for (int i = row - 1; i > 0; i--){
            ChessPosition down = new ChessPosition(i, col);
            if (current.getPiece(down) == null){
                ChessMove newDown = new ChessMove(startpos, down, null);
                posMoves.add(newDown);
            }
            else if(current.getPiece(down).getTeamColor() != color){
                ChessMove newDown = new ChessMove(startpos, down, null);
                posMoves.add(newDown);
                break;
            }
            else{
                break;
            }
        }
        //going left
        for (int i = col - 1; i > 0; i--){
            ChessPosition left = new ChessPosition(row, i);
            if (current.getPiece(left) == null){
                ChessMove newLeft = new ChessMove(startpos, left, null);
                posMoves.add(newLeft);
            }
            else if(current.getPiece(left).getTeamColor() != color){
                ChessMove newLeft = new ChessMove(startpos, left, null);
                posMoves.add(newLeft);
                break;
            }
            else{
                break;
            }
        }
        //going right
        for (int i = col + 1; i <= 8; i++){
            ChessPosition right = new ChessPosition(row, i);
            if (current.getPiece(right) == null){
                ChessMove newRight = new ChessMove(startpos, right, null);
                posMoves.add(newRight);
            }
            else if(current.getPiece(right).getTeamColor() != color){
                ChessMove newRight = new ChessMove(startpos, right, null);
                posMoves.add(newRight);
                break;
            }
            else{
                break;
            }
        }
        return posMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RookMovesCalculator that = (RookMovesCalculator) o;
        return Objects.equals(startpos, that.startpos) && Objects.equals(current, that.current) && color == that.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startpos, current, color);
    }
}
