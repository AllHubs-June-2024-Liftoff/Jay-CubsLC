package com.gamehouse.project.controllers;

import com.gamehouse.project.models.Game;
import com.gamehouse.project.models.GameReviews;
import com.gamehouse.project.models.User;
import com.gamehouse.project.models.data.*;
import com.gamehouse.project.models.dto.GameReviewsDTO;
import com.gamehouse.project.models.dto.GameUsernameDTO;
import com.gamehouse.project.services.APICallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
@CrossOrigin("http://localhost:5173/")
public class GameReviewController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameReviewsRepository gameReviewsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameCategoryRepository gameCategoryRepository;

    @Autowired
    private GamePlatformRepository gamePlatformRepository;


    // Save Game Reviews by Game igdb, Username
    @PostMapping("/save")
    public ResponseEntity<String> saveGameReview (@RequestBody GameReviewsDTO gameReviewsDTO) throws Exception {

        // Checks if Review already added by User - commented out the following to fix the 409.Conflict error
//        List<GameReviews> reviewsListByUser = gameReviewsRepository.findAllByUsername(gameReviewsDTO.getUsername());
//
//        for (GameReviews gameReview : reviewsListByUser) {
//
//            if (gameReview.getGameReview().equals(gameReviewsDTO.getGameReview())) {
//
//                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Game Review by User already exist.");
//            }
//        }


        // Create new Game Review object
        GameReviews newGameReview = new GameReviews();


        // Search gameRepository to find game based on igdbCode
        Optional<Game> getGame = gameRepository.findByIgdbCode(gameReviewsDTO.getIgdbCode());

        if (getGame.isPresent()) {
            newGameReview.setGame(getGame.get());
            newGameReview.setGameName(getGame.get().getName());

        } else {

            // Uses igdbCode to retrieve game from APICallService if NOT saved in gameRepository
            APICallService newApiCall = new APICallService();
            Game addNewGame = newApiCall.getGamebyIDGBCODE(gameReviewsDTO.getIgdbCode());
            gameCategoryRepository.saveAll(addNewGame.getGameCategories());
            gamePlatformRepository.saveAll(addNewGame.getGamePlatforms());
            gameRepository.save(addNewGame);

            // THEN, Search gameRepository to find game based on igdbCode
            Game gameAdded = gameRepository.findByIgdbCode(gameReviewsDTO.getIgdbCode()).get();
            newGameReview.setGame(gameAdded);
            newGameReview.setGameName(gameAdded.getName());
        }


        // Search UserRepository to find User based on username
//        User getUser = userRepository.findByUsername(gameReviewsDTO.getUsername());
//
//        newGameReview.setUser(getUser);
        newGameReview.setUsername(gameReviewsDTO.getUsername());


        // Setting igdbCode & game review to save in gameReviewRepository
        newGameReview.setIgdbCode(gameReviewsDTO.getIgdbCode());
        newGameReview.setGameReview(gameReviewsDTO.getGameReview());

        gameReviewsRepository.save(newGameReview);

        return ResponseEntity.status(HttpStatus.CREATED).body("Game Review saved");
    }



    // Get Reviews by Game igdbCode
    @PostMapping("/getReviewsIgdb")
    public List<GameReviews> getReviewsByIgdb(@RequestBody long igdbCode) {

        List<GameReviews> reviewsList = new ArrayList<>();

        List<GameReviews> reviewsListByIgdb = gameReviewsRepository.findAllByIgdbCode(igdbCode);


        for (int i = 0; i < reviewsListByIgdb.size(); i++) {
            GameReviews gameReview = new GameReviews();

//            gameReview.setGame(reviewsListByIgdb.get(i).getGame());
//            gameReview.setUser(reviewsListByIgdb.get(i).getUser());
            gameReview.setGameName(reviewsListByIgdb.get(i).getGameName());
            gameReview.setGameReview(reviewsListByIgdb.get(i).getGameReview());
            gameReview.setIgdbCode(reviewsListByIgdb.get(i).getIgdbCode());
            gameReview.setUsername(reviewsListByIgdb.get(i).getUsername());

            reviewsList.add(gameReview);
        }

        return reviewsList;
    }


    // Get Reviews by Username
    @GetMapping("/{username}")
    public List<GameReviews> getReviewsByUsername(@PathVariable String username) {

        List<GameReviews> reviewsListByUser = new ArrayList<>();

        List<GameReviews> reviewsListByUsername = gameReviewsRepository.findAllByUsername(username);

        for (int i = 0; i < reviewsListByUsername.size(); i++) {
            GameReviews gameReviewByUsername = new GameReviews();

//            gameReviewByUsername.setGame(reviewsListByIgdb.get(i).getGame());
//            gameReviewByUsername.setUser(reviewsListByIgdb.get(i).getUser());
            gameReviewByUsername.setGameName(reviewsListByUsername.get(i).getGameName());
            gameReviewByUsername.setGameReview(reviewsListByUsername.get(i).getGameReview());
            gameReviewByUsername.setIgdbCode(reviewsListByUsername.get(i).getIgdbCode());
            gameReviewByUsername.setUsername(reviewsListByUsername.get(i).getUsername());

            reviewsListByUser.add(gameReviewByUsername);
        }

        return reviewsListByUser;
    }


    // Delete reviews by Game Igdb, Username, Game Review
    @DeleteMapping("/delete")
    public ResponseEntity<String> removeReview(@RequestBody GameReviewsDTO gameReviewsDTO) {

        // Pulls up list of Reviews by Username
        List<GameReviews> reviewsListByUser = gameReviewsRepository.findAllByUsername(gameReviewsDTO.getUsername());

        for (GameReviews gameReview : reviewsListByUser) {

            if (gameReview.getGameReview().equals(gameReviewsDTO.getGameReview())) {

                gameReviewsRepository.deleteById(gameReview.getId());

            }
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Game Review by User is removed.");
    }


    // Delete reviews by Game Review Id
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> removeReview(@PathVariable int id) {

        // Checks to see if Review exist in gameReviewsRepository based on id
        if (gameReviewsRepository.existsById(id)) {

            gameReviewsRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Game Review is removed.");

        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Game Review does not exist.");
        }
    }

}
