package com.example.springsecurityclient.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Calendar;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class PasswordResetToken {
    private static final int EXPIRATION_TIME = 10; //exp time 10 mins
    @Id
    //IDENTITY is used when database provides ways to auto-generate the ids
    //when strategy is AUTO, persistence provider is responsible
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private Date expirationTime;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "user_id",
            //if referencedColumnName is not given JPA will automatically assume it's the primary key
            referencedColumnName = "id", //user_id column in the VerificationToken maps to ID column in the USER
            //The foreignKey attribute does not change the existence of a column or its fundamental behavior
            foreignKey = @ForeignKey(name = "FK_USER_PASSWORD_TOKEN")
            //foreignKey does not change the fundamental str of the fields in any tables
            //It merely names the constraint that enforces the foreign key relationship in the database.
    )
    private User user;

    public PasswordResetToken(User user, String token) {
        super();
        this.token = token;
        this.user = user;
        this.expirationTime = calculateExpirationTime(EXPIRATION_TIME);
    }

    public PasswordResetToken(String token) {
        super();
        this.token = token;
        this.expirationTime = calculateExpirationTime(EXPIRATION_TIME);
    }

    private Date calculateExpirationTime(int expirationTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime()); //may be this line is redundant but think multithreading
        calendar.add(Calendar.MINUTE, expirationTime);
        //The entire expression new Date(calendar.getTime().getTime()) essentially converts the Calendar instance to a
        // Date object, and then immediately back to milliseconds, which is then used to create a new Date object.
        return new Date(calendar.getTime().getTime());
        //it's a common way to convert between Calendar and Date in Java, especially if you're dealing with an API that
        // requires a Date object but you prefer the Calendar class for date-time calculations

        //what we are doing is:
//        Getting the time from the Calendar as a Date.
//                Then getting the time from the Date as milliseconds.
//        And then creating a new Date object with those milliseconds.
    }
    //Returning new Date(calendar.getTime().getTime()) instead of calendar.getTime() can be preferable in situations
    // where you want to ensure that the returned Date object is a separate instance from the one held by the Calendar.

    //This is primarily a concern for maintaining immutability and preventing unintentional side effects.

    //In a multi-threaded environment, if the Calendar instance is shared across threads,
    // it's safer to return a new Date instance to avoid concurrent modification issues.
}
