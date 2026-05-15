package pet.be.pet_clinic.owner;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import pet.be.pet_clinic.model.NamedEntity;

@Entity
@Table(name = "types")
public class PetType extends NamedEntity {

}
