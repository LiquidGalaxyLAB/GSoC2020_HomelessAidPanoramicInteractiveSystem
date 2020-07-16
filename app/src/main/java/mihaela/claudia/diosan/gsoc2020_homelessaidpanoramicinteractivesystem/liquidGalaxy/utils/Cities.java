package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.liquidGalaxy.utils;

public class Cities {
    private String city;
    private String country;
    private String image;

    public Cities(){}

    public Cities(String city, String country, String image) {
        this.city = city;
        this.country = country;
        this.image = image;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getImage() {
        return image;
    }
}
