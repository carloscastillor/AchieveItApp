package com.tfg.AchieveIt.webRest;

import com.api.igdb.exceptions.RequestException;
import com.tfg.AchieveIt.domain.*;
import com.tfg.AchieveIt.repository.*;
import com.tfg.AchieveIt.services.AchievementStatsApiClient;
import com.tfg.AchieveIt.services.IGDBApiClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import proto.Game;

import java.util.*;

@RestController
@RequestMapping("/api")
public class DatabaseController {

    private final VideogameRepository videogameRepository;
    private final GenreRepository genreRepository;
    private final PlatformRepository platformRepository;
    private final DeveloperRepository developerRepository;
    private final PublisherRepository publisherRepository;
    private final AchievementRepository achievementRepository;
    private IGDBApiClient igdbApiClient = new IGDBApiClient();
    private AchievementStatsApiClient achievementStatsApiClient = new AchievementStatsApiClient();

    public DatabaseController(VideogameRepository videogameRepository, GenreRepository genreRepository, PlatformRepository platformRepository, DeveloperRepository developerRepository, PublisherRepository publisherRepository, AchievementRepository achievementRepository) {
        this.videogameRepository = videogameRepository;
        this.genreRepository = genreRepository;
        this.platformRepository = platformRepository;
        this.developerRepository = developerRepository;
        this.publisherRepository = publisherRepository;
        this.achievementRepository = achievementRepository;
    }

    @GetMapping("/dbFill")
    public void FillDatabase() {
        try {
            /*FillGenres();
            FillPlatforms();
            FillDevelopersPublishers();
            FillVideogames();*/
            FillAchievements();
        } catch (Exception e) {}
    }

    private void FillGenres() throws RequestException {

        int page = 0;
        List<proto.Genre> genres;
        do {
            genres = igdbApiClient.searchGenres(page);
            for (proto.Genre genre : genres) {
                Genre genreBD = new Genre(genre.getName());
                genreRepository.save(genreBD);
            }
            page++;
        }while(!genres.isEmpty());
    }

    private void FillPlatforms()throws RequestException {

        int page = 0;
        List<proto.Platform> platforms;
        do {
            platforms = igdbApiClient.searchPlatforms(page);
            for (proto.Platform platform : platforms) {
                Platform platformDB = new Platform(platform.getName());
                platformRepository.save(platformDB);
            }
            page++;
        }while(!platforms.isEmpty());
    }

    private void FillDevelopersPublishers() throws RequestException{
        int page = 0;
        List<proto.Company> Companies;
        do {
            Companies = igdbApiClient.searchCompanies(page);
            for (proto.Company company : Companies) {
                System.out.println("name: " + company.getName() + " - Developed: " + company.getDevelopedCount() + " - isPublisher: " + company.getPublishedCount());
                if(company.getDevelopedCount()>0){
                    Developer developerDB = new Developer(company.getName());
                    if (developerRepository.findDeveloperByName(developerDB.getName()) == null)developerRepository.save(developerDB);
                }

                if(company.getPublishedCount()>0){
                    Publisher publisherDB = new Publisher(company.getName());
                    if (publisherRepository.findPublisherByName(publisherDB.getName()) == null)publisherRepository.save(publisherDB);
                }
            }
            page++;
        }while(!Companies.isEmpty());
    }

    private void FillVideogames() throws RequestException {
         int page = 0;
         List<Game> games;
         do {
             games = igdbApiClient.searchGames(page);
             for (Game game : games) {
                 System.out.println("- name of game: " + game.getName());
                 List<proto.Genre> genres = game.getGenresList();
                 List<proto.InvolvedCompany> Companies = game.getInvolvedCompaniesList();
                 List<proto.Platform> platforms = game.getPlatformsList();

                 Videogame videogame = new Videogame(game.getName());
                 Set<Genre> genresDB = new HashSet<>();

                 for (proto.Genre genre: genres) {

                     Genre genreDB = genreRepository.findGenreByName(genre.getName());
                     genresDB.add(genreDB);
                 }
                 Set<Platform> platformsDB = new HashSet<>();

                 for(proto.Platform platform: platforms){
                     Platform platformDB = platformRepository.findPlatformByName(platform.getName());
                     platformsDB.add(platformDB);
                 }

                 Set<Publisher> publishersDB = new HashSet<>();
                 Set<Developer> developersDB = new HashSet<>();
                 for(proto.InvolvedCompany involvedCompany : Companies){

                     if(involvedCompany.getPublisher()){
                         Publisher publisherDB = publisherRepository.findPublisherByName(involvedCompany.getCompany().getName());
                         publishersDB.add(publisherDB);
                     }

                     if(involvedCompany.getDeveloper()){
                         Developer developerDB = developerRepository.findDeveloperByName(involvedCompany.getCompany().getName());
                         developersDB.add(developerDB);
                     }
                 }

                 videogame.setDevelopers(developersDB);
                 videogame.setPublishers(publishersDB);
                 videogame.setPlatforms(platformsDB);
                 videogame.setGenres(genresDB);

                 videogameRepository.save(videogame);
             }
             page++;
         }while(!games.isEmpty());
    }

    private void FillAchievements(){
        String achievement = "";

        achievement = achievementStatsApiClient.searchAchievements("1596730");

        System.out.println(achievement);
    }
}