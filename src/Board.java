import edu.princeton.cs.algs4.Queue;

public class Board {
    private int[][] tiles;
    private int manhattan = 0;
    private int hamming = 0;
    private int zeroPosX = -1;
    private int zeroPosy = -1;
    private int N;

    public Board(int[][] tiles) {
        if (tiles == null)
            throw new IllegalArgumentException();
        int manhattan = 0;
        int hamming = 0;
        N = tiles.length;
        this.tiles = new int[N][N];
        for (int i = 0; i < this.tiles.length; i++) {
            for (int j = 0; j < this.tiles[i].length; j++) {
                this.tiles[i][j] = tiles[i][j];
                if (this.tiles[i][j] == 0) {
                    zeroPosX = i;
                    zeroPosy = j;
                    continue;
                }
                if (this.tiles[i][j] != (i * N + j + 1)) {
                    hamming++;
                    int ex = (this.tiles[i][j] - 1) / N;
                    int ey = this.tiles[i][j] - 1 - (ex * N);

                    manhattan += Math.abs(i - ex) + Math.abs(j - ey);
                }

            }
        }
        this.hamming = hamming;
        this.manhattan = manhattan;
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder("" + this.tiles.length);
        for (int i = 0; i < this.tiles.length; i++) {
            sb.append("\n");
            for (int j = 0; j < this.tiles[i].length; j++) {
                sb.append(" ");
                sb.append(this.tiles[i][j]);
            }
        }
        sb.append("\n");
        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return N;
    }

    // number of tiles out of place
    public int hamming() {
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        Board that = (Board) y;
        if (this.N != that.N)
            return false;
        return this.toString().equalsIgnoreCase(that.toString());
    }

    private Board left() {
        if (zeroPosX - 1 >= 0) {
            int[][] left = new int[this.tiles.length][this.tiles[0].length];
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < this.tiles[i].length; j++) {
                    left[i][j] = this.tiles[i][j];
                }
            }
            int temp = left[zeroPosX][zeroPosy];
            left[zeroPosX][zeroPosy] = left[zeroPosX - 1][zeroPosy];
            left[zeroPosX - 1][zeroPosy] = temp;
            return new Board(left);
        }
        return null;
    }

    private Board right() {
        if (zeroPosX + 1 < N) {
            int[][] right = new int[this.tiles.length][this.tiles[0].length];
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < this.tiles[i].length; j++) {
                    right[i][j] = this.tiles[i][j];
                }
            }
            int temp = right[zeroPosX][zeroPosy];
            right[zeroPosX][zeroPosy] = right[zeroPosX + 1][zeroPosy];
            right[zeroPosX + 1][zeroPosy] = temp;
            return new Board(right);
        }
        return null;
    }

    private Board up() {
        if (zeroPosy - 1 >= 0) {
            int[][] up = new int[this.tiles.length][this.tiles[0].length];
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < this.tiles[i].length; j++) {
                    up[i][j] = this.tiles[i][j];
                }
            }
            int temp = up[zeroPosX][zeroPosy];
            up[zeroPosX][zeroPosy] = up[zeroPosX][zeroPosy - 1];
            up[zeroPosX][zeroPosy - 1] = temp;
            return new Board(up);
        }
        return null;
    }

    private Board down() {
        if (zeroPosy + 1 < N) {
            int[][] down = new int[this.tiles.length][this.tiles[0].length];
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < this.tiles[i].length; j++) {
                    down[i][j] = this.tiles[i][j];
                }
            }
            int temp = down[zeroPosX][zeroPosy];
            down[zeroPosX][zeroPosy] = down[zeroPosX][zeroPosy + 1];
            down[zeroPosX][zeroPosy + 1] = temp;
            return new Board(down);
        }
        return null;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> neighbor = new Queue<Board>();
        Board up = up();
        if (up != null)
            neighbor.enqueue(up);
        Board down = down();
        if (down != null)
            neighbor.enqueue(down);
        Board left = left();
        if (left != null)
            neighbor.enqueue(left);
        Board right = right();
        if (right != null)
            neighbor.enqueue(right);
        return neighbor;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] left = new int[this.tiles.length][this.tiles[0].length];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < this.tiles[i].length; j++) {
                left[i][j] = this.tiles[i][j];
            }
        }
        int r = 0;
        int a = 0;
        int b = 1;
        if (this.tiles[r][a] == 0 || this.tiles[r][b] == 0) {
            r++;
        }
        int temp = left[r][a];
        left[r][a] = left[r][b];
        left[r][b] = temp;
        return new Board(left);
    }

    public static void main(String[] args) {

    }
}
