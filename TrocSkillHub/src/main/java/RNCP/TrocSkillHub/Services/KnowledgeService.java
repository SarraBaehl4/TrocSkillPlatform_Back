package RNCP.TrocSkillHub.Services;

import RNCP.TrocSkillHub.DTOs.KnowledgeDTO;
import RNCP.TrocSkillHub.Mappers.KnowledgeMapper;
import RNCP.TrocSkillHub.Models.Category;
import RNCP.TrocSkillHub.Models.Knowledge;
import RNCP.TrocSkillHub.Repositories.CategoryRepository;
import RNCP.TrocSkillHub.Repositories.KnowledgeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class KnowledgeService {
    
    private final KnowledgeRepository knowledgeRepository;
    private final CategoryRepository categoryRepository;
    private final KnowledgeMapper knowledgeMapper;
    
    public KnowledgeService(KnowledgeRepository knowledgeRepository,
                             CategoryRepository categoryRepository,
                             KnowledgeMapper knowledgeMapper) {
        this.knowledgeRepository = knowledgeRepository;
        this.categoryRepository = categoryRepository;
        this.knowledgeMapper = knowledgeMapper;
    }
    
    // GET ALL
    public List<KnowledgeDTO> getAllKnowledges() {
        List<Knowledge> knowledges = knowledgeRepository.findAll();
        return knowledgeMapper.toDTOList(knowledges);
    }
    
    // GET BY ID
    public KnowledgeDTO getKnowledgeById(Long id) {
        Knowledge knowledge = knowledgeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Knowledge not found with id: " + id));
        return knowledgeMapper.toDTO(knowledge);
    }
    
    // CREATE
    @Transactional
    public KnowledgeDTO createKnowledge(KnowledgeDTO knowledgeDTO) {
        Category category = categoryRepository.findById(knowledgeDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + knowledgeDTO.getCategoryId()));
        
        Knowledge knowledge = new Knowledge();
        knowledge.setName(knowledgeDTO.getName());
        knowledge.setCategory(category);
        
        Knowledge savedKnowledge = knowledgeRepository.save(knowledge);
        return knowledgeMapper.toDTO(savedKnowledge);
    }
    
    // UPDATE
    @Transactional
    public KnowledgeDTO updateKnowledge(Long id, KnowledgeDTO knowledgeDTO) {
        Knowledge existingKnowledge = knowledgeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Knowledge not found with id: " + id));
        
        Category category = categoryRepository.findById(knowledgeDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + knowledgeDTO.getCategoryId()));
        
        existingKnowledge.setName(knowledgeDTO.getName());
        existingKnowledge.setCategory(category);
        
        Knowledge updatedKnowledge = knowledgeRepository.save(existingKnowledge);
        return knowledgeMapper.toDTO(updatedKnowledge);
    }
    
    // DELETE
    @Transactional
    public void deleteKnowledge(Long id) {
        Knowledge knowledge = knowledgeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Knowledge not found with id: " + id));
        knowledgeRepository.delete(knowledge);
    }
}