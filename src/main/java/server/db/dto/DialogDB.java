package server.db.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "dialog")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DialogDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dialog_id")
    private Integer dialogId;
    @Column(name = "dialog_name")
    private String dialogName;
    private String creator;
    private Integer type;
    @Column(name = "has_photo")
    private Boolean hasPhoto;
    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_dialog",
            joinColumns = @JoinColumn(name = "dialog_id"),
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
   private Set<UserDB> userDBS = new HashSet<>();
}
