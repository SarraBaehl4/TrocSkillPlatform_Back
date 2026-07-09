package RNCP.TrocSkillHub.DTOs;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String address;
    private String city;
    private String country;
    private String phoneNumber;
    private String description;
    private List<EducationDTO> education;
    private List<ExperienceDTO> experience;
    private List<ProjectDTO> project;
    private List<UserKnowledgeDTO> competences;
    private List<UserKnowledgeDTO> besoins;

    public UserDTO() {
    }

    public UserDTO(Long id, String firstName, String lastName, String email, String password,
                   String address, String city, String country, String phoneNumber, String description,
                   List<EducationDTO> education, List<ExperienceDTO> experience, List<ProjectDTO> project,
                   List<UserKnowledgeDTO> competences, List<UserKnowledgeDTO> besoins) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.address = address;
        this.city = city;
        this.country = country;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.education = education;
        this.experience = experience;
        this.project = project;
        this.competences = competences;
        this.besoins = besoins;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<EducationDTO> getEducation() {
        return education;
    }

    public void setEducation(List<EducationDTO> education) {
        this.education = education;
    }

    public List<ExperienceDTO> getExperience() {
        return experience;
    }

    public void setExperience(List<ExperienceDTO> experience) {
        this.experience = experience;
    }

    public List<ProjectDTO> getProject() {
        return project;
    }

    public void setProject(List<ProjectDTO> project) {
        this.project = project;
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