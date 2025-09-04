package com.piyush.NamekartNotesProject.Repository;

import com.piyush.NamekartNotesProject.Entity.Note;
import com.piyush.NamekartNotesProject.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note,Long> {

    List<Note> findByUser(User user);
    Optional<Note> findBySharedLink(String sharedLink);

}
