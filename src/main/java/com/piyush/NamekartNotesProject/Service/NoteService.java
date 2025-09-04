package com.piyush.NamekartNotesProject.Service;

import com.piyush.NamekartNotesProject.Entity.Note;
import com.piyush.NamekartNotesProject.Entity.User;
import com.piyush.NamekartNotesProject.Repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NoteService {

    private  final NoteRepository noteRepository;

    // create a new note
    public Note createNote(User user,Note note){
        note.setUser(user);
        note.setCreatedAt(LocalDateTime.now());
        note.setUpdateAt(LocalDateTime.now());
        return noteRepository.save(note);
    }

    // get all note for a user
    public List<Note> getNotesByUser(User user){
        return noteRepository.findByUser(user);

    }

    // update note
    public Optional<Note> updateNote(Long noteId,Note updatedNote){
        return noteRepository.findById(noteId).map(note -> {
            note.setTitle(updatedNote.getTitle());
            note.setContent(updatedNote.getContent());
            note.setUpdateAt(LocalDateTime.now());
            return noteRepository.save(note);
        });
    }

    // delete note
    public  void deleteNote(Long noteId){
        noteRepository.deleteById(noteId);
    }

    // Generate sharable Link
    public Optional<Note> sharedNote(Long noteId){
        return noteRepository.findById(noteId).map(note -> {
            String uniqueLink= UUID.randomUUID().toString();
            note.setSharedLink(uniqueLink);
            return noteRepository.save(note);
        });
    }

    //Get note by shared Link
    public Optional<Note> getNoteBySharedLink(String sharedLink){
        return noteRepository.findBySharedLink(sharedLink);
    }
}
