package RNCP.TrocSkillHub.Services;

import RNCP.TrocSkillHub.DTOs.KnowledgeDTO;
import RNCP.TrocSkillHub.Mappers.KnowledgeMapper;
import RNCP.TrocSkillHub.Models.Category;
import RNCP.TrocSkillHub.Models.Knowledge;
import RNCP.TrocSkillHub.Repositories.CategoryRepository;
import RNCP.TrocSkillHub.Repositories.KnowledgeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class KnowledgeServiceTest {

    @Mock
    private KnowledgeRepository knowledgeRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private KnowledgeMapper knowledgeMapper;

    @InjectMocks
    private KnowledgeService knowledgeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Category buildCategory(Long id, String name) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        return category;
    }

    @Test
    void getAllKnowledges_ShouldReturnListOfKnowledgeDTOs() {
        Knowledge knowledge1 = new Knowledge();
        knowledge1.setId(1L);
        knowledge1.setName("Java");
        knowledge1.setCategory(buildCategory(10L, "Informatique"));

        Knowledge knowledge2 = new Knowledge();
        knowledge2.setId(2L);
        knowledge2.setName("Pâtisserie");
        knowledge2.setCategory(buildCategory(15L, "Cuisine"));

        List<Knowledge> knowledges = Arrays.asList(knowledge1, knowledge2);

        KnowledgeDTO dto1 = new KnowledgeDTO();
        dto1.setId(1L);
        dto1.setName("Java");
        dto1.setCategoryId(10L);
        dto1.setCategoryName("Informatique");

        KnowledgeDTO dto2 = new KnowledgeDTO();
        dto2.setId(2L);
        dto2.setName("Pâtisserie");
        dto2.setCategoryId(15L);
        dto2.setCategoryName("Cuisine");

        List<KnowledgeDTO> expectedDTOs = Arrays.asList(dto1, dto2);

        when(knowledgeRepository.findAll()).thenReturn(knowledges);
        when(knowledgeMapper.toDTOList(knowledges)).thenReturn(expectedDTOs);

        List<KnowledgeDTO> result = knowledgeService.getAllKnowledges();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Java");
        assertThat(result.get(0).getCategoryId()).isEqualTo(10L);
        assertThat(result.get(1).getName()).isEqualTo("Pâtisserie");
        assertThat(result.get(1).getCategoryId()).isEqualTo(15L);

        verify(knowledgeRepository, times(1)).findAll();
        verify(knowledgeMapper, times(1)).toDTOList(knowledges);
    }

    @Test
    void getAllKnowledges_ShouldReturnEmptyList_WhenNoKnowledgesExist() {
        List<Knowledge> emptyKnowledges = Collections.emptyList();
        List<KnowledgeDTO> emptyDTOs = Collections.emptyList();

        when(knowledgeRepository.findAll()).thenReturn(emptyKnowledges);
        when(knowledgeMapper.toDTOList(emptyKnowledges)).thenReturn(emptyDTOs);

        List<KnowledgeDTO> result = knowledgeService.getAllKnowledges();

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(knowledgeRepository, times(1)).findAll();
        verify(knowledgeMapper, times(1)).toDTOList(emptyKnowledges);
    }

    @Test
    void getAllKnowledges_ShouldCallRepositoryAndMapper() {
        List<Knowledge> knowledges = Arrays.asList(new Knowledge());
        List<KnowledgeDTO> dtos = Arrays.asList(new KnowledgeDTO());

        when(knowledgeRepository.findAll()).thenReturn(knowledges);
        when(knowledgeMapper.toDTOList(anyList())).thenReturn(dtos);

        knowledgeService.getAllKnowledges();

        verify(knowledgeRepository, times(1)).findAll();
        verify(knowledgeMapper, times(1)).toDTOList(knowledges);
        verifyNoMoreInteractions(knowledgeRepository, knowledgeMapper);
    }

    @Test
    void getKnowledgeById_ShouldReturnKnowledgeDTO_WhenKnowledgeExists() {
        Long knowledgeId = 1L;
        Knowledge knowledge = new Knowledge();
        knowledge.setId(knowledgeId);
        knowledge.setName("Spring Boot");
        knowledge.setCategory(buildCategory(10L, "Informatique"));

        KnowledgeDTO expectedDTO = new KnowledgeDTO();
        expectedDTO.setId(knowledgeId);
        expectedDTO.setName("Spring Boot");
        expectedDTO.setCategoryId(10L);
        expectedDTO.setCategoryName("Informatique");

        when(knowledgeRepository.findById(knowledgeId)).thenReturn(Optional.of(knowledge));
        when(knowledgeMapper.toDTO(knowledge)).thenReturn(expectedDTO);

        KnowledgeDTO result = knowledgeService.getKnowledgeById(knowledgeId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(knowledgeId);
        assertThat(result.getName()).isEqualTo("Spring Boot");
        assertThat(result.getCategoryId()).isEqualTo(10L);

        verify(knowledgeRepository, times(1)).findById(knowledgeId);
        verify(knowledgeMapper, times(1)).toDTO(knowledge);
    }

    @Test
    void getKnowledgeById_ShouldThrowException_WhenKnowledgeNotFound() {
        Long knowledgeId = 999L;
        when(knowledgeRepository.findById(knowledgeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> knowledgeService.getKnowledgeById(knowledgeId))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Knowledge not found with id: " + knowledgeId);

        verify(knowledgeRepository, times(1)).findById(knowledgeId);
        verify(knowledgeMapper, never()).toDTO(any());
    }

    @Test
    void getKnowledgeById_ShouldCallRepositoryWithCorrectId() {
        Long knowledgeId = 5L;
        Knowledge knowledge = new Knowledge();
        KnowledgeDTO dto = new KnowledgeDTO();

        when(knowledgeRepository.findById(knowledgeId)).thenReturn(Optional.of(knowledge));
        when(knowledgeMapper.toDTO(knowledge)).thenReturn(dto);

        knowledgeService.getKnowledgeById(knowledgeId);

        verify(knowledgeRepository, times(1)).findById(knowledgeId);
    }

    @Test
    void createKnowledge_ShouldSaveAndReturnKnowledgeDTO() {
        KnowledgeDTO inputDTO = new KnowledgeDTO();
        inputDTO.setName("Docker");
        inputDTO.setCategoryId(10L);

        Category category = buildCategory(10L, "Informatique");

        Knowledge savedEntity = new Knowledge();
        savedEntity.setId(1L);
        savedEntity.setName("Docker");
        savedEntity.setCategory(category);

        KnowledgeDTO expectedDTO = new KnowledgeDTO();
        expectedDTO.setId(1L);
        expectedDTO.setName("Docker");
        expectedDTO.setCategoryId(10L);
        expectedDTO.setCategoryName("Informatique");

        when(categoryRepository.findById(10L)).thenReturn(Optional.of(category));
        when(knowledgeRepository.save(any(Knowledge.class))).thenReturn(savedEntity);
        when(knowledgeMapper.toDTO(savedEntity)).thenReturn(expectedDTO);

        KnowledgeDTO result = knowledgeService.createKnowledge(inputDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Docker");
        assertThat(result.getCategoryId()).isEqualTo(10L);

        verify(categoryRepository, times(1)).findById(10L);
        verify(knowledgeRepository, times(1)).save(any(Knowledge.class));
        verify(knowledgeMapper, times(1)).toDTO(savedEntity);
    }

    @Test
    void createKnowledge_ShouldThrowException_WhenCategoryNotFound() {
        KnowledgeDTO inputDTO = new KnowledgeDTO();
        inputDTO.setName("Docker");
        inputDTO.setCategoryId(999L);

        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> knowledgeService.createKnowledge(inputDTO))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Category not found with id: " + 999L);

        verify(categoryRepository, times(1)).findById(999L);
        verify(knowledgeRepository, never()).save(any());
        verify(knowledgeMapper, never()).toDTO(any());
    }

    @Test
    void createKnowledge_ShouldFollowCorrectExecutionFlow() {
        KnowledgeDTO inputDTO = new KnowledgeDTO();
        inputDTO.setName("Kubernetes");
        inputDTO.setCategoryId(10L);

        Category category = buildCategory(10L, "Informatique");
        Knowledge savedEntity = new Knowledge();
        KnowledgeDTO outputDTO = new KnowledgeDTO();

        when(categoryRepository.findById(10L)).thenReturn(Optional.of(category));
        when(knowledgeRepository.save(any(Knowledge.class))).thenReturn(savedEntity);
        when(knowledgeMapper.toDTO(any(Knowledge.class))).thenReturn(outputDTO);

        KnowledgeDTO result = knowledgeService.createKnowledge(inputDTO);

        assertThat(result).isNotNull();

        var inOrder = inOrder(categoryRepository, knowledgeRepository, knowledgeMapper);
        inOrder.verify(categoryRepository).findById(10L);
        inOrder.verify(knowledgeRepository).save(any(Knowledge.class));
        inOrder.verify(knowledgeMapper).toDTO(savedEntity);
    }

    @Test
    void createKnowledge_ShouldHandleDifferentCategories() {
        KnowledgeDTO inputDTO = new KnowledgeDTO();
        inputDTO.setName("Yoga");
        inputDTO.setCategoryId(20L);

        Category category = buildCategory(20L, "Sport");

        Knowledge savedEntity = new Knowledge();
        savedEntity.setId(10L);
        savedEntity.setCategory(category);

        KnowledgeDTO outputDTO = new KnowledgeDTO();
        outputDTO.setId(10L);
        outputDTO.setCategoryId(20L);
        outputDTO.setCategoryName("Sport");

        when(categoryRepository.findById(20L)).thenReturn(Optional.of(category));
        when(knowledgeRepository.save(any(Knowledge.class))).thenReturn(savedEntity);
        when(knowledgeMapper.toDTO(savedEntity)).thenReturn(outputDTO);

        KnowledgeDTO result = knowledgeService.createKnowledge(inputDTO);

        assertThat(result.getCategoryId()).isEqualTo(20L);
        assertThat(result.getId()).isNotEqualTo(result.getCategoryId());
    }

    @Test
    void updateKnowledge_ShouldUpdateAndReturnKnowledgeDTO_WhenKnowledgeExists() {
        Long knowledgeId = 1L;
        KnowledgeDTO inputDTO = new KnowledgeDTO();
        inputDTO.setName("React Updated");
        inputDTO.setCategoryId(10L);

        Category category = buildCategory(10L, "Informatique");

        Knowledge existingKnowledge = new Knowledge();
        existingKnowledge.setId(knowledgeId);
        existingKnowledge.setName("React");
        existingKnowledge.setCategory(category);

        Knowledge updatedEntity = new Knowledge();
        updatedEntity.setId(knowledgeId);
        updatedEntity.setName("React Updated");
        updatedEntity.setCategory(category);

        KnowledgeDTO expectedDTO = new KnowledgeDTO();
        expectedDTO.setId(knowledgeId);
        expectedDTO.setName("React Updated");
        expectedDTO.setCategoryId(10L);
        expectedDTO.setCategoryName("Informatique");

        when(knowledgeRepository.findById(knowledgeId)).thenReturn(Optional.of(existingKnowledge));
        when(categoryRepository.findById(10L)).thenReturn(Optional.of(category));
        when(knowledgeRepository.save(existingKnowledge)).thenReturn(updatedEntity);
        when(knowledgeMapper.toDTO(updatedEntity)).thenReturn(expectedDTO);

        KnowledgeDTO result = knowledgeService.updateKnowledge(knowledgeId, inputDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(knowledgeId);
        assertThat(result.getName()).isEqualTo("React Updated");
        assertThat(result.getCategoryId()).isEqualTo(10L);

        verify(knowledgeRepository, times(1)).findById(knowledgeId);
        verify(categoryRepository, times(1)).findById(10L);
        verify(knowledgeRepository, times(1)).save(existingKnowledge);
        verify(knowledgeMapper, times(1)).toDTO(updatedEntity);
    }

    @Test
    void updateKnowledge_ShouldThrowException_WhenKnowledgeNotFound() {
        Long knowledgeId = 999L;
        KnowledgeDTO inputDTO = new KnowledgeDTO();
        inputDTO.setName("Non-existent");
        inputDTO.setCategoryId(10L);

        when(knowledgeRepository.findById(knowledgeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> knowledgeService.updateKnowledge(knowledgeId, inputDTO))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Knowledge not found with id: " + knowledgeId);

        verify(knowledgeRepository, times(1)).findById(knowledgeId);
        verify(categoryRepository, never()).findById(any());
        verify(knowledgeRepository, never()).save(any());
        verify(knowledgeMapper, never()).toDTO(any());
    }

    @Test
    void updateKnowledge_ShouldThrowException_WhenCategoryNotFound() {
        Long knowledgeId = 1L;
        KnowledgeDTO inputDTO = new KnowledgeDTO();
        inputDTO.setName("React Updated");
        inputDTO.setCategoryId(999L);

        Knowledge existingKnowledge = new Knowledge();
        existingKnowledge.setId(knowledgeId);
        existingKnowledge.setName("React");
        existingKnowledge.setCategory(buildCategory(10L, "Informatique"));

        when(knowledgeRepository.findById(knowledgeId)).thenReturn(Optional.of(existingKnowledge));
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> knowledgeService.updateKnowledge(knowledgeId, inputDTO))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Category not found with id: " + 999L);

        verify(knowledgeRepository, times(1)).findById(knowledgeId);
        verify(categoryRepository, times(1)).findById(999L);
        verify(knowledgeRepository, never()).save(any());
        verify(knowledgeMapper, never()).toDTO(any());
    }

    @Test
    void updateKnowledge_ShouldUpdateAllFields() {
        Long knowledgeId = 2L;
        KnowledgeDTO inputDTO = new KnowledgeDTO();
        inputDTO.setName("Vue.js");
        inputDTO.setCategoryId(10L);

        Category oldCategory = buildCategory(15L, "Cuisine");
        Category newCategory = buildCategory(10L, "Informatique");

        Knowledge existingKnowledge = new Knowledge();
        existingKnowledge.setId(knowledgeId);
        existingKnowledge.setName("Old Name");
        existingKnowledge.setCategory(oldCategory);

        Knowledge updatedEntity = new Knowledge();
        KnowledgeDTO outputDTO = new KnowledgeDTO();

        when(knowledgeRepository.findById(knowledgeId)).thenReturn(Optional.of(existingKnowledge));
        when(categoryRepository.findById(10L)).thenReturn(Optional.of(newCategory));
        when(knowledgeRepository.save(existingKnowledge)).thenReturn(updatedEntity);
        when(knowledgeMapper.toDTO(updatedEntity)).thenReturn(outputDTO);

        knowledgeService.updateKnowledge(knowledgeId, inputDTO);

        assertThat(existingKnowledge.getName()).isEqualTo("Vue.js");
        assertThat(existingKnowledge.getCategory()).isEqualTo(newCategory);

        verify(knowledgeRepository, times(1)).save(existingKnowledge);
    }

    @Test
    void updateKnowledge_ShouldFollowCorrectExecutionFlow() {
        Long knowledgeId = 3L;
        KnowledgeDTO inputDTO = new KnowledgeDTO();
        inputDTO.setName("Angular");
        inputDTO.setCategoryId(10L);

        Category category = buildCategory(10L, "Informatique");
        Knowledge existingKnowledge = new Knowledge();
        Knowledge updatedEntity = new Knowledge();
        KnowledgeDTO outputDTO = new KnowledgeDTO();

        when(knowledgeRepository.findById(knowledgeId)).thenReturn(Optional.of(existingKnowledge));
        when(categoryRepository.findById(10L)).thenReturn(Optional.of(category));
        when(knowledgeRepository.save(existingKnowledge)).thenReturn(updatedEntity);
        when(knowledgeMapper.toDTO(updatedEntity)).thenReturn(outputDTO);

        knowledgeService.updateKnowledge(knowledgeId, inputDTO);

        var inOrder = inOrder(knowledgeRepository, categoryRepository, knowledgeMapper);
        inOrder.verify(knowledgeRepository).findById(knowledgeId);
        inOrder.verify(categoryRepository).findById(10L);
        inOrder.verify(knowledgeRepository).save(existingKnowledge);
        inOrder.verify(knowledgeMapper).toDTO(updatedEntity);
    }

    @Test
    void updateKnowledge_ShouldChangeCategory() {
        Long knowledgeId = 7L;
        KnowledgeDTO inputDTO = new KnowledgeDTO();
        inputDTO.setName("Course à pied");
        inputDTO.setCategoryId(20L);

        Category oldCategory = buildCategory(10L, "Informatique");
        Category newCategory = buildCategory(20L, "Sport");

        Knowledge existingKnowledge = new Knowledge();
        existingKnowledge.setId(knowledgeId);
        existingKnowledge.setName("Course à pied");
        existingKnowledge.setCategory(oldCategory);

        Knowledge updatedEntity = new Knowledge();
        updatedEntity.setCategory(newCategory);

        KnowledgeDTO outputDTO = new KnowledgeDTO();
        outputDTO.setCategoryId(20L);

        when(knowledgeRepository.findById(knowledgeId)).thenReturn(Optional.of(existingKnowledge));
        when(categoryRepository.findById(20L)).thenReturn(Optional.of(newCategory));
        when(knowledgeRepository.save(existingKnowledge)).thenReturn(updatedEntity);
        when(knowledgeMapper.toDTO(updatedEntity)).thenReturn(outputDTO);

        KnowledgeDTO result = knowledgeService.updateKnowledge(knowledgeId, inputDTO);

        assertThat(existingKnowledge.getCategory()).isEqualTo(newCategory);
        assertThat(result.getCategoryId()).isEqualTo(20L);
    }

    @Test
    void deleteKnowledge_ShouldDeleteKnowledge_WhenKnowledgeExists() {
        Long knowledgeId = 1L;
        Knowledge knowledge = new Knowledge();
        knowledge.setId(knowledgeId);
        knowledge.setName("MongoDB");
        knowledge.setCategory(buildCategory(10L, "Informatique"));

        when(knowledgeRepository.findById(knowledgeId)).thenReturn(Optional.of(knowledge));
        doNothing().when(knowledgeRepository).delete(knowledge);

        knowledgeService.deleteKnowledge(knowledgeId);

        verify(knowledgeRepository, times(1)).findById(knowledgeId);
        verify(knowledgeRepository, times(1)).delete(knowledge);
    }

    @Test
    void deleteKnowledge_ShouldThrowException_WhenKnowledgeNotFound() {
        Long knowledgeId = 999L;
        when(knowledgeRepository.findById(knowledgeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> knowledgeService.deleteKnowledge(knowledgeId))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Knowledge not found with id: " + knowledgeId);

        verify(knowledgeRepository, times(1)).findById(knowledgeId);
        verify(knowledgeRepository, never()).delete(any());
    }

    @Test
    void deleteKnowledge_ShouldCallRepositoryWithCorrectEntity() {
        Long knowledgeId = 5L;
        Knowledge knowledge = new Knowledge();
        knowledge.setId(knowledgeId);
        knowledge.setCategory(buildCategory(15L, "Cuisine"));

        when(knowledgeRepository.findById(knowledgeId)).thenReturn(Optional.of(knowledge));
        doNothing().when(knowledgeRepository).delete(knowledge);

        knowledgeService.deleteKnowledge(knowledgeId);

        verify(knowledgeRepository, times(1)).delete(knowledge);
    }

    @Test
    void deleteKnowledge_ShouldFollowCorrectExecutionFlow() {
        Long knowledgeId = 10L;
        Knowledge knowledge = new Knowledge();
        knowledge.setCategory(buildCategory(20L, "Sport"));

        when(knowledgeRepository.findById(knowledgeId)).thenReturn(Optional.of(knowledge));
        doNothing().when(knowledgeRepository).delete(knowledge);

        knowledgeService.deleteKnowledge(knowledgeId);

        var inOrder = inOrder(knowledgeRepository);
        inOrder.verify(knowledgeRepository).findById(knowledgeId);
        inOrder.verify(knowledgeRepository).delete(knowledge);
    }
}