package RNCP.TrocSkillHub.DTOs;

public class UpdateAvatarDTO {

    private String avatarId;

    public UpdateAvatarDTO() {
    }

    public UpdateAvatarDTO(String avatarId) {
        this.avatarId = avatarId;
    }

    public String getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(String avatarId) {
        this.avatarId = avatarId;
    }
}