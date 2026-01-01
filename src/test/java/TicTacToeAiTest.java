import com.iti.crg.client.domain.entities.*;
import com.iti.crg.client.domain.game.aistrategy.EasyTicTacToeAi;
import com.iti.crg.client.domain.game.aistrategy.HardTicTacToeAi;
import com.iti.crg.client.domain.game.aistrategy.MediumTicTacToeAi;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TicTacToeAiTest {

    @Test
    void testEasyAiChoosesEmptyCell() {
        Board board = new TicTacToeBoard();
        EasyTicTacToeAi ai = new EasyTicTacToeAi();
        Cell move = ai.AIMove(board);
        assertNotNull(move);
        assertTrue(board.getGrid()[move.getRow()][move.getCol()] == '_');
    }

    @Test
    void testHardAiBlocksOpponentWin() {
        Board board = new TicTacToeBoard();
        HardTicTacToeAi ai = new HardTicTacToeAi('X');

        board.getGrid()[0][0] = 'O';
        board.getGrid()[0][1] = 'O';
        board.getGrid()[1][1] = 'X';

        Cell move = ai.AIMove(board);

        assertEquals(0, move.getRow());
        assertEquals(2, move.getCol());
    }

    @Test
    void testHardAiTakesWinningMove() {
        Board board = new TicTacToeBoard();
        HardTicTacToeAi ai = new HardTicTacToeAi('X');

        board.getGrid()[2][0] = 'X';
        board.getGrid()[2][1] = 'X';
        board.getGrid()[1][1] = 'O';

        Cell move = ai.AIMove(board);

        assertEquals(2, move.getRow());
        assertEquals(2, move.getCol());
    }


        @Test
        void testAlternatesBetweenEasyAndHard() {
            Board board = new TicTacToeBoard();
            MediumTicTacToeAi ai = new MediumTicTacToeAi('X');
            assertFalse(ai.isBestMove());
            Cell firstMove = ai.AIMove(board);
            assertNotNull(firstMove);
            assertTrue(ai.isBestMove());
            Cell secondMove = ai.AIMove(board);
            assertNotNull(secondMove);
            assertFalse(ai.isBestMove());
        }
    }

