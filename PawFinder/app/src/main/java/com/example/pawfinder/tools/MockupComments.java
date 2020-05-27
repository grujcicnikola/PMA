package com.example.pawfinder.tools;

import com.example.pawfinder.R;
import com.example.pawfinder.model.Comment;
import com.example.pawfinder.model.Pet;
import com.example.pawfinder.model.PetGender;
import com.example.pawfinder.model.PetType;
import com.example.pawfinder.model.User;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MockupComments {

    public static List<Comment> getComments() {
        ArrayList<Comment> comments = new ArrayList<Comment>();
        Comment u1 = new Comment(Long.valueOf(1), "Video sam ga na uglu bulevara Lazara", new Date(), getUsers().get(0), getPets().get(0));
        Comment u2 = new Comment(Long.valueOf(2), "Bas je lep!", new Date(), getUsers().get(1), getPets().get(0));
        Comment u3 = new Comment(Long.valueOf(3), "Nisam ga video. Primetio sam da ima mnogo lutalica u Novom Sadu.", new Date(), getUsers().get(2), getPets().get(0));
        Comment u4 = new Comment(Long.valueOf(1), "Lep je", new Date(), getUsers().get(1), getPets().get(1));
        Comment u5 = new Comment(Long.valueOf(2), "Nadam se da cete ga pronaci!", new Date(), getUsers().get(2), getPets().get(1));

        comments.add(u1);
        comments.add(u2);
        comments.add(u3);
        comments.add(u4);
        comments.add(u5);

        return comments;

    }

    public static List<Pet> getPets() {
        ArrayList<Pet> pets = new ArrayList<Pet>();

//        User user1 = new User();
//        user1.setId(new Long(4));
//        user1.setEmail("pera@gmail.com");
//        user1.setPassword("123");
//
//        User user2 = new User();
//        user2.setId(new Long(5));
//        user2.setEmail("jova@gmail.com");
//        user2.setPassword("123");
//
//        Pet p1 = new Pet((long) 1, PetType.DOG, "Dzeki", PetGender.MALE, "Pas ima zelenu ogrlicu", R.drawable.puppydog, "23/04/2020", "021/1234", false, user1);
//        Pet p2 = new Pet((long) 2, PetType.CAT, "Djura", PetGender.MALE, "Ne prilazi nepoznatima", R.drawable.cat, "01/05/2020", "021/1234", false, user2);
//        Pet p3 = new Pet((long) 3, PetType.DOG, "Lara", PetGender.FEMALE, "Druzeljubiva, ima cip", R.drawable.dog2, "01/05/2020", "021/1234", false, user2);
//        Pet p4 = new Pet((long) 4, PetType.CAT, "Kiki", PetGender.FEMALE, "Ruska plava macka", R.drawable.russiancat, "01/05/2020", "021/1234", false, user2);
//        Pet p5 = new Pet((long) 5, PetType.DOG, "Aleks", PetGender.FEMALE, "opis 1", R.drawable.labrador, "01/05/2020", "021/1234", false, user2);
//        Pet p6 = new Pet((long) 6, PetType.DOG, "Bobi", PetGender.MALE, "opis 2", R.drawable.samojedjpg, "23/04/2020", "021/1234", false, user1);
//
//        pets.add(p1);
//        pets.add(p2);
//        pets.add(p3);
//        pets.add(p4);
//        pets.add(p5);
//        pets.add(p6);
//
        return pets;
    }

    public static List<User> getUsers() {
        ArrayList<User> users = new ArrayList<User>();
//        User user1 = new User(Long.valueOf(1), "nikolagrujcic@gmail.com", "sifra", null);
//        User user2 = new User(Long.valueOf(2), "mira@gmail.com", "sifra", null);
//        User user3 = new User(Long.valueOf(3), "ljuba@gmail.com", "sifra", null);
//        //Long id, String email, String password, Collection<Pet> pet
//
//
//        users.add(user1);
//        users.add(user2);
//        users.add(user3);

        return users;

    }

    public static List<Comment> getThisPetsComments(Pet pet) {
        ArrayList<Comment> comments = new ArrayList<Comment>();
        for (int i = 0; i < getComments().size(); i++) {
            if (getComments().get(i).getPet().getId() == pet.getId()) {
                comments.add(getComments().get(i));
            }
        }
        return comments;
    }

    public static ArrayList<Pet> getReports() {

        ArrayList<Pet> reports = new ArrayList<Pet>();

//        User user1 = new User();
//        user1.setId(new Long(1));
//        user1.setEmail("pera@gmail.com");
//        user1.setPassword("123");
//
//        Pet p1 = new Pet((long) 1, PetType.DOG, "Dzeki", PetGender.MALE, "Pas ima zelenu ogrlicu", R.drawable.puppydog, "23/04/2020", "021/1234", false, user1);
//        Pet p6 = new Pet((long) 6, PetType.DOG, "Bobi", PetGender.MALE, "opis 2", R.drawable.samojedjpg, "23/04/2020", "021/1234", false, user1);
//
//        reports.add(p1);
//        reports.add(p6);

        return reports;
    }
}
