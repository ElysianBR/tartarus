package br.com.elysian.tartarus.model;

import br.com.elysian.tartarus.model.pk.ConfirmationPK;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

@Getter
@Setter
@NoArgsConstructor
@Entity
@IdClass(ConfirmationPK.class)
@Table(schema = "tartarus", name = "confirmation")
public class Confirmation {

    private static final int MINUTES_EXPIRATION = 10;

    @Id
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Id
    @OneToOne
    @JoinColumn(name = "type_id", nullable = false)
    private ConfirmationType type;

    @Column(name = "sequence", nullable = false, length = 6)
    private String sequence;

    @Column(name = "emission", nullable = false)
    private Date emission;

    @Column(name = "expiration", nullable = false)
    private Date expiration;

    @Column(name = "date_confirmation")
    private Date dateConfirmation;


    public Confirmation(Account account, ConfirmationType type) {
        Random random = new Random();

        int maxLength = 6;
        StringBuilder sb = new StringBuilder(maxLength);
        for (int i = 0; i < maxLength; i++) {
            sb.append(random.nextInt(10));
        }

        this.sequence = sb.toString();
        this.account = account;
        this.type = type;
        this.emission = new Date();
        this.expiration = Date.from(LocalDateTime.now().plusMinutes(MINUTES_EXPIRATION).atZone(ZoneId.systemDefault()).toInstant());
    }

}
