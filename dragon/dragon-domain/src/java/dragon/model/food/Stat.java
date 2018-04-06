package dragon.model.food;

/**
 * Created by lin.cheng
 */
public class Stat {
    private String name;
    private String alias;
    private int liked = 0;
    private int disliked = 0;
    private int vetoed = 0;
    private int factor;
    private int score;
    private int visited = 0;

    public Stat(String name, int factor, int score) {
        this.name = name;
        this.factor = factor;
        this.score = score;
    }

    public String getAlias() {
        if(alias == null){
            return name;
        }
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLiked() {
        return liked;
    }

    public void setLiked(int liked) {
        this.liked = liked;
    }

    public int getDisliked() {
        return disliked;
    }

    public void setDisliked(int disliked) {
        this.disliked = disliked;
    }

    public int getVetoed() {
        return vetoed;
    }

    public void setVetoed(int vetoed) {
        this.vetoed = vetoed;
    }

    public int getFactor() {
        return factor;
    }

    public void setFactor(int factor) {
        if(factor < 0){
            factor = 0;
        }
        if(factor > 30){
            factor = 30;
        }
        this.factor = factor;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getVisited() {
        return visited;
    }

    public void setVisited(int visited) {
        this.visited = visited;
    }

    @Override
    public String toString() {
        return "Name: " + name +
                ", factor: " + factor +
                ", score: " + score +
                ", visited: " + visited +
                ", liked: " + liked +
                ", disliked: " + disliked +
                ", vetoed: " + vetoed;
    }

    public String toPrintString() {
        return String.format("%-45s%-10s%-10s%-10s%-10s%-10s%-10s", name.substring(0, name.length()<43?name.length():43),
                getFactor(), getScore(), getVisited(), getLiked(), getDisliked(), getVetoed());
    }

}
