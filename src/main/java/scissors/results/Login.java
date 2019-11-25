package scissors.results;

import java.util.Objects;

public class Login {

    private String id;

    public Login(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Login)) return false;
        Login login = (Login) o;
        return getId().equals(login.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        while(id.length()<15){
            id +=" ";
        }
        return id;
    }
}
