package chesscoach.util;

import java.util.Objects;

public class Space {
    public int rank;  // 0-7
    public int file;  // 0=a, 7=h

    public Space(int rank, int file){
        this.rank = rank;
        this.file = file;
    }

    public Space pan(int dRank, int dFile){
        return new Space(rank + dRank, file + dFile);
    }

    public Space panRank(int dRank){
        return pan(dRank, 0);
    }

    public Space panFile(int dFile){
        return pan(0, dFile);
    }

    public int getFile() {
        return file;
    }

    public int getRank() {
        return rank;
    }

    public String toString(){
        return Util.formatCoord(rank, file);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Space space = (Space) o;
        return rank == space.rank && file == space.file;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, file);
    }
}
