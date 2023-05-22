package com.tfg.AchieveIt.domain;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "videogame_table")
public class Videogame {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_Videogame")
    @Column(name = "id")
    Long id;

    @Column(name = "name")
    String name;

    @ManyToMany(targetEntity = Genre.class)
    @JoinTable(
            name = "videogame_genre",
            joinColumns = @JoinColumn(name = "videogame_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    Set<Genre> genres;
    @ManyToMany(targetEntity = Developer.class)
    @JoinTable(
            name = "videogame_developer",
            joinColumns = @JoinColumn(name = "videogame_id"),
            inverseJoinColumns = @JoinColumn(name = "developer_id")
    )
    Set<Developer> developers;

    @ManyToMany(targetEntity = Publisher.class)
    @JoinTable(
            name = "videogame_publisher",
            joinColumns = @JoinColumn(name = "videogame_id"),
            inverseJoinColumns = @JoinColumn(name = "publisher_id")
    )
    Set<Publisher> publishers;
    @ManyToMany(targetEntity = Platform.class)
    @JoinTable(
            name = "videogame_platform",
            joinColumns = @JoinColumn(name = "videogame_id"),
            inverseJoinColumns = @JoinColumn(name = "platform_id")
    )
    Set<Platform> platforms;

    public Videogame(String name){
        this.name = name;
    }

    public Videogame() {

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

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public Set<Developer> getDevelopers() {
        return developers;
    }

    public void setDevelopers(Set<Developer> developers) {
        this.developers = developers;
    }

    public Set<Publisher> getPublishers() {
        return publishers;
    }

    public void setPublishers(Set<Publisher> publishers) {
        this.publishers = publishers;
    }

    public Set<Platform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(Set<Platform> platforms) {
        this.platforms = platforms;
    }
}