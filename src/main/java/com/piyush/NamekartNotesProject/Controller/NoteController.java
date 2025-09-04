package com.piyush.NamekartNotesProject.Controller;

import com.piyush.NamekartNotesProject.Util.JwtUtil;
import com.piyush.NamekartNotesProject.Entity.Note;
import com.piyush.NamekartNotesProject.Entity.User;
import com.piyush.NamekartNotesProject.Service.NoteService;
import com.piyush.NamekartNotesProject.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    private User validateAndGetUser(String authHeader){
        if(authHeader==null || !authHeader.startsWith("Bearer ")){
            throw  new RuntimeException("Missing or Invalid Authorization header");
        }
        String token= authHeader.substring(7);
        String username=jwtUtil.extractUsername(token);

        if(!jwtUtil.validateToken(token,username)){
            throw new RuntimeException("Invalid or Expired token");
        }
        return userService.findByUsername(username)
                .orElseThrow(()->new RuntimeException("Usernot found"));
    }

    // create Note
    @PostMapping
    public ResponseEntity<?> createNote(@RequestHeader("Authorization") String authHeader,@RequestBody Note note){
        User user= validateAndGetUser(authHeader);
        return ResponseEntity.ok(noteService.createNote(user,note));
    }

    // Get Note For Logged-in User
    @GetMapping
    public ResponseEntity<List<Note>> getNotes(@RequestHeader("Authorization") String authHeader){

        User user= validateAndGetUser(authHeader);
        return ResponseEntity.ok(noteService.getNotesByUser(user));
    }

    // update Note
    @PutMapping("/{id}")
    public  ResponseEntity<?> updateNote(@RequestHeader("Authorization") String authHeader,@PathVariable Long id, @RequestBody Note updateNote){
        validateAndGetUser(authHeader);
        return noteService.updateNote(id,updateNote)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete Note
    @DeleteMapping("/{id}")
    public  ResponseEntity<?> deleteNote( @RequestHeader("Authorization") String authHeader,@PathVariable Long id){
       validateAndGetUser(authHeader);
        noteService.deleteNote(id);
        return  ResponseEntity.ok("Note Deleted Successfully");
    }

    // share Notes
    @PostMapping("/{id}/share")
    public ResponseEntity<?> shareNote( @RequestHeader("Authorization") String authHeader,@PathVariable Long id){
       validateAndGetUser(authHeader);
        return noteService.sharedNote(id)
                .map(note -> ResponseEntity.ok("Shared Link: /api/notes/shared/"+note.getSharedLink()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/shared/{link}")
    public ResponseEntity<?> getSharedNote(@PathVariable String link) {
        return noteService.getNoteBySharedLink(link)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
