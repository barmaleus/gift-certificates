package by.rekuts.giftcertificates.repository.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "gift_tag")
public class Tag {
    @Id
    @SequenceGenerator(name = "certSequence", sequenceName = "gift_tag_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "certSequence")
    private int id;
    private String name;
    @ManyToMany(mappedBy = "tags", cascade = CascadeType.ALL)
    private List<Certificate> certificates;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Certificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<Certificate> certificates) {
        this.certificates = certificates;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
