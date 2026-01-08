package com.example.service_cabinet.service;



import com.example.cabinet.CabinetDTO;
import com.example.service_cabinet.dto.CreateCabinetRequest;
import com.example.service_cabinet.entity.Cabinet;
import com.example.service_cabinet.repository.CabinetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CabinetService {

    private final CabinetRepository cabinetRepository;
    private final Optional<KafkaProducerService> kafkaProducerService;

    public CabinetService(CabinetRepository cabinetRepository,
                          @Autowired(required = false) KafkaProducerService kafkaProducerService) {
        this.cabinetRepository = cabinetRepository;
        this.kafkaProducerService = Optional.ofNullable(kafkaProducerService);
    }

    // Créer un nouveau cabinet
    @Transactional
    public CabinetDTO createCabinet(CreateCabinetRequest request) {
        System.out.println("INFO: Création d'un nouveau cabinet: " + request.getNom());

        // Vérifier si le cabinet existe déjà
        if (cabinetRepository.existsByNom(request.getNom())) {
            throw new RuntimeException("Un cabinet avec ce nom existe déjà");
        }

        // Créer l'entité Cabinet
        Cabinet cabinet = new Cabinet();
        cabinet.setNom(request.getNom());
        cabinet.setLogo(request.getLogo());
        cabinet.setSpecialite(request.getSpecialite());
        cabinet.setAdresse(request.getAdresse());
        cabinet.setTelephone(request.getTelephone());
        cabinet.setEmail(request. getEmail());
        cabinet.setActif(request.getActif());

        // Sauvegarder en base
        Cabinet savedCabinet = cabinetRepository.save(cabinet);
        System.out.println("INFO: Cabinet créé avec ID: " + savedCabinet. getId());

        // Émettre un événement Kafka
//        kafkaProducerService.sendCabinetCreatedEvent(savedCabinet);

        // Retourner le DTO
        return convertToDTO(savedCabinet);
    }

    // Récupérer tous les cabinets
    public List<CabinetDTO> getAllCabinets() {
        System.out. println("INFO: Récupération de tous les cabinets");
        return cabinetRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors. toList());
    }

    // Récupérer un cabinet par ID
    public CabinetDTO getCabinetById(Long id) {
        System.out. println("INFO: Récupération du cabinet avec ID: " + id);
        Cabinet cabinet = cabinetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cabinet non trouvé avec ID: " + id));
        return convertToDTO(cabinet);
    }

    // Récupérer les cabinets actifs
    public List<CabinetDTO> getActiveCabinets() {
        System.out.println("INFO: Récupération des cabinets actifs");
        return cabinetRepository.findByActifTrue()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Mettre à jour un cabinet
    @Transactional
    public CabinetDTO updateCabinet(Long id, CreateCabinetRequest request) {
        System.out.println("INFO:  Mise à jour du cabinet avec ID: " + id);

        Cabinet cabinet = cabinetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cabinet non trouvé avec ID: " + id));

        // Vérifier si le nouveau nom n'est pas déjà pris
        if (! cabinet.getNom().equals(request.getNom()) &&
                cabinetRepository.existsByNom(request.getNom())) {
            throw new RuntimeException("Un cabinet avec ce nom existe déjà");
        }

        // Mettre à jour les champs
        cabinet.setNom(request.getNom());
        cabinet.setLogo(request.getLogo());
        cabinet.setSpecialite(request.getSpecialite());
        cabinet.setAdresse(request.getAdresse());
        cabinet.setTelephone(request.getTelephone());
        cabinet.setEmail(request.getEmail());
        cabinet.setActif(request.getActif());

        Cabinet updatedCabinet = cabinetRepository.save(cabinet);
        System.out.println("INFO: Cabinet mis à jour avec ID:  " + id);

        // Émettre un événement de mise à jour
//        kafkaProducerService.sendCabinetUpdatedEvent(updatedCabinet);

        return convertToDTO(updatedCabinet);
    }

    // Supprimer (désactiver) un cabinet
    @Transactional
    public void deleteCabinet(Long id) {
        System.out.println("INFO: Désactivation du cabinet avec ID: " + id);

        Cabinet cabinet = cabinetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cabinet non trouvé avec ID: " + id));

        cabinet.setActif(false);
        cabinetRepository.save(cabinet);
        System.out.println("INFO: Cabinet désactivé avec ID: " + id);

        // Émettre un événement de suppression
//        kafkaProducerService.sendCabinetDeletedEvent(id);
    }

    // Convertir Entity en DTO
    private CabinetDTO convertToDTO(Cabinet cabinet) {
        CabinetDTO dto = new CabinetDTO();
        dto.setId(cabinet.getId());
        dto.setLogo(cabinet.getLogo());
        dto.setNom(cabinet.getNom());
        dto.setSpecialite(cabinet.getSpecialite());
        dto.setAdresse(cabinet.getAdresse());
        dto.setTelephone(cabinet.getTelephone());
        dto.setEmail(cabinet.getEmail());
        dto.setActif(cabinet.getActif());
        dto.setCreatedAt(cabinet.getCreatedAt());
        dto.setUpdatedAt(cabinet.getUpdatedAt());
        return dto;
    }
}