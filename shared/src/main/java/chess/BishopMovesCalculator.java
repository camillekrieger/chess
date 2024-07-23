package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class BishopMovesCalculator {
    private ChessPosition startpos;
    private ChessBoard current;
    private ChessGame.TeamColor color;
    public BishopMovesCalculator(ChessPosition pos, ChessBoard currBoard, ChessGame.TeamColor color){
        this.startpos = pos;
        this.current = currBoard;
        this.color = color;
    }

    public Collection<ChessMove> possibleMoves() {
        Collection<ChessMove> posMoves = new ArrayList<>();
        int row = startpos.getRow();
        int col = startpos.getColumn();
        //up left
        int upLRow = row + 1;
        int upLCol = col - 1;
        while (upLRow <= 8 && upLCol > 0){
            ChessPosition upLeft = new ChessPosition(upLRow, upLCol);
            if (current.getPiece(upLeft) == null){
                ChessMove newUpLeft = new ChessMove(startpos, upLeft, null);
                posMoves.add(newUpLeft);
                upLRow++;
                upLCol--;
            }
            else if(current.getPiece(upLeft).getTeamColor() != color){
                ChessMove newUpLeft = new ChessMove(startpos, upLeft, null);
                posMoves.add(newUpLeft);
                break;
            }
            else{
                break;
            }
        }
        //up right
        int upRRow = row + 1;
        int upRCol = col + 1;
        while (upRRow <= 8 && upRCol <= 8){
            ChessPosition upRight = new ChessPosition(upRRow, upRCol);
            if (current.getPiece(upRight) == null){
                ChessMove newUpRight = new ChessMove(startpos, upRight, null);
                posMoves.add(newUpRight);
                upRRow++;
                upRCol++;
            }
            else if(current.getPiece(upRight).getTeamColor() != color){
                ChessMove newUpRight = new ChessMove(startpos, upRight, null);
                posMoves.add(newUpRight);
                break;
            }
            else{
                break;
            }
        }
        //down left
        int downLRow = row - 1;
        int downLCol = col - 1;
        while (downLRow > 0 && downLCol > 0){
            ChessPosition downLeft = new ChessPosition(downLRow, downLCol);
            if (current.getPiece(downLeft) == null){
                ChessMove newDownLeft = new ChessMove(startpos, downLeft, null);
                posMoves.add(newDownLeft);
                downLRow--;
                downLCol--;
            }
            else if(current.getPiece(downLeft).getTeamColor() != color){
                ChessMove newDownLeft = new ChessMove(startpos, downLeft, null);
                posMoves.add(newDownLeft);
                break;
            }
            else{
                break;
            }
        }
        //down right
        int downRRow = row - 1;
        int downRCol = col + 1;
        while (downRRow > 0 && downRCol <= 8){
            ChessPosition downRight = new ChessPosition(downRRow, downRCol);
            if (current.getPiece(downRight) == null){
                ChessMove newDownRight = new ChessMove(startpos, downRight, null);
                posMoves.add(newDownRight);
                downRRow--;
                downRCol++;
            }
            else if(current.getPiece(downRight).getTeamColor() != color){
                ChessMove newDownRight = new ChessMove(startpos, downRight, null);
                posMoves.add(newDownRight);
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
        BishopMovesCalculator that = (BishopMovesCalculator) o;
        return Objects.equals(startpos, that.startpos) && Objects.equals(current, that.current) && color == that.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startpos, current, color);
    }
}
