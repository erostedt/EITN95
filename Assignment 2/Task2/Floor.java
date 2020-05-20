import java.util.*;

class Floor {
    int size;
    Tile[][] tiles;

    public Floor(int size){
        Tile[][] t = new Tile[size][size];
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                t[i][j] = new Tile();
            }
        }
        this.tiles = t;
        this.size = size;
    }

    public void updateFloor(Student s, List<Integer> newPos) {
        this.tiles[s.pos.get(0)][s.pos.get(1)].studentsOnTile -= 1;
        this.tiles[newPos.get(0)][newPos.get(1)].studentsOnTile += 1;
        this.tiles[s.pos.get(0)][s.pos.get(1)].students.remove(s);
        this.tiles[newPos.get(0)][newPos.get(1)].students.add(s);

    }

}