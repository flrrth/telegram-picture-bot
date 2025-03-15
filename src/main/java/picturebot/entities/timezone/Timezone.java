package picturebot.entities.timezone;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "timezone", schema = "picture_bot")
public class Timezone {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "city", nullable = false, length = 100)
    private String city;

    public Timezone() {
    }

    public Timezone(final Long id, final String city) {
        this.id = id;
        this.city = city;
    }

    public Long getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        final Timezone timezone = (Timezone) other;
        return Objects.equals(getId(), timezone.getId()) && Objects.equals(getCity(), timezone.getCity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCity());
    }
}
