package RNCP.TrocSkillHub.Controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import RNCP.TrocSkillHub.DTOs.AvatarDTO;
import RNCP.TrocSkillHub.Utils.AvatarConstants;

@RestController
@RequestMapping("/api/avatars")
@CrossOrigin(origins = "http://localhost:5173")
public class AvatarController {

    private static final String AVATAR_BASE_PATH = "/avatars/";

    @GetMapping
    public ResponseEntity<List<AvatarDTO>> getAvailableAvatars() {
        List<AvatarDTO> avatars = AvatarConstants.AVAILABLE_AVATAR_IDS.stream()
                .map(id -> new AvatarDTO(id, AVATAR_BASE_PATH + id + ".svg"))
                .collect(Collectors.toList());
        return ResponseEntity.ok(avatars);
    }
}