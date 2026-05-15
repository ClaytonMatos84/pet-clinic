package pet.be.pet_clinic.vet;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import pet.be.pet_clinic.model.NamedEntity;

@Entity
@Table(name = "specialties")
public class Specialty extends NamedEntity {

}
