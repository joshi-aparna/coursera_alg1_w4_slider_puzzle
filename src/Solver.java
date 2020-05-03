import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {
    private class BoardComparator implements Comparator<SearchNode> {

        @Override
        public int compare(SearchNode o1, SearchNode o2) {
            if (o1.moves + o1.board.manhattan() < o2.moves + o2.board.manhattan())
                return -1;
            if (o1.moves + o1.board.manhattan() > o2.moves + o2.board.manhattan())
                return 1;
            return 0;
        }
    }

    private class SearchNode {
        Board board;
        SearchNode previous;
        int moves;

        SearchNode(Board board) {
            this.board = board;
            this.previous = null;
            this.moves = 0;
        }

        SearchNode(Board board, SearchNode previous, int m) {
            this.board = board;
            this.previous = previous;
            this.moves = m;
        }
    }

    private MinPQ<SearchNode> minPQ = new MinPQ<>(new BoardComparator());
    private MinPQ<SearchNode> twinPQ = new MinPQ<>(new BoardComparator());
    private SearchNode goal = null;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        minPQ.insert(new SearchNode(initial));
        twinPQ.insert(new SearchNode(initial.twin()));
        solve();
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return goal != null;
    }

    // min number of moves to solve initial board
    public int moves() {
        if (isSolvable())
            return goal.moves;
        return -1;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (goal == null)
            return null;
        Stack<Board> solution = new Stack<>();
        SearchNode cur = goal;
        while (cur != null) {
            solution.push(cur.board);
            cur = cur.previous;
        }
        return solution;
    }

    private boolean alreadyProcessed(SearchNode s, Board n) {
        SearchNode cur = s;
        while (cur != null) {
            if (cur.board.equals(n)) {
                return true;
            }
            cur = cur.previous;
        }
        return false;
    }

    private void solve() {
        while (!minPQ.isEmpty() || !twinPQ.isEmpty()) {
            SearchNode cur = minPQ.delMin();
            SearchNode curTwin = twinPQ.delMin();
            if (cur.board.hamming() == 0) {
                this.goal = cur;
                break;
            }
            if (curTwin.board.hamming() == 0) {
                this.goal = null;
                break;
            }
            for (Board n : cur.board.neighbors()) {
                if (!alreadyProcessed(cur, n)) {
                    minPQ.insert(new SearchNode(n, cur, cur.moves + 1));
                }
            }
            for (Board n : curTwin.board.neighbors()) {
                if (!alreadyProcessed(curTwin, n)) {
                    twinPQ.insert(new SearchNode(n, curTwin, curTwin.moves + 1));
                }
            }
        }
    }


    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
