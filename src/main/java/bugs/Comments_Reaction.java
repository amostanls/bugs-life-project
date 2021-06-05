package bugs;

public class Comments_Reaction {
    private int hash;
    private String reaction;

    public Comments_Reaction(int hash, String reaction) {
        this.hash = hash;
        this.reaction = reaction;
    }

    public int getHash() {
        return hash;
    }

    public void setHash(int hash) {
        this.hash = hash;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }
}
