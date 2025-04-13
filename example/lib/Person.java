package org.example.lib;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Person {
   // Fields to store personal details and information
   private String Name;
   private String Age;
   private String Password;
   private String Weight;
   private String Height;
   private String bloodPressure;
   private String Gender;
   private String Activity;
   private LocalDate lastUpdated = LocalDate.now();
   private List<String> history;

   public Person() {
      this.history = new ArrayList<>();
   }

   public List<String> getHistory() { return history; }
   public void setHistory(List<String> history) { this.history = history; }
   public void addHistory(String record) { history.add(record); }

   public LocalDate getLastUpdated() {return lastUpdated;}
   public void setLastUpdated(LocalDate lastUpdated) {this.lastUpdated = lastUpdated;}
   public String getName() { return Name; }
   public String getAge() { return Age; }
   public String getPassword(){ return Password; }
   public String getWeight() { return Weight; }
   public String getHeight() { return Height; }
   public String getBlood() { return bloodPressure; }
   public String getGender() { return Gender; }
   public String getActivity() { return Activity; }
   public void setName(String name) { this.Name = name; }
   public void setAge(String age) { this.Age = age; }
   public void setPassword(String password) { this.Password = password; }
   public void setWeight(String weight) { this.Weight = weight; }
   public void setHeight(String height) { this.Height = height; }
   public void setBlood(String blood) { this.bloodPressure = blood; }
   public void setGender(String gender) { this.Gender = gender; }
   public void setActivity(String activity) { this.Activity = activity; }

}

