package RNCP.TrocSkillHub.Mappers;

import RNCP.TrocSkillHub.DTOs.KnowledgeDTO;
import RNCP.TrocSkillHub.Models.Category;
import RNCP.TrocSkillHub.Models.Knowledge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class KnowledgeMapperTest {

    private KnowledgeMapper knowledgeMapper;

    @BeforeEach
    void setUp() {
        knowledgeMapper = Mappers.getMapper(KnowledgeMapper.class);
    }

    private Category buildCategory(Long id, String name) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        return category;
    }

    @Test
    void toDTO_ShouldConvertKnowledgeToKnowledgeDTO() {
        Knowledge knowledge = new Knowledge();
        knowledge.setId(1L);
        knowledge.setName("Java Programming");
        knowledge.setCategory(buildCategory(10L, "Informatique"));

        KnowledgeDTO result = knowledgeMapper.toDTO(knowledge);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Java Programming");
        assertThat(result.getCategoryId()).isEqualTo(10L);
        assertThat(result.getCategoryName()).isEqualTo("Informatique");
    }

    @Test
    void toDTO_ShouldReturnNull_WhenKnowledgeIsNull() {

        KnowledgeDTO result = knowledgeMapper.toDTO(null);

        assertThat(result).isNull();
    }

    @Test
    void toDTO_ShouldMapAllProperties() {

        Knowledge knowledge = new Knowledge();
        knowledge.setId(99L);
        knowledge.setName("Python");
        knowledge.setCategory(buildCategory(5L, "Programmation"));

        KnowledgeDTO result = knowledgeMapper.toDTO(knowledge);

        assertThat(result).isNotNull();
        assertThat(result)
            .extracting(
                KnowledgeDTO::getId,
                KnowledgeDTO::getName,
                KnowledgeDTO::getCategoryId,
                KnowledgeDTO::getCategoryName
            )
            .containsExactly(99L, "Python", 5L, "Programmation");
    }

    @Test
    void toDTO_ShouldHandleKnowledgeWithNullCategory() {

        Knowledge knowledge = new Knowledge();
        knowledge.setId(10L);
        knowledge.setName("React");
        knowledge.setCategory(null);

        KnowledgeDTO result = knowledgeMapper.toDTO(knowledge);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getName()).isEqualTo("React");
        assertThat(result.getCategoryId()).isNull();
        assertThat(result.getCategoryName()).isNull();
    }

    @Test
    void toDTOList_ShouldConvertKnowledgeListToKnowledgeDTOList() {

        Knowledge knowledge1 = new Knowledge();
        knowledge1.setId(1L);
        knowledge1.setName("Java");
        knowledge1.setCategory(buildCategory(1L, "Informatique"));

        Knowledge knowledge2 = new Knowledge();
        knowledge2.setId(2L);
        knowledge2.setName("SQL");
        knowledge2.setCategory(buildCategory(2L, "Base de données"));

        Knowledge knowledge3 = new Knowledge();
        knowledge3.setId(3L);
        knowledge3.setName("Git");
        knowledge3.setCategory(buildCategory(1L, "Informatique"));

        List<Knowledge> knowledges = Arrays.asList(knowledge1, knowledge2, knowledge3);

        List<KnowledgeDTO> result = knowledgeMapper.toDTOList(knowledges);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);

        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getName()).isEqualTo("Java");
        assertThat(result.get(0).getCategoryId()).isEqualTo(1L);
        assertThat(result.get(0).getCategoryName()).isEqualTo("Informatique");

        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getName()).isEqualTo("SQL");
        assertThat(result.get(1).getCategoryId()).isEqualTo(2L);
        assertThat(result.get(1).getCategoryName()).isEqualTo("Base de données");

        assertThat(result.get(2).getId()).isEqualTo(3L);
        assertThat(result.get(2).getName()).isEqualTo("Git");
        assertThat(result.get(2).getCategoryId()).isEqualTo(1L);
        assertThat(result.get(2).getCategoryName()).isEqualTo("Informatique");
    }

    @Test
    void toDTOList_ShouldReturnEmptyList_WhenKnowledgesListIsEmpty() {

        List<Knowledge> emptyList = Collections.emptyList();

        List<KnowledgeDTO> result = knowledgeMapper.toDTOList(emptyList);

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    void toDTOList_ShouldReturnNull_WhenKnowledgesListIsNull() {

        List<KnowledgeDTO> result = knowledgeMapper.toDTOList(null);

        assertThat(result).isNull();
    }

    @Test
    void toDTOList_ShouldHandleSingleElementList() {

        Knowledge knowledge = new Knowledge();
        knowledge.setId(10L);
        knowledge.setName("MongoDB");
        knowledge.setCategory(buildCategory(4L, "Base de données"));

        List<Knowledge> singleKnowledge = Collections.singletonList(knowledge);

        List<KnowledgeDTO> result = knowledgeMapper.toDTOList(singleKnowledge);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(10L);
        assertThat(result.get(0).getName()).isEqualTo("MongoDB");
        assertThat(result.get(0).getCategoryId()).isEqualTo(4L);
        assertThat(result.get(0).getCategoryName()).isEqualTo("Base de données");
    }

    @Test
    void toDTOList_ShouldHandleDifferentCategories() {

        Knowledge k1 = new Knowledge();
        k1.setId(1L);
        k1.setName("Java");
        k1.setCategory(buildCategory(1L, "Informatique"));

        Knowledge k2 = new Knowledge();
        k2.setId(2L);
        k2.setName("Photoshop");
        k2.setCategory(buildCategory(2L, "Design"));

        Knowledge k3 = new Knowledge();
        k3.setId(3L);
        k3.setName("Guitar");
        k3.setCategory(buildCategory(3L, "Musique"));

        List<Knowledge> knowledges = Arrays.asList(k1, k2, k3);

        List<KnowledgeDTO> result = knowledgeMapper.toDTOList(knowledges);

        assertThat(result).hasSize(3);
        assertThat(result)
            .extracting(KnowledgeDTO::getCategoryId)
            .containsExactly(1L, 2L, 3L);
        assertThat(result)
            .extracting(KnowledgeDTO::getCategoryName)
            .containsExactly("Informatique", "Design", "Musique");
    }


    @Test
    void toDTO_ShouldHandleEmptyStrings() {

        Knowledge knowledge = new Knowledge();
        knowledge.setId(15L);
        knowledge.setName("");
        knowledge.setCategory(buildCategory(1L, ""));

        KnowledgeDTO result = knowledgeMapper.toDTO(knowledge);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(15L);
        assertThat(result.getName()).isEmpty();
        assertThat(result.getCategoryId()).isEqualTo(1L);
        assertThat(result.getCategoryName()).isEmpty();
    }

    @Test
    void toDTO_ShouldHandleLongName() {

        Knowledge knowledge = new Knowledge();
        knowledge.setId(20L);
        knowledge.setName("Machine Learning and Deep Neural Networks with TensorFlow and PyTorch");
        knowledge.setCategory(buildCategory(10L, "Informatique"));

        KnowledgeDTO result = knowledgeMapper.toDTO(knowledge);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(20L);
        assertThat(result.getName()).isEqualTo("Machine Learning and Deep Neural Networks with TensorFlow and PyTorch");
    }

    @Test
    void toDTO_ShouldHandleSpecialCharactersInName() {

        Knowledge knowledge = new Knowledge();
        knowledge.setId(25L);
        knowledge.setName("C++ & C#");
        knowledge.setCategory(buildCategory(5L, "Informatique"));

        KnowledgeDTO result = knowledgeMapper.toDTO(knowledge);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("C++ & C#");
    }
}