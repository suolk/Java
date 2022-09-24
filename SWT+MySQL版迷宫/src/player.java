class player {
    int cnt, best_score = 100000000;
    String Name;
    boolean cheat;

    public void setName(String name) {
        Name = name;
    }

    public void StepCount() {
        ++cnt;
    }

    public void setBest_score() {
        if (best_score > cnt)
            best_score = cnt;
    }
}
