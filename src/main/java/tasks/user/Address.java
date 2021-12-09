package tasks.user;

import java.util.Objects;

public class Address {
    private String street;
    private String suit;
    private String city;
    private String zipcode;
    private Geo geo;

    public Address(String street, String suit, String city, String zipcode, Geo geo) {
        this.street = street;
        this.suit = suit;
        this.city = city;
        this.zipcode = zipcode;
        this.geo = geo;
    }

    public Address(Address address){
        this.street = address.street;
        this.suit = address.suit;
        this.city = address.city;
        this.zipcode = address.zipcode;
        this.geo = new Geo(address.geo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(street, address.street) && Objects.equals(suit, address.suit) && Objects.equals(city, address.city) && Objects.equals(zipcode, address.zipcode) && Objects.equals(geo, address.geo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, suit, city, zipcode, geo);
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", suit='" + suit + '\'' +
                ", city='" + city + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", geo=" + geo +
                '}';
    }
}
