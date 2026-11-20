package RNCP.TrocSkillHub.DTOs;

import java.util.List;

public class UserCardDTO {

    private Long id;
    private String pseudo;
    private String pictureUrl;
    private List<UserKnowledgeDTO> competences;
    private List<UserKnowledgeDTO> besoins;

    public UserCardDTO() {
    }

    public UserCardDTO(Long id, String pseudo, String pictureUrl,
                        List<UserKnowledgeDTO> competences, List<UserKnowledgeDTO> besoins) {
        this.id = id;
        this.pseudo = pseudo;
        this.pictureUrl = pictureUrl;
        this.competences = competences;
        this.besoins = besoins;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public List<UserKnowledgeDTO> getCompetences() {
        return competences;
    }

    public void setCompetences(List<UserKnowledgeDTO> competences) {
        this.competences = competences;
    }

    public List<UserKnowledgeDTO> getBesoins() {
        return besoins;
    }

    public void setBesoins(List<UserKnowledgeDTO> besoins) {
        this.besoins = besoins;
    }
}