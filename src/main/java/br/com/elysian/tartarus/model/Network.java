package br.com.elysian.tartarus.model;

import br.com.elysian.tartarus.view.RoleViews;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "tartarus", name = "network")
//@JsonView(RoleViews.AccountView.class)
public class Network {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 10)
    private Long id;

    @Column(name = "network_url", nullable = false)
    private String networkUrl;

    @Column(name = "active")
    @ColumnDefault("true")
    private boolean active = true;

    @Column(name = "tag", length = 5, nullable = false, unique = true)
    private String tag;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToOne
    @JoinColumn(name = "type_id", nullable = false)
    private NetworkType type;

    @OneToOne
    @JoinColumn(name = "blockchain_id")
    private Blockchain blockchain;

    @Column(name = "infura_url")
    private String infuraUrl;


    public String getNetworkNameFormated() {
        return tag + " - " + name;
    }

}
