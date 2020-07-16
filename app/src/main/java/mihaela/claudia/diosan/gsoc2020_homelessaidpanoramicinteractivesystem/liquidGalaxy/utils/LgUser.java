package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.utils;

public class LgUser {
    private String username;
    private int color;

    public LgUser(){}

    public LgUser(String username, int color) {

        this.username = username;
        this.color = color;
    }

    public String getUsername() {
        return username;
    }

    public int getColor() {
        return color;
    }
}
