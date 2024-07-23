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
        QueenMovesCalculator that = (QueenMovesCalculator) o;
        return Objects.equals(startpos, that.startpos) && Objects.equals(current, that.current) && color == that.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startpos, current, color);
    }
}
