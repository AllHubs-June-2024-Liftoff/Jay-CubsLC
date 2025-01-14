package com.gamehouse.project.models;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class GameJson {

     private String name;
     private long id;
     private List<Age_Ratings> age_ratings;
     private int[] genres; // we call this GameCategory
     private Cover cover;
     private int[] platforms;
     private String summary;

     public GameJson(){}

     public GameJson(String name, long id, List<Age_Ratings> age_ratings, int[] genres, Cover cover, int[] platforms, String summary) {
          this.name = name;
          this.id = id;
          this.age_ratings = age_ratings;
          this.genres = genres;
          this.cover = cover;
          this.platforms = platforms;
          this.summary = summary;
     }


     public String getName() {
          return name;
     }

     public void setName(String name) {
          this.name = name;
     }

     public long getId() {
          return id;
     }

     public void setId(long id) {
          this.id = id;
     }

     public List<Age_Ratings> getAge_ratings() {
          return age_ratings;
     }

     public void setAge_ratings(List<Age_Ratings> age_ratings) {
          this.age_ratings = age_ratings;
     }

     public int[] getGenres() {
          return genres;
     }

     public void setGenres(int[] genres) {
          this.genres = genres;
     }

     public int[] getPlatforms() {
          return platforms;
     }

     public void setPlatforms(int[] platforms) {
          this.platforms = platforms;
     }

     public Cover getCover() {
          return cover;
     }

     public void setCover(Cover cover) {
          this.cover = cover;
     }

     public String getSummary() {
          return summary;
     }

     public void setSummary(String summary) {
          this.summary = summary;
     }




     @Override
     public String toString() {
          return "GameJson {" + "\n" +
                  "name='" + name + '\'' + ", \n" +
                  "id=" + id + ", \n" +
                  "age_ratings=" + age_ratings + ", \n" +
                  "category=" + genres + ", \n" +
                  "cover=" + cover + ", \n" +
                  "platforms=" + Arrays.toString(platforms) + ", \n" +
                  "summary='" + summary + '\'' + "\n" +
                  '}' + "\n";
     }

     @Override
     public boolean equals(Object o) {
          if (this == o) return true;
          if (o == null || getClass() != o.getClass()) return false;
          GameJson gameJson = (GameJson) o;
          return id == gameJson.id;
     }

     @Override
     public int hashCode() {
          return Objects.hashCode(id);
     }
}
