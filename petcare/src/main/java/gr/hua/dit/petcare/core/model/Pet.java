package gr.hua.dit.petcare.core.model;


import java.util.List;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


// Represents a pet in the system
@Entity
@Table(name = "pets")
public class Pet {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank
    private String name;


    @NotBlank
    private String species;


    private String breed;

    @NotNull
    private Integer age;


    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;


    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments;


    // --- ΝΕΟ ΠΕΔΙΟ: Σημειώσεις Κτηνιάτρου ---
    @Column(length = 2000)
    private String vetNotes;


    public Pet() {}


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }
    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }
    public List<Appointment> getAppointments() { return appointments; }
    public void setAppointments(List<Appointment> appointments) { this.appointments = appointments; }


    // Getters & Setters για vetNotes
    public String getVetNotes() { return vetNotes; }
    public void setVetNotes(String vetNotes) { this.vetNotes = vetNotes; }
}
