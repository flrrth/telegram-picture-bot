package picturebot.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "daily_picture", schema = "picture_bot")
public class DailyPicture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "date_used", nullable = false, updatable = false)
    private LocalDate dateUsed;

    public DailyPicture() {
    }

    public DailyPicture(final String path) {
        this(path, LocalDate.now());
    }

    public DailyPicture(final String path, final LocalDate dateUsed) {
        this.path = path;
        this.dateUsed = dateUsed;
    }

    public Long getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public LocalDate getDateUsed() {
        return dateUsed;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        final DailyPicture that = (DailyPicture) other;
        return Objects.equals(getPath(), that.getPath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPath());
    }
}
