package site.magazine.wisenews.Models;

import javax.persistence.*;

@Entity
public class Magazine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String contains;
    private String link;
    private String tags;
    private String filename;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    public Magazine() {}

    public Magazine(String name, String contains, String link) {
        this.name = name;
        this.contains = contains;
        this.link = link;
    }

    public Magazine(Long id, String name, String contains, String link) {
        this.id = id;
        this.name = name;
        this.contains = contains;
        this.link = link;
    }

    public Magazine(Long id, String name, String contains, String link, String tags) {
        this.id = id;
        this.name = name;
        this.contains = contains;
        this.link = link;
        this.tags = tags;
    }

    public Magazine(String name, String contains, String link, String tags) {
        this.name = name;
        this.contains = contains;
        this.link = link;
        this.tags = tags;
    }

    public Magazine(String name, String contains, String link, String tags, User author) {
        this.name = name;
        this.contains = contains;
        this.link = link;
        this.tags = tags;
        this.author = author;
    }

    public String getAuthor() {
        if (author != null)
            return author.getUsername();
        return "none";
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContains() {
        return contains;
    }

    public void setContains(String contains) {
        this.contains = contains;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
