package scissors.results;

import java.util.Objects;

public class Results{

    private Integer win;
    private Integer lose;
    private Integer draw;

    public Results() {
    }

    public Results(Integer win, Integer lose, Integer draw) {
        this.win = win;
        this.lose = lose;
        this.draw = draw;
    }

    public int getWin() {
        return win;
    }

    public Results newWin() {
        return new Results(this.win+1,this.lose,this.draw);
    }

    public int getLose() {
        return lose;
    }

    public Results newLose() {
        return new Results(this.win,this.lose+1,this.draw);
    }

    public int getDraw() {
        return draw;
    }

    public Results newDraw() {
        return new Results(this.win,this.lose,this.draw+1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Results results = (Results) o;
        return Objects.equals(win, results.win) &&
                Objects.equals(lose, results.lose) &&
                Objects.equals(draw, results.draw);
    }

    @Override
    public int hashCode() {
        return Objects.hash(win, lose, draw);
    }

    @Override
    public String toString() {
        return "|win " + win +
                "|lose " + lose +
                "|draw " + draw +
                "|";
    }
}
