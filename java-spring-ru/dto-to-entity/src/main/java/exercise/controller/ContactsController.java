package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import exercise.model.Contact;
import exercise.repository.ContactRepository;
import exercise.dto.ContactDTO;
import exercise.dto.ContactCreateDTO;

@RestController
@RequestMapping("/contacts")
public class ContactsController {

    @Autowired
    private ContactRepository contactRepository;

    // BEGIN
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ContactDTO create(@RequestBody ContactCreateDTO contact) {
        Contact entity = toEntity(contact);
        contactRepository.save(entity);

        return toDTO(entity);
    }

    private Contact toEntity(ContactCreateDTO contact) {
        var entity = new Contact();

        entity.setPhone(contact.getPhone());
        entity.setFirstName(contact.getFirstName());
        entity.setLastName(contact.getLastName());

        return entity;
    }

    private ContactDTO toDTO(Contact contact) {
        var contactDto = new ContactDTO();

        contactDto.setPhone(contact.getPhone());
        contactDto.setFirstName(contact.getFirstName());
        contactDto.setLastName(contact.getLastName());
        contactDto.setId(contact.getId());
        contactDto.setCreatedAt(contact.getCreatedAt());
        contactDto.setUpdatedAt(contact.getUpdatedAt());

        return contactDto;
    }
    // END
}
