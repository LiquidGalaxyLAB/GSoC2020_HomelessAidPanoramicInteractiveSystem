package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.utils;

public class LgUser {
    private String username;
    private int color;
    private String latitude;
    private String longitude;
    private String image;
    private String birthday;
    private String location;
    private String schedule;
    private String need;
    private String lifeHistory;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;


    public LgUser(){}

    public LgUser(String username, String latitude, String longitude, String location, String email, String phone, String firstName, String lastName) {
        this.username = username;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
        this.email = email;
        this.phone = phone;
        this.firstName = firstName;
        this.lastName = lastName;

    }

    public LgUser(String username, int color, String latitude, String longitude, String image, String  birthday, String location, String schedule, String need, String lifeHistory) {

        this.username = username;
        this.color = color;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
        this.birthday =birthday;
        this.location = location;
        this.schedule = schedule;
        this.need = need;
        this.lifeHistory = lifeHistory;
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

    public String getImage() {
        return image;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getLocation() {
        return location;
    }

    public String getSchedule() {
        return schedule;
    }

    public String getNeed() {
        return need;
    }

    public String getLifeHistory() {
        return lifeHistory;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
