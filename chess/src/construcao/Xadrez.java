package construcao;

import construcao.board.Board;
import gui.Table;

public class Xadrez {

    public static void main(String[] args) {

        Board.Builder builder = new Board.Builder();
        Board board = new Board(builder);

        System.out.println(board.createStandarBoard());

        Table table = new Table();
    }
}
