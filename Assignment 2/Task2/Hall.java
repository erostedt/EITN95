class Hall {
    int size = 20;
    Tile[][] tiles;

    public Hall(){
        Tile[][] tiles = new Tile[size][size];
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                tiles[i][j] = new Tile();
            }
        }
        this.tiles = tiles;
    }

    public void moveStudent(Student student, int[] newPos) {
        // Moves student, i.e updates state of the hall.
        this.tiles[student.x][student.y].students.remove(student);
        this.tiles[newPos[0]][newPos[1]].students.add(student);
    }

}