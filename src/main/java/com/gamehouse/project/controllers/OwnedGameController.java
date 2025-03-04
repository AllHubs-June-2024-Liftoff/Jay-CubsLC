package com.gamehouse.project.controllers;

import com.gamehouse.project.models.Game;
import com.gamehouse.project.models.OwnedGame;
import com.gamehouse.project.models.User;
import com.gamehouse.project.models.WishlistGame;
import com.gamehouse.project.models.data.*;
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
@RequestMapping("/owned")
@CrossOrigin("http://localhost:5173/")
public class OwnedGameController {

    @Autowired
    private OwnedGameRepository ownedGameRepository;

    @Autowired
    private WishlistGameRepository wishlistGameRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameCategoryRepository gameCategoryRepository;

    @Autowired
    private GamePlatformRepository gamePlatformRepository;



    // Add game to ownedGameRepository using long igdbCode & String username
    @PostMapping("/addGame")
    public ResponseEntity<String> addGameToOwned (@RequestBody GameUsernameDTO gameUsernameDTO) throws Exception {

        // Checks if Game already added by user
        List<OwnedGame> OwnedListByUser = ownedGameRepository.findAllByUsername(gameUsernameDTO.getUsername());

        for (OwnedGame game : OwnedListByUser) {
            if (game.getIgdbCode() == gameUsernameDTO.getIgdbCode()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Game already added to Owned List.");
            }
        }


        // Checks if Game already added to wishlistGameRepository, if so, will remove
        List<WishlistGame> WishlistByUser = wishlistGameRepository.findAllByUsername(gameUsernameDTO.getUsername());

        for (WishlistGame game : WishlistByUser) {
            if (game.getIgdbCode() == gameUsernameDTO.getIgdbCode()) {
                wishlistGameRepository.deleteById(game.getId());
            }
        }


        OwnedGame gameOwnedlist = new OwnedGame();

        // Search gameRepository for game -- if NOT will save game to gameRepository
        Optional<Game> getGame = gameRepository.findByIgdbCode(gameUsernameDTO.getIgdbCode());

        if (getGame.isPresent()) {
            gameOwnedlist.setGame(getGame.get());
            gameOwnedlist.setGameName(getGame.get().getName());
            gameOwnedlist.setIgdbCode(getGame.get().getIgdbCode());
            gameOwnedlist.setBoxArtUrl(getGame.get().getBoxArtURL());

        } else {

            // Uses igdbCode to retrieve game from APICallService & save to gameRepository
            APICallService newApiCall = new APICallService();
            Game addNewGame = newApiCall.getGamebyIDGBCODE(gameUsernameDTO.getIgdbCode());

            gameCategoryRepository.saveAll(addNewGame.getGameCategories());
            gamePlatformRepository.saveAll(addNewGame.getGamePlatforms());

            gameRepository.save(addNewGame);


            // THEN, Search gameRepository to find game based on igdbCode to set game as OwnedGame item
            Game ownedListGame = gameRepository.findByIgdbCode(gameUsernameDTO.getIgdbCode()).get();
            gameOwnedlist.setGame(ownedListGame);
            gameOwnedlist.setGameName(ownedListGame.getName());
            gameOwnedlist.setIgdbCode(ownedListGame.getIgdbCode());
            gameOwnedlist.setBoxArtUrl(ownedListGame.getBoxArtURL());
        }

        // Search UserRepository to find User based on username
//        User getUser = userRepository.findByUsername(gameUsernameDTO.getUsername());
//
//        gameOwnedlist.setUser(getUser);
        gameOwnedlist.setUsername(gameUsernameDTO.getUsername());

        // Saves Owned game to repo
        ownedGameRepository.save(gameOwnedlist);

//        wishlistGameRepository.saveAll(gameWishlist);
        return ResponseEntity.status(HttpStatus.CREATED).body("Game added to Owned List.");
    }



    // Get Owned Games by Username
    @GetMapping("/{username}")
    public List<OwnedGame> getOwnedGameByUsername (@PathVariable String username) {

        List<OwnedGame> ownedListByUser = new ArrayList<>();

        List<OwnedGame> ownedListByUsername = ownedGameRepository.findAllByUsername(username);

        for (int i = 0; i < ownedListByUsername.size(); i++) {
            OwnedGame ownedGame = new OwnedGame();

//            wishlistGame.setGame(wishlistByUsername.get(i).getGame());
//            wishlistGame.setUser(wishlistByUsername.get(i).getUser());
            ownedGame.setUsername(ownedListByUsername.get(i).getUsername());
            ownedGame.setGameName(ownedListByUsername.get(i).getGameName());
            ownedGame.setIgdbCode(ownedListByUsername.get(i).getIgdbCode());
            ownedGame.setBoxArtUrl(ownedListByUsername.get(i).getBoxArtUrl());

            ownedListByUser.add(ownedGame);
        }

        return ownedListByUser;
    }



    // Removes Game from ownedGameRepository by Igdb, Username
    @DeleteMapping("/delete")
    public ResponseEntity<String> removeGame(@RequestBody GameUsernameDTO gameUsernameDTO) {

        // Pulls up list of Owned Games by Username
        List<OwnedGame> OwnedListByUser = ownedGameRepository.findAllByUsername(gameUsernameDTO.getUsername());

        for (OwnedGame game : OwnedListByUser) {
            if (game.getIgdbCode() == gameUsernameDTO.getIgdbCode()) {
                ownedGameRepository.deleteById(game.getId());
            }
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Game is removed from Owned List.");
    }

}
