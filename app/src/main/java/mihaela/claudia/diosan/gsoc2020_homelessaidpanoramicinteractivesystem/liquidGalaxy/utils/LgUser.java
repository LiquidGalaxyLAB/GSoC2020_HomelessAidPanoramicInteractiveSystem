package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.utils;

public class LgUser {
    private String username;
    private int color;
    private String latitude;
    private String longitude;

    public LgUser(){}

    public LgUser(String username, int color, String latitude, String longitude) {

        this.username = username;
        this.color = color;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getUsername() {
        return username;
    }

    public int getColor() {
        return color;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
